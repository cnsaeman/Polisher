package atlantis.latex;

import static atlantis.latex.LaTeXComponent.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author cnsaeman
 */
public class LaTeXDocument {

    public static final int LEVEL_PARAGRAPH = 5;
    
    public static final ArrayList<String> STRUCTURE_COMMANDS = new ArrayList<>(Arrays.asList(
        "part", "chapter", "section", "subsection", "subsubsection", "paragraph", "subparagraph"
    ));

    // Currently not used
    public static final ArrayList<String> COMMANDS_WITH_PARSEABLE_FIRST_ARGUMENT = new ArrayList<>(Arrays.asList(
        "footnote"
    ));
    
    public ArrayList<Integer> linebreaks;

    public StringBuilder fulltext;
    public LaTeXComponent componentTree;
    
    public LaTeXComponent body;
    public LaTeXComponent bibliography;

    public static LaTeXDocument fromFile(String fileName) {
        StringBuilder out = new StringBuilder();
        try {
            out.append(new String(Files.readAllBytes(Paths.get(fileName))));
            if (fileName.endsWith(".tex")) {
                String bblFileName = fileName.replace(".tex", ".bbl");
                if ((new File(bblFileName)).exists()) {
                    out.append(new String(Files.readAllBytes(Paths.get(bblFileName))));
                }
            }
        } catch (Exception e) {
            out = new StringBuilder("##??" + e.toString());
        }
        return (new LaTeXDocument(out));
    }

    public LaTeXDocument(StringBuilder fulltext) {
        this.fulltext = fulltext;
        findLineBreaks();
        componentTree = parseRoughStructure();
    }

    // current parsing data
    public int currentPosition;
    public int[] currentBounds;
    public LaTeXComponent currentComponent;

    public LaTeXComponent parseRoughStructure() {
        LaTeXComponent tree = new LaTeXComponent(fulltext);
        tree.type = LaTeXComponent.TYPE_LATEX_DOCUMENT;
        int bodyPosition = fulltext.indexOf("\\begin{document}");
        int bodyEnd = fulltext.indexOf("\\end{document}");
        int bibPosition = fulltext.indexOf("\\begin{thebibliography}");
        tree.children.add(new LaTeXComponent(LaTeXComponent.TYPE_HEADER, 0, bodyPosition, tree));
        body = new LaTeXEnvironment(LaTeXComponent.TYPE_CONTENT, bodyPosition, bibPosition, bodyPosition + 2, bodyEnd + "\\begin{document}".length(), "body", tree);
        tree.children.add(body);
        parseComponent(body);
        correctStructureInBody(body);
        bibliography = new LaTeXComponent(LaTeXComponent.TYPE_BIBLIOGRAPHY, bibPosition, fulltext.length(), tree);
        parseComponent(bibliography);
        correctStructureInBibliography(bibliography.children.get(0));
        tree.children.add(bibliography);
        return tree;
    }
    
    /**
     * Corrects the structure tree hierarchically, adding text etc. to the bibitem commands as children
     * 
     * @param component 
     */
    public void correctStructureInBody(LaTeXComponent component) {
        int pos=0;
        int level=100;
        LaTeXComponent[] currentStructureComponents=new LaTeXComponent[]{null,null,null,null,null,null,null};
        while (pos<component.children.size()) {
            LaTeXComponent child=component.children.get(pos);
            if ((child.type==TYPE_ENVIRONMENT) && (child.subtype==SUBTYPE_BODY)) correctStructureInBody(child);
            if ((child.type==TYPE_COMMAND) && (child.subtype - 40 >-1)) {
                if (child.subtype-40>level) {
                    child.parent=currentStructureComponents[level];
                    currentStructureComponents[level].children.add(child);
                    component.children.remove(pos);
                } else pos++;
                level = child.subtype - 40;
                currentStructureComponents[level] = child;
            } else {
                if (level<100) {
                    child.parent=currentStructureComponents[level];
                    currentStructureComponents[level].children.add(child);
                    component.children.remove(pos);
                } else pos++;
            }
        }
    }

    /**
     * Corrects the structure tree hierarchically, adding text etc. to the bibitem commands as children
     * 
     * @param component 
     */
    public void correctStructureInBibliography(LaTeXComponent component) {
        int pos=0;
        int mode=0; // 0 : looking for bibitem, 1 : adding text
        LaTeXComponent currentBibitem = null;
        while (pos<component.children.size()) {
            LaTeXComponent child=component.children.get(pos);
            if ((child.type==TYPE_COMMAND) && (child.subtype==SUBTYPE_BIBITEM)) {
                mode=1;
                currentBibitem=child;
                pos++;
            } else {
                if (mode==1) {
                    child.parent=currentBibitem;
                    component.children.remove(pos);
                    currentBibitem.children.add(child);
                } else pos++;
            }
        }
    }
    
    public boolean commandAtCurrentPosition() {
        return((fulltext.charAt(currentPosition) == '\\') 
                && (currentPosition<currentBounds[1]-1) 
                    && !Character.isWhitespace(fulltext.charAt(currentPosition+1)));
    }

    public void parseComponent(LaTeXComponent component) {
        int[] oldBounds=currentBounds;
        int oldPosition=currentPosition;
        LaTeXComponent oldComponent=currentComponent;
        
        currentBounds = component.getBounds();
        currentComponent = component;
        currentPosition = currentBounds[0];
        while (currentPosition < currentBounds[1]) {
            // Check for environments
            if (checkForString("\\begin{")) {
                parseEnvironment();
                continue;
            }

            // Check for commands
            if (commandAtCurrentPosition()) {
                parseCommand();
                continue;
            }

            // Check for math environments 
            // TODO distinguish: inline vs separate
            if (fulltext.charAt(currentPosition) == '$') {
                parseMathEnvironment();
                continue;
            }

            // Parse regular text
            parseText();
        }
        
        currentBounds=oldBounds;
        currentComponent=oldComponent;
        currentPosition=oldPosition;
    }

    private void parseMathEnvironment() {
        int startPos = currentPosition;
        currentPosition++; // Skip the $

        boolean isDisplayMath = false;
        if (currentPosition < currentBounds[1] && fulltext.charAt(currentPosition) == '$') {
            isDisplayMath = true;
            currentPosition++; // Skip the second $ for display math
        }

        StringBuilder mathContent = new StringBuilder();
        while (currentPosition < currentBounds[1]) {
            if (fulltext.charAt(currentPosition) == '$') {
                if (isDisplayMath) {
                    currentPosition++;
                    if (currentPosition < currentBounds[1] && fulltext.charAt(currentPosition) == '$') {
                        currentPosition++; // Skip the second $ for display math
                        break;
                    } else {
                        mathContent.append('$'); // Was just a single $, not closing
                    }
                } else {
                    currentPosition++; // Skip the closing $
                    break;
                }
            } else {
                mathContent.append(fulltext.charAt(currentPosition));
                currentPosition++;
            }
        }

        if (isDisplayMath) {
            LaTeXEnvironment environment = new LaTeXEnvironment(LaTeXComponent.TYPE_ENVIRONMENT,
                    startPos, currentPosition, startPos + 2,
                    currentPosition - 2, "equation", currentComponent);
            currentComponent.children.add(environment);
        } else {
            LaTeXComponent component = new LaTeXComponent(TYPE_INLINEMATH,startPos,currentPosition,currentComponent);
            currentComponent.children.add(component);
        }
    }

    private void parseText() {
        while (currentPosition < currentBounds[1] && Character.isWhitespace(fulltext.charAt(currentPosition))) currentPosition++;
        int startPos=currentPosition;
        while (currentPosition < currentBounds[1]
                && !commandAtCurrentPosition()
                && fulltext.charAt(currentPosition) != '$') currentPosition++;
        if (startPos<currentPosition) {
            LaTeXComponent text=new LaTeXComponent(TYPE_TEXT,startPos,currentPosition,currentComponent);
            currentComponent.children.add(text);
        }
    }

    private void parseEnvironment() {
        int startPos = currentPosition;
        currentPosition += "\\begin{".length();

        StringBuilder envName = new StringBuilder();
        while (currentPosition < fulltext.length() && fulltext.charAt(currentPosition) != '}') {
            envName.append(fulltext.charAt(currentPosition));
            currentPosition++;
        }
        currentPosition++; // Skip the closing brace

        String environmentName = envName.toString();

        // Parse environment content
        int nestedLevel = 1;
        String endTag = "\\end{" + environmentName + "}";

        while (currentPosition < fulltext.length() && nestedLevel > 0) {
            if (checkForString("\\begin{" + environmentName + "}")) {
                nestedLevel++;
                // Add the begin tag to content
                currentPosition += ("\\begin{" + environmentName + "}").length();
            } else if (checkForString(endTag)) {
                nestedLevel--;
                if (nestedLevel > 0) {
                    // This is a nested end tag, add it to content
                    currentPosition += endTag.length();
                } else {
                    // This is the closing tag for our environment
                    currentPosition += endTag.length();
                }
            } else {
                currentPosition++;
            }
        }

        LaTeXEnvironment environment = new LaTeXEnvironment(LaTeXComponent.TYPE_ENVIRONMENT,
                startPos, currentPosition, startPos + "\\begin{".length() + 1 + environmentName.length(),
                currentPosition - 1 - "\\end{".length() - environmentName.length(), environmentName, currentComponent);
        currentComponent.children.add(environment);
        parseComponent(environment);
    }

    private void parseCommand() {
        int startPos = currentPosition;
        currentPosition++; // Skip the backslash

        // Get command name
        StringBuilder commandName = new StringBuilder();
        while (currentPosition < currentBounds[1]
                && (Character.isLetter(fulltext.charAt(currentPosition)) || fulltext.charAt(currentPosition) == '*')) {
            commandName.append(fulltext.charAt(currentPosition));
            currentPosition++;
        }

        // If it's just a special character command like \$, \%, etc.
        if (commandName.length() == 0 && currentPosition < currentBounds[1]) {
            commandName.append(fulltext.charAt(currentPosition));
            currentPosition++;
            // turn into text
        } //else {

        String cmd = commandName.toString();

        // Parse parameters inside braces { }
        ArrayList<String> params = new ArrayList<>();
        while (currentPosition < currentBounds[1] && fulltext.charAt(currentPosition) == '{') {
            currentPosition++; // Skip opening brace
            StringBuilder param = new StringBuilder();
            int braceLevel = 1;

            while (currentPosition < currentBounds[1] && braceLevel > 0) {
                char c = fulltext.charAt(currentPosition);
                if (c == '{') {
                    braceLevel++;
                } else if (c == '}') {
                    braceLevel--;
                    if (braceLevel == 0) {
                        break; // Don't include the closing brace
                    }
                }
                param.append(c);
                currentPosition++;
            }

            if (currentPosition < currentBounds[1] && fulltext.charAt(currentPosition) == '}') {
                currentPosition++; // Skip closing brace
            }

            params.add(param.toString());
        }

        // Parse optional parameters inside brackets [ ]
        ArrayList<String> optionalParams = new ArrayList<>();
        while (currentPosition < currentBounds[1] && fulltext.charAt(currentPosition) == '[') {
            currentPosition++; // Skip opening bracket
            StringBuilder param = new StringBuilder();
            int bracketLevel = 1;

            while (currentPosition < currentBounds[1] && bracketLevel > 0) {
                char c = fulltext.charAt(currentPosition);
                if (c == '[') {
                    bracketLevel++;
                } else if (c == ']') {
                    bracketLevel--;
                    if (bracketLevel == 0) {
                        break; // Don't include the closing bracket
                    }
                }
                param.append(c);
                currentPosition++;
            }

            if (currentPosition < currentBounds[1] && fulltext.charAt(currentPosition) == ']') {
                currentPosition++; // Skip closing bracket
            }

            optionalParams.add(param.toString());
        }

        LaTeXCommand command = new LaTeXCommand(TYPE_COMMAND, startPos, currentPosition, cmd, params, optionalParams, currentComponent);
        currentComponent.children.add(command);
    }

    private boolean checkForString(String str) {
        if (currentPosition + str.length() > currentBounds[1]) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if (fulltext.charAt(currentPosition + i) != str.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    public void findLineBreaks() {
        linebreaks = new ArrayList<>();
        int i = fulltext.indexOf("\n");
        while (i > -1) {
            linebreaks.add(i);
            i = fulltext.indexOf("\n", i + 1);
        }
    }

    public int[] getPosition(int pos) {

        int row = 1;
        while ((row < linebreaks.size() + 1) && (linebreaks.get(row - 1) < pos)) {
            row++;
        }
        int lastRet = fulltext.lastIndexOf("\n", pos);
        int column = pos - lastRet;
        // correction for end of line
        if (column == 0) {
            column = pos - fulltext.lastIndexOf("\n", pos - 1);
        }
        return (new int[]{row, column});
    }

    public String toString() {
        return (componentTree.toStringBuilder("", fulltext).toString());
    }

    public LaTeXComponent getBody() {
        return(body);
    }

    public LaTeXComponent getBibliography() {
        return(bibliography);
    }

}
