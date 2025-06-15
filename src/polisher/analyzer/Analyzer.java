package polisher.analyzer;

import atlantis.latex.LaTeXDocument;
import java.util.Collections;
import polisher.MainSourceView;
import polisher.Resources;
import polisher.analyzer.modules.CheckingModule;

/**
 *
 * @author cnsaeman
 */
public class Analyzer {
    
    public final MainSourceView MSV;
    public final Resources RSC;
    
    public LaTeXDocument document;
    
    public int type;
    
    public Analyzer(MainSourceView MSV, Resources RSC, int type) {
        this.MSV=MSV; 
        this.RSC=RSC;
        this.document=MSV.highlighter.document;
        this.type=type; // -1 for all
    }
    
    public Task run() {
        Task task=new Task(MSV.highlighter.document);
        if (type==-1) {
            for (CheckingModule module : RSC.modules) {
                module.check(task);
            }
        } else {
            RSC.modules.get(type).check(task);
        }
        Collections.sort(task.problems);
        return(task);
    }
    
}
