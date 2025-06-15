package atlantis.latex;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author cnsaeman
 */
public class LaTeXComponent {

    public final static int TYPE_LATEX_DOCUMENT = 0;          // header section
    public final static int TYPE_HEADER = 1;          // header section
    public final static int TYPE_CONTENT = 2;         // document environment
    public final static int TYPE_BIBLIOGRAPHY = 3;    // bibliography
    public final static int TYPE_TEXT=20;
    public final static int TYPE_ENVIRONMENT=21;
    public final static int TYPE_COMMAND=22;
    public final static int TYPE_INLINEMATH=23;
    
    public final static int SUBTYPE_BODY=5;
    public final static int SUBTYPE_CITATION=10;
    public final static int SUBTYPE_LABEL=11;
    public final static int SUBTYPE_REFERENCE=12;
    public final static int SUBTYPE_FOOTNOTE=19;
    public final static int SUBTYPE_EQUATION=20;
    public final static int SUBTYPE_EQUATION_MULTILINER=21;
    public final static int SUBTYPE_DIAGRAM=22;
    public final static int SUBTYPE_BIBITEM=30;

    public static final Map<Integer, String> COMPONENT_TYPES = Map.of(
            0, "LaTeX Document",
            1, "Document Header",
            2, "Document Content Environment",
            3, "Bibliography",
            20, "Text",
            21, "Environment",
            22, "Command",
            23, "Inline Math"
    );
    
    public static final Map<Integer, String> COMPONENT_SUBTYPES = Map.of(
            -1, "unknown",
            20,"equation",
            30, "bibitem",
            // structure commands from here...
            40,"part"
    );

    public int start;
    public int end;

    public int type;
    public int subtype;

    public ArrayList<LaTeXComponent> children;
    public LaTeXComponent parent;

    public LaTeXComponent(StringBuilder content) {
        children = new ArrayList<>();
        start = 0;
        end = content.length();
    }

    public LaTeXComponent(int type, int start, int end, LaTeXComponent parent) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.parent = parent;
        children = new ArrayList<>();
        subtype=-1;
    }
    
    public int snippetEnd(StringBuilder fulltext) {
        int snippetEnd=fulltext.indexOf("\n", start);
        if (end<snippetEnd) snippetEnd=end;
        if (snippetEnd>start+30) snippetEnd=start+30;
        return(snippetEnd);
    }

    public StringBuilder description(StringBuilder fulltext) {
        return (new StringBuilder(COMPONENT_TYPES.get(type) + ", start: " + start + ", end: " + end + ", first 30 characters: " + fulltext.substring(start, snippetEnd(fulltext))));
    }

    public StringBuilder toStringBuilder(String margin, StringBuilder fulltext) {
        StringBuilder out = new StringBuilder(margin + description(fulltext));
        for (LaTeXComponent child : children) {
            out.append("\n").append(child.toStringBuilder(margin + "...", fulltext));
        }
        return (out);
    }
    
    public int[] getBounds() {
        return(new int[]{start,end});
    }
    
    /**
     * Returns the endpoint of the last child or the endpoint of the component if no children.
     * @return 
     */
    public int getEndOfLastChild() {
        if (children.isEmpty()) return(end);
        return(children.getLast().end);
    }

    public LaTeXComponent previousSibling() {
        int pos=parent.children.indexOf(this);
        if (pos>1) return(parent.children.get(pos-1));
        return(null);
    }
    
    public LaTeXComponent nextSibling() {
        int pos=parent.children.indexOf(this);
        if (pos<parent.children.size()-1) return(parent.children.get(pos+1));
        return(null);
    }
    
    public boolean startsWith(StringBuilder fulltext, String substring) {
        int pos=start;
        while (Character.isWhitespace(fulltext.charAt(pos))) pos++;
        int len=substring.length();
        if (pos+len>=fulltext.length()) return(false);
        return(fulltext.substring(pos, pos+len).equals(substring));
    }

    
}
