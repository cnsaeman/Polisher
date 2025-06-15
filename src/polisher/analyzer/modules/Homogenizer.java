package polisher.analyzer.modules;

import atlantis.tools.Parser;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import polisher.Profile;
import polisher.Resources;
import polisher.analyzer.Task;

/**
 *
 * @author cnsaeman
 */
public class Homogenizer extends CheckingModule {
    
    public static final String mainSwitch = "Global homogenizer";
    public static final List<String> subSwitches = List.of("apply to entire source");    
    
    public ArrayList<Homogenization> homogenizations;

    public Homogenizer(Resources RSC) {
        super(RSC);
    }

    @Override
    public String getMainSwitchString() {
        return(mainSwitch);
    }

    @Override
    public List<String> getSubSwitchStrings() {
        return(subSwitches);
    }

    @Override
    public void initForProfile(Profile profile) {
        homogenizations=new ArrayList<>();
        try {
            String wordList=Files.readString(Path.of(profile.folder+"/homogenizer.cfg"));
            // build hash
            for (String line : wordList.split("\n")) {
                if (!line.startsWith("%")) {
                    int i1=line.indexOf(":");
                    int i2=line.indexOf("|");
                    String preferredForm=line.substring(0,i1);
                    String alternatives=null;
                    String exceptions=null;
                    if (i2>-1) {
                        alternatives=line.substring(i1+1,i2);
                        exceptions=line.substring(i2+1);
                    } else {
                        alternatives=line.substring(i1+1);
                    }
                    homogenizations.add(new Homogenization(preferredForm,alternatives,exceptions));
                }
            }
        } catch (Exception ex) {
            RSC.outEx(ex);
        }
    }
    
    public void checkOn(Task task, StringBuilder text, int offset) {
        for (Homogenization homogenization : homogenizations) {
            for (String alternative : homogenization.alternatives) {
                int pos=text.indexOf(alternative);
                while (pos>-1) {
                    boolean matches=true;
                    for (String exception : homogenization.exceptions) {
                        if (text.indexOf(exception,pos)==pos) {
                            matches=false;
                            break;
                        }
                    }
                    if (matches) task.addProblem(this,offset+pos,offset+pos+alternative.length(),"Standard is "+homogenization.preferredForm+" instead of "+alternative+".",50);
                    pos = text.indexOf(alternative,pos+1);
                }
            }
        }
    }

    @Override
    public void check(Task task) {
        if (RSC.activeProfile.switchOn(mainSwitch)) {
            if (RSC.activeProfile.switchOn(mainSwitch+":"+subSwitches.get(0))) {
                checkOn(task,task.document.fulltext,0);
            } else {
                // TODO: only check in body
            }
        }
    }

    
}
