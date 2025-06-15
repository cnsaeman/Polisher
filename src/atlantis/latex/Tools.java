package atlantis.latex;

import atlantis.latex.LaTeXEnvironment;


/**
 *
 * @author cnsaeman
 */
public class Tools {

    public static char lastCharacterInEquation(LaTeXEnvironment environment) {
        LaTeXComponent child=environment.children.getLast();
        return('"');
    }
    
}
