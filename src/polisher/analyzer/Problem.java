package polisher.analyzer;

import polisher.analyzer.modules.CheckingModule;

/**
 *
 * @author cnsaeman
 */
public class Problem implements Comparable<Problem> {
    
    public int start;
    public int end;
    public CheckingModule module;
    public String description;
    public int severity;
    
    public Problem(CheckingModule module, int start, int end, String description, int severity) {
        this.module=module;
        this.start=start;
        this.end=end;
        this.description=description;
        this.severity=severity;
    }

    @Override
    public int compareTo(Problem problem) {
        return(Integer.compare(start, problem.start));
    }
    
}
