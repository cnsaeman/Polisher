/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polisher;

/**
 *
 * @author cnsaeman
 */
class Problem {
    public int start;
    public int end;
    public String message;
    public int severity;
    
    public Problem(int s, int e, String msg, int sev) {
        start=s;
        end=e;
        message=msg;
        severity=sev;
    }
}
