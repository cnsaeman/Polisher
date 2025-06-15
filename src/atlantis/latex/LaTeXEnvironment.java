package atlantis.latex;

/**
 *
 * @author cnsaeman
 */
public class LaTeXEnvironment extends LaTeXComponent {
    
    public int innerStart;
    public int innerEnd;
    
    public String environmentName;
    
    public LaTeXEnvironment(int type, int start, int end, int innerStart, int innerEnd, String name, LaTeXComponent parent) {
        super(type, start, end, parent);
        this.innerStart=innerStart;
        this.innerEnd=innerEnd;
        this.environmentName=name;
        if (name.equals("body")) subtype=SUBTYPE_BODY;
        if (name.equals("equation")) subtype=SUBTYPE_EQUATION;
        if (name.equals("eqnarray")) subtype=SUBTYPE_EQUATION;
        if (name.equals("aligned") || name.equals("gathered")) subtype=SUBTYPE_EQUATION_MULTILINER;
        if (name.equals("tikzcd")) subtype=SUBTYPE_DIAGRAM;
    }
    
    @Override
    public StringBuilder description(StringBuilder fulltext) {
        return (new StringBuilder(COMPONENT_TYPES.get(type) + " with name "+environmentName+", start: " + start + ", end: " + end + ", innerStart: " + innerStart + ", innerEnd: " + innerEnd + ", first 30 characters: " + fulltext.substring(start, snippetEnd(fulltext))));
    }
    
    @Override
    public int[] getBounds() {
        return(new int[]{innerStart,innerEnd});
    }

}
