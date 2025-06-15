package polisher.analyzer.modules;

import atlantis.latex.LaTeXCommand;
import atlantis.latex.LaTeXComponent;
import static atlantis.latex.LaTeXComponent.*;
import static atlantis.latex.LaTeXDocument.*;
import atlantis.latex.LaTeXEnvironment;
import java.util.List;
import polisher.Profile;
import polisher.Resources;
import polisher.analyzer.Task;

/**
 *
 * @author cnsaeman
 */
public class PunctuationChecker extends CheckingModule {
    
    public static final String mainSwitch = "Punctuation Checker";
    public static final List<String> subSwitches = List.of(
            "paragraph header needs to end with fullstop",
            "footnote punctuation"
    );    
    
    public PunctuationChecker(Resources RSC) {
        super(RSC);
    }
    
    @Override
    public String getMainSwitchString() {
        return(mainSwitch);
    }

    @Override
    public List<String> getSubSwitchStrings() {
        return(subSwitches);
    }
    
    @Override
    public void initForProfile(Profile profile) {
        
    }

    @Override
    public void check(Task task) {
        if (RSC.activeProfile.switchOn(mainSwitch)) {
            checkComponent(task,task.document.body);
        }
    }
    
    public void checkComponent(Task task, LaTeXComponent component) {
        StringBuilder fulltext=task.document.fulltext;
        if (component instanceof LaTeXCommand) {
            LaTeXCommand command=(LaTeXCommand)component;
            if (component.subtype > 39) {
                // Previous text needs to end with suitable puncutation mark
                checkSuitablePunctuationmarkBefore(task,command);
                
                // paragraph header needs to end with fullstop
                if ((component.subtype == 40 + LEVEL_PARAGRAPH) && RSC.activeProfile.switchOn(mainSwitch + ":paragraph header needs to end with fullstop")) {
                    if (!command.parameters.get(0).endsWith(".")) {
                        task.addProblem(this, component.start, component.end, "Paragraph header does not end with fullstop.", 60);
                    }
                }
            }
            
            if (command.subtype==SUBTYPE_FOOTNOTE) {
                // footnote punctuation
                if (RSC.activeProfile.switchOn(mainSwitch+":footnote punctuation")) {
                    String text=command.parameters.get(0);
                    char finalCharacter=text.charAt(text.length()-1);
                    if (Character.isLowerCase(text.charAt(0)) && (finalCharacter == '.')) {
                        task.addProblem(this, component.start,component.end, "Footnote started lowercase but finished with fullstop.", 60);
                    }
                    if ((finalCharacter == ',') || (finalCharacter == ';')) {
                        task.addProblem(this, component.start, component.end, "Footnote ended with inappropriate character", 60);
                    }
                }
            }
        } else if (component instanceof LaTeXEnvironment) {
                LaTeXEnvironment environment=(LaTeXEnvironment) component;
                
                if (environment.subtype==SUBTYPE_EQUATION) {
                    LaTeXComponent following=environment.nextSibling();
                    
                    // check for lower case continuation after fullstop in equation
                    if ((following!=null) && (following.type==TYPE_TEXT)) {
                        LaTeXComponent lastChild=((LaTeXEnvironment) component).children.getLast();
                        if ((lastChild.type == TYPE_ENVIRONMENT) && (lastChild.subtype == SUBTYPE_EQUATION_MULTILINER))
                            lastChild = lastChild.children.getLast();
                        if ((lastChild.type != TYPE_ENVIRONMENT) || (lastChild.subtype != SUBTYPE_DIAGRAM)) {
                            int pos = lastChild.end - 1;
                            // TODO exclude environments
                            while (Character.isWhitespace(fulltext.charAt(pos))) {
                                pos--;
                            }
                            char lastCharacter = fulltext.charAt(pos);
                            
                            if (Character.isLowerCase(fulltext.charAt(following.start))) {
                                if (lastCharacter=='.') {
                                    task.addProblem(this, component.start, component.end, "Equation ended with fullstop, but lower case continuation.", 60);
                                }
                            }   
                            
                            if (Character.isUpperCase(fulltext.charAt(following.start))) {
                                if (lastCharacter!='.') {
                                    task.addProblem(this, component.start, component.end, "Upper case after equation, but not fullstop at end of equation.", 60);
                                }
                            }   
                        
                            if (RSC.activeProfile.switchOn(mainSwitch + ":comma in equation before where") && (lastCharacter != ',') && (following.startsWith(fulltext,"where"))){
                                task.addProblem(this, component.start, component.end, "Equation followed by where needs to end in comma.", 60);
                            }
                            if (RSC.activeProfile.switchOn(mainSwitch + ":no comma in equation before with") && (lastCharacter == ',') && (following.startsWith(fulltext,"with"))) {
                                task.addProblem(this, component.start, component.end, "Equation followed by with but ended in comma.", 60);
                            }
                        }
                    }
                }
        } else if (component.type==TYPE_TEXT) {
            // check that text paragraphs are ended with appropriate punctuation sign
            int pos=fulltext.indexOf("\n\n",component.start);
            while ((pos>-1) && (pos<component.end)) {
                checkSuitablePunctuationmarkBefore(task, pos);
                pos=fulltext.indexOf("\n\n",pos+2);
            }
        }
        for (LaTeXComponent child : component.children) checkComponent(task,child);
    }

    private void checkSuitablePunctuationmarkBefore(Task task, int pos) {
        char lastChar=task.document.fulltext.charAt(pos);
        while(Character.isWhitespace(lastChar)) {
            pos--;
            lastChar=task.document.fulltext.charAt(pos);
        }
        
        // check if previous character is a suitable punctuation mark or a brace
        if (lastChar=='.') return;
        if (lastChar=='!') return;
        if (lastChar=='?') return;
        if (lastChar=='}') return;
        
        // if not, check if the previous text ends in a LaTeX command
        while(!Character.isWhitespace(lastChar)) {
            pos--;
            lastChar=task.document.fulltext.charAt(pos);
        }
        lastChar=task.document.fulltext.charAt(pos+1);
        if (lastChar=='\\') return;
        
        task.addProblem(this, pos-4, pos+10, "Expected end of sentence punctuation mark.", 60);
    }
    
    private void checkSuitablePunctuationmarkBefore(Task task,LaTeXComponent component) {
        checkSuitablePunctuationmarkBefore(task,component.start-1);
    }
    
    
}
