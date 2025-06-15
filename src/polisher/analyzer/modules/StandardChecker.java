package polisher.analyzer.modules;

import atlantis.latex.LaTeXDocument;
import polisher.Configuration;
import polisher.MainSourceView;

/**
 *
 * @author cnsaeman
 */
public class StandardChecker {

    public final MainSourceView MSV;
    public final Configuration configuration;
    public final LaTeXDocument document;
    
    public StandardChecker(MainSourceView MSV,Configuration configuration) {
        this.MSV=MSV;
        this.configuration=configuration;
        document=MSV.highlighter.document;
    }
    
    public void run() {
        
    }
    
}
