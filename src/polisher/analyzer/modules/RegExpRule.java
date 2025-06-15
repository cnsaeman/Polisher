package polisher.analyzer.modules;

import atlantis.tools.Parser;
import java.util.regex.Pattern;

/**
 *
 * @author cnsaeman
 */
public class RegExpRule {
    public String message;
    public String regexp;
    public Pattern pattern;
    public int severity;
    public String level;
    public int flags; // 1 : exclude tags
    
    public RegExpRule(String line1, String line2) {
        level="0";
        severity=100;
        flags=0;
        String[] entries=line1.split("\\|");
        this.message = entries[0];
        if (entries.length>1) this.level=entries[1];
        if (entries.length>2) this.severity=Integer.valueOf(entries[2]);
        if (entries.length>3) {
            String[] flagList=entries[3].split(",");
            for (String flag : flagList) {
                if (flag.equals("not_in_tags")) flags+=1;
            }
        }
        this.regexp = line2.trim();
        pattern = Pattern.compile(regexp);
    }
    
    public String toString() {
        return("Rule:"+message+" regexp: "+regexp+" severity: "+severity);
    }
}
