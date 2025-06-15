package atlantis.latex;

import static atlantis.latex.LaTeXDocument.STRUCTURE_COMMANDS;
import java.util.ArrayList;

/**
 *
 * @author cnsaeman
 */
public class LaTeXCommand extends LaTeXComponent {
    
    public String command;
    public ArrayList<String> parameters;
    public ArrayList<String> optionalParameters;
    
    public LaTeXCommand(int type, int start, int end, String command, ArrayList<String> parameters, ArrayList<String> optionalParameters, LaTeXComponent parent) {
        super(type, start, end, parent);
        this.command=command;
        if (command.equals("cite")) {
            subtype=SUBTYPE_CITATION;
        } else if (command.equals("label")) {
            subtype=SUBTYPE_LABEL;
        } else if (command.equals("eqref")) {
            subtype=SUBTYPE_REFERENCE;
        } else if (command.equals("bibitem")) {
            subtype=SUBTYPE_BIBITEM;
        } else if (command.equals("footnote")) {
            subtype=SUBTYPE_FOOTNOTE;
        } else {
            int structureLevel = STRUCTURE_COMMANDS.indexOf(command);
            if (structureLevel > 0) subtype = 40 + structureLevel;
        }
        this.parameters=parameters;
        this.optionalParameters=optionalParameters;
    }
    
    @Override
    public StringBuilder description(StringBuilder fulltext) {
        return (new StringBuilder(COMPONENT_TYPES.get(type) + " " + command+", subtype: "+subtype+", start: " + start + ", end: " + end + ", first 30 characters: " + fulltext.substring(start, snippetEnd(fulltext))));
    }
    
}
