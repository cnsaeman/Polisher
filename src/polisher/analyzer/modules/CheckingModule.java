package polisher.analyzer.modules;

import java.util.List;
import polisher.Profile;
import polisher.Resources;
import polisher.analyzer.Task;

/**
 *
 * @author cnsaeman
 */
public abstract class CheckingModule {
    
    public final Resources RSC;
    
    public CheckingModule(Resources RSC) {
        this.RSC=RSC;
    }
    
    public abstract String getMainSwitchString();
    
    public abstract List<String> getSubSwitchStrings();
    
    public abstract void initForProfile(Profile profile);
    
    public abstract void check(Task task);
    
    public boolean startsWith(StringBuilder builder, String string, int pos) {
        if (builder.length()<pos+string.length()) return(false);
        return(builder.substring(pos,pos+string.length()).equals(string));
    }
    
}
