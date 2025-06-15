package polisher;

import atlantis.tools.TextFile;
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
public class Configuration extends HashMap<String,String> {
    
    public Path path;
    public String fileName;
    public final Resources RSC;
    
    public Configuration(Resources RSC, String fileName) throws IOException {
        super();
        this.RSC=RSC;
        this.fileName=fileName;
        readIn();
    }
    
    /**
     * Read in configuration, either form directory or from file
     * 
     * @param folder
     * @throws IOException 
     */
    public void readIn() throws IOException {
        Path path = Paths.get(fileName);
        String[] config = new String(Files.readAllBytes(path)).split("\n");
        for (String line : config) {
            if (!line.trim().startsWith("%")) {
                int pos = line.lastIndexOf(":");
                if (pos > -1)
                    put(line.substring(0, pos), line.substring(pos + 1));
            }
        }
    }
    
    public void writeBack() {
        StringBuffer out=new StringBuffer();
        for(String key : keySet()) {
            out.append("\n").append(key).append(":").append(get(key));
        }
        TextFile.writeStringToFile(out.substring(1), fileName);
    }
        
}
