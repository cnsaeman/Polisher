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
    public Pattern pattern;
    public Pattern exception;
    public int level;
    public int severity;
    
    public Rule(String msg, String regexp, String excp, int l, int s) {
        message=msg;
        pattern=Pattern.compile(regexp);
        if (excp.isEmpty()) {
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
        return("Rule:"+message+" level:"+Integer.toString(level)+" severity:"+Integer.toString(severity));
    }
}
