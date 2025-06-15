package polisher;

import atlantis.latex.LaTeXDocument;
import highlighter.SyntaxHighlighter;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.StyledDocument;
import polisher.analyzer.Problem;

/**
 *
 * @author cnsaeman
 */
public class MainSourceView extends JTextPane implements CaretListener {
    
    public final Resources RSC;
    public SyntaxHighlighter highlighter;
    public ArrayList<Problem> problems;
    public int fontSize;
    
    public Object currentPositionHighlighter;
    
    public MainSourceView(Resources RSC) {
        this.RSC=RSC;
        highlighter = new SyntaxHighlighter(RSC,this);
        addCaretListener(this);
        fontSize=getFont().getSize();
        problems=new ArrayList<>();
        
        currentPositionHighlighter=null;
    }
    
    public void setDocument(LaTeXDocument document) {
        highlighter.setDocument(document);
    }
    
    public void updateDisplay() {
        highlighter.updateSyntaxHighlighting();
        setCaretPosition(0);
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        displayPosition(e.getDot());
    }
    
    public void addCurrentPositionHighlight(int start, int end) {
        Highlighter highlighter=getHighlighter();
        if (currentPositionHighlighter!=null) highlighter.removeHighlight(currentPositionHighlighter);
        Highlighter.HighlightPainter painter = 
            new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
        try {
            currentPositionHighlighter = highlighter.addHighlight(start, end, painter);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    public void moveToProblemNumber(int j) {
        Problem problem = problems.get(j);
        scrollToVisible(problem.start, problem.end);
        addCurrentPositionHighlight(problem.start, problem.end);
        RSC.MF.showProblemText(problem.description);
        RSC.MF.problemNumberLabel.setText("Problem "+(j+1)+" out of "+problems.size());
    }
    
    public void moveToFirstProblem() {
        if (problems.size()==0) return;
        moveToProblemNumber(0);
    }

    public void moveToPreviousProblem() {
        if (problems.size()==0) return;
        int pos=getCaretPosition();
        int j=0;
        while ((j<problems.size()) && (problems.get(j).start<pos)) j++;
        if (j>0) {
            j--;
            moveToProblemNumber(j);
        }
    }

    public void moveToNextProblem() {
        if (problems.size()==0) return;
        int pos=getCaretPosition()+1;
        int j=0;
        while ((j<problems.size()) && (problems.get(j).start<pos)) j++;
        if (j<problems.size()) {
            moveToProblemNumber(j);
        }
    }

    public void moveToLastProblem() {
        if (problems.size()==0) return;
        moveToProblemNumber(problems.size()-1);
    }
    
    public void scrollToVisible(int start, int end) {
        setCaretPosition(start);
        // scroll with some borders
        try {
            Rectangle2D startR2D = modelToView2D(start);
            Rectangle2D endR2D = modelToView2D(end);
            int dist=RSC.guiTools.guiScale(50);
            int x=0;
            int y=(int)startR2D.getY();
            int height=(int)(endR2D.getY()+endR2D.getHeight())-y;
            int width=this.getWidth();
            if (y>dist) {
                y=y-dist;
                height=height+dist;
            } else {
                height=height+y;
                y=0;
            }
            if (y+height+dist<this.getHeight()) {
                height=height+dist;
            } else {
                height=getHeight()-y;
            }
            scrollRectToVisible(new Rectangle(x, y, width, height));
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    private void displayPosition(int pos) {
        int row=0;
        int column=0;
        if (highlighter.document!=null) {
            int[] location=highlighter.document.getPosition(pos);
            row=location[0];
            column=location[1];
        }
        RSC.MF.displayPosition(row,column);
    }

    public void addProblem(Problem problem) {
        this.highlighter.addError(problem.start, problem.end);
        problems.add(problem);
    }

    public void updateSyntaxHighlighting() {
        highlighter.updateSyntaxHighlighting();
    }
    
    public void increaseFontSize() {
        fontSize++;
        setFontSize();
    }
    
    public void decreaseFontSize() {
        fontSize--;
        setFontSize();
    }
    
    public void setFontSize() {
        setFont(new FontUIResource(new java.awt.Font("Monospaced", 0, fontSize)));        
    }

    public void setProblems(ArrayList<Problem> problems) {
        this.problems=problems;
        if (problems.size()==0) {
            RSC.MF.problemNumberLabel.setText("No problems found.");
        } else {
            RSC.MF.problemNumberLabel.setText(problems.size()+" problems found.");
        }
    }
    
}
