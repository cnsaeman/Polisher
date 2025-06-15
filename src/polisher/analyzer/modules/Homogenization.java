package polisher.analyzer.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author cnsaeman
 */
public class Homogenization {
    
    public String preferredForm;
    public List<String> alternatives;
    public List<String> exceptions;
    
    public Homogenization(String preferredForm, String alternativesString, String exceptionsString) {
        this.preferredForm=preferredForm;
        alternatives=Arrays.asList(alternativesString.split(";"));
        if ((exceptionsString!=null) && (exceptionsString.length()>0)) {
            exceptions=Arrays.asList(alternativesString.split(";"));
        } else {
            exceptions=new ArrayList<>();
        }
    }
    
    
}
