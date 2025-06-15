package polisher;

import atlantis.tools.TextFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import polisher.analyzer.modules.CheckingModule;
import static polisher.analyzer.modules.Homogenizer.mainSwitch;

/**
 *
 * @author cnsaeman
 */
public class Profile extends Configuration {
   
    public String folder;
    public HashMap<String,String> homogenizationList;
    
    public Profile(Resources RSC, String folder) throws IOException {
        super(RSC, folder+"/profile.cfg");
        this.folder=folder;
    }
    
    public boolean switchOn(String switchName) {
        if (!containsKey(switchName)) return(true);
        return(get(switchName).toLowerCase().equals("true"));
    }
    
    public void writeBack() {
        StringBuffer out=new StringBuffer();
        for(String key : keySet()) {
            out.append("\n").append(key).append(":").append(get(key));
        }
        for (CheckingModule module : RSC.modules) {
            out.append("\n"+module.getMainSwitchString()+":"+switchOn(module.getMainSwitchString()));
            for (String subswitch : module.getSubSwitchStrings()) {
                out.append("\n"+module.getMainSwitchString()+":"+subswitch+":"+switchOn(module.getMainSwitchString()+":"+subswitch));
            }
        }
        TextFile.writeStringToFile(out.substring(1), fileName);
    }
    
    
}
