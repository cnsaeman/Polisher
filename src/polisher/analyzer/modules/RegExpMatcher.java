package polisher.analyzer.modules;

import atlantis.latex.LaTeXComponent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import polisher.Profile;
import polisher.Resources;
import polisher.analyzer.Task;

/**
 *
 * @author cnsaeman
 */
public class RegExpMatcher extends CheckingModule {

    public static final String mainSwitch = "Regular Expression checker";
    public static final List<String> subSwitches = List.of("apply to entire source");
    
    ArrayList<RegExpRule> rules;
    
    public RegExpMatcher(Resources RSC) {
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
        rules=new ArrayList<>();
        try {
            String rulesFile[] = Files.readString(Path.of(profile.folder+"/regexps.cfg")).split("\n");
            int line=0;
            int mode=0; // 1: homogenization, 2: rules
            while (line<rulesFile.length) {
                if (!rulesFile[line].startsWith("%")) {
                    try {
                        RegExpRule rule = new RegExpRule(rulesFile[line], rulesFile[line + 1]);
                        line++;
                        rules.add(rule);
                    } catch (Exception ex) {
                        RSC.out("Error reading rule: ");
                        RSC.out("  line: " + line);
                        RSC.out("  message: " + rulesFile[line]);
                        RSC.out("  regexp: " + rulesFile[line + 1]);
                    }
                }
                line++;
            }
        } catch(Exception ex) {
            RSC.outEx(ex);
        }
    }
    
    public void checkRuleAt(RegExpRule rule, Task task, LaTeXComponent component) {
        Matcher m = rule.pattern.matcher(task.document.fulltext);
        m.region(component.start, component.end);
        while (m.find()) {
            boolean found=true;
            // work through tags;
            if ((rule.flags & 1)==1) {
                int p1=task.document.fulltext.lastIndexOf("{",m.start());
                int p2=task.document.fulltext.lastIndexOf("}",m.start());
                if (p1>p2) {
                    // inside a { ... }
                    String test=task.document.fulltext.substring(p1-5,p1);
                    if (task.document.fulltext.substring(p1-5,p1).equals("\\cite")) found=false;
                    if (task.document.fulltext.substring(p1-5,p1).equals("\\cref")) found=false;
                    if (task.document.fulltext.substring(p1-6,p1).equals("\\eqref")) found=false;
                    if (task.document.fulltext.substring(p1-6,p1).equals("\\label")) found=false;
                }
            }
            if (found) task.addProblem(this, m.start(), m.end(), rule.message, rule.severity);
        }
    }

    @Override
    public void check(Task task) {
        for (RegExpRule rule : rules) {
            if (rule.level.equals("0")) {
                //checkRuleAt(rule,task,task.document);
            } else if (rule.level.equals("1")) {
                checkRuleAt(rule,task,task.document.getBody());
            } else if (rule.level.equals("90")) {
                checkRuleAt(rule,task,task.document.getBibliography());
            } else {
                checkRuleAt(rule,task,task.document.getBody());
                checkRuleAt(rule,task,task.document.getBibliography());
            }
        }
    }
    
}
