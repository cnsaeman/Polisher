package polisher.analyzer;

import atlantis.latex.LaTeXDocument;
import java.util.ArrayList;
import java.util.Collections;
import polisher.analyzer.modules.CheckingModule;

/**
 *
 * @author cnsaeman
 */
public class Task {
    
    public final LaTeXDocument document;
    public ArrayList<Problem> problems;
    
    public Task(LaTeXDocument document) {
        this.document=document;
        problems=new ArrayList<>();        
    }

    public void addProblem(CheckingModule module, int start, int end, String description, int severity) {
        problems.add(new Problem(module,start,end,description,severity));
    }

    public String getProblems() {
        StringBuffer out=new StringBuffer();
        Collections.sort(problems);
        for (Problem problem : problems) {
            int[] position=document.getPosition(problem.start);
            out.append("\nat line ").append(position[0]).append(", column ").append(position[1]).append(": ").append(problem.description);
        }
        return(out.substring(1));
    }
    
}
