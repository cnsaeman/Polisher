/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polisher;

import java.util.regex.Pattern;

/**
 *
 * @author cnsaeman
 */
public class Rule {
    public String message;
    public String regexp;
    public Pattern pattern;
    public Pattern exception;
    public int level;
    public int severity;
    
    public Rule(String msg, String re, String excp, int l, int s) {
        message=msg;
        regexp=re;
        pattern=Pattern.compile(re);
        if (excp==null || excp.isEmpty()) {
            exception=null;
        } else {
            if (l==2) {
                exception=Pattern.compile(excp,Pattern.DOTALL);
            } else {
                exception=Pattern.compile(excp);
            }
        }
        level=l;
        severity=s;
    }
    
    public String toString() {
        return("Rule:"+message+" regexp: "+regexp+" level:"+Integer.toString(level)+" severity:"+Integer.toString(severity));
    }
}
