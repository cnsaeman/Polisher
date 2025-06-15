package polisher.analyzer.modules;

import atlantis.latex.LaTeXCommand;
import atlantis.latex.LaTeXComponent;
import static atlantis.latex.LaTeXComponent.*;
import atlantis.latex.LaTeXEnvironment;
import atlantis.tools.Parser;
import atlantis.tools.TextFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import polisher.Profile;
import polisher.Resources;
import polisher.analyzer.Task;

/**
 *
 * @author cnsaeman
 */
public class BibliographyChecker extends CheckingModule {

    public static final String mainSwitch = "Bibliography checker";
    public static final List<String> subSwitches = List.of("apply to entire source");    
    
    public HashMap<String,String> journalNormalization;

    public BibliographyChecker(Resources RSC) {
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
        journalNormalization=new HashMap<>();
        String[] config=TextFile.readOutFile(profile.folder+"/bibliography.cfg").split("\\n");
        for (String line : config) {
            if (line.startsWith("journal-normalization")) {
                String[] entries=line.split(":");
                if (entries.length==3) journalNormalization.put(entries[1],entries[2]);
            }
        }
    }

    @Override
    public void check(Task task) {
        if (RSC.activeProfile.switchOn(mainSwitch)) {
            checkComponent(task,task.document.bibliography);
        }
    }
    
    public void checkComponent(Task task, LaTeXComponent component) {
        StringBuilder fulltext=task.document.fulltext;
        // check for bibitem
        if ((component.type==TYPE_COMMAND) && (component.subtype==SUBTYPE_BIBITEM)) {
            
            // check that it ends in fullstop
            LaTeXComponent lastText=component.children.getLast();
            String bibitemText=fulltext.substring(lastText.start,lastText.end);
            // remove all comments
            String cleanText=bibitemText.trim();
            int i=cleanText.indexOf('%');
            while (i>-1) {
                int p=cleanText.indexOf('\n',i);
                String front=cleanText.substring(0,i);
                if (p==-1) {
                    cleanText=front;
                } else {
                    cleanText=front+cleanText.substring(p+1);
                }
                i=cleanText.indexOf('%');
            }
            cleanText=cleanText.trim();
            if (cleanText.charAt(cleanText.length()-1)!='.') {
                task.addProblem(this, lastText.start, lastText.end+2, "Bibitem must end with fullstop.", 60);
            }
            
            // extract journal
            String journal=fulltext.substring(component.start,lastText.end);
            if (journal.contains("``")) {
                journal=Parser.cutFrom(journal,"''");
            } else if (journal.contains("{\\")) {
                journal=Parser.mCuts(journal,"<{\\","<}");
            }
            for (String key : journalNormalization.keySet()) {
                if (journal.contains(key)) {
                    task.addProblem(this,component.start,lastText.end,"Journal name should be normalized from "+key+" to "+journalNormalization.get(key),60);
                }
            }
            
            
            
        }
        for (LaTeXComponent child : component.children) checkComponent(task,child);
    }

    
}
