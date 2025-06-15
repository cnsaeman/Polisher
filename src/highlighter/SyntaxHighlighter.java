package highlighter;

import atlantis.latex.LaTeXComponent;
import static atlantis.latex.LaTeXComponent.*;
import atlantis.latex.LaTeXDocument;
import atlantis.latex.LaTeXEnvironment;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import polisher.MainSourceView;
import polisher.Resources;

/**
 *
 * @author cnsaeman
 */
public class SyntaxHighlighter {
    
    public final Resources RSC;
    
    public final MainSourceView MSV;

    public LaTeXDocument document;
    
    public ArrayList<SimpleAttributeSet> styles;
    public ArrayList<ArrayList<int[]>> ranges;
    
    public StringBuffer html;
    
    public SyntaxHighlighter(Resources RSC, MainSourceView MSV) {
        this.RSC=RSC;
        this.MSV=MSV;
        styles=new ArrayList<>();
        ranges=new ArrayList<>();
        
        // header: 0 light gray
        SimpleAttributeSet sas1 = new SimpleAttributeSet();
        StyleConstants.setForeground(sas1, Color.LIGHT_GRAY);
        styles.add(sas1);
        ranges.add(new ArrayList<>());
        
        // structural elements: 1 black and bold
        SimpleAttributeSet sas2 = new SimpleAttributeSet();
        StyleConstants.setForeground(sas2, Color.BLACK);
        StyleConstants.setBold(sas2, true);
        styles.add(sas2);
        ranges.add(new ArrayList<>());

        // environments: 2 magenta
        SimpleAttributeSet sas3 = new SimpleAttributeSet();
        StyleConstants.setForeground(sas3, new Color(153, 0, 153));
        styles.add(sas3);
        ranges.add(new ArrayList<>());

        // citations: 3 light blue
        SimpleAttributeSet sas4 = new SimpleAttributeSet();
        StyleConstants.setForeground(sas4, new Color(0, 51, 153));
        styles.add(sas4);
        ranges.add(new ArrayList<>());

        // equations: 4 green
        SimpleAttributeSet sas6 = new SimpleAttributeSet();
        StyleConstants.setForeground(sas6, new Color(0, 153, 0));
        styles.add(sas6);
        ranges.add(new ArrayList<>());

        // labels: 5 orange
        SimpleAttributeSet sas5 = new SimpleAttributeSet();
        StyleConstants.setForeground(sas5, new Color(230, 138, 0));
        styles.add(sas5);
        ranges.add(new ArrayList<>());

        // structural elements: cyan
        SimpleAttributeSet sas7 = new SimpleAttributeSet();
        StyleConstants.setForeground(sas7, new Color(0, 153, 255));
        styles.add(sas7);
        ranges.add(new ArrayList<>());

        // Error: red
        SimpleAttributeSet sas8 = new SimpleAttributeSet();
        StyleConstants.setForeground(sas8, new Color(255, 51, 0));
        styles.add(sas8);
        ranges.add(new ArrayList<>());
        
    }
    
    public void setDocument(LaTeXDocument document) {
        this.document=document;
        addMarking(document.componentTree);
    }
    
    public void addMarking(LaTeXComponent component) {
        switch (component.type) {
            case TYPE_HEADER :
                ranges.get(0).add(component.getBounds());
                break;
            case TYPE_COMMAND :
                switch (component.subtype) {
                    case SUBTYPE_CITATION :
                        ranges.get(3).add(component.getBounds());
                        break;
                    case SUBTYPE_BIBITEM : 
                        int end=component.getEndOfLastChild();
                        ranges.get(5).add(component.getBounds());
                        ranges.get(4).add(new int[]{component.getBounds()[1],end});
                        break;
                    case SUBTYPE_FOOTNOTE : 
                        ranges.get(6).add(component.getBounds());
                        break;
                    case SUBTYPE_LABEL : 
                        ranges.get(5).add(component.getBounds());
                        break;
                    case SUBTYPE_REFERENCE : 
                        ranges.get(5).add(component.getBounds());
                        break;
                    default : {
                        if (component.subtype>39) ranges.get(1).add(component.getBounds());
                    }    
                }
                break;
            case TYPE_ENVIRONMENT : 
                LaTeXEnvironment environment=(LaTeXEnvironment) component;
                ranges.get(2).add(new int[]{environment.start,environment.innerStart});
                ranges.get(2).add(new int[]{environment.innerEnd,environment.end});
                if (environment.subtype==SUBTYPE_EQUATION) 
                    ranges.get(4).add(new int[]{environment.innerStart,environment.innerEnd});
                break;
            case TYPE_INLINEMATH : 
                ranges.get(4).add(component.getBounds());
        }
        for (LaTeXComponent child : component.children) {
            addMarking(child);
        }
    }
    
    public void addError(int start, int end) {
        ranges.get(7).add(new int[]{start,end});
    }
    
    public String getHTML() {
        return(html.toString());
    }
    
    public void updateSyntaxHighlighting() {
        MSV.setForeground(Color.GRAY);
        MSV.setText(document.fulltext.toString());
        StyledDocument doc = MSV.getStyledDocument();
        SimpleAttributeSet empty = new SimpleAttributeSet();
        doc.setCharacterAttributes(0, doc.getLength(), empty, true);
        for (int i=0;i<styles.size();i++) {
            SimpleAttributeSet style=styles.get(i);
            for (int[] range : ranges.get(i)) {
                doc.setCharacterAttributes(range[0], range[1]-range[0], style, true);
            }
        }
    }
    
}
