package polisher;

import atlantis.gui.GuiTools;
import atlantis.gui.Icons;
import atlantis.gui.StandardResources;
import atlantis.tools.TextFile;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import polisher.analyzer.modules.BibliographyChecker;
import polisher.analyzer.modules.CheckingModule;
import polisher.analyzer.modules.Homogenizer;
import polisher.analyzer.modules.PunctuationChecker;
import polisher.analyzer.modules.RegExpMatcher;

/**
 *
 * @author cnsaeman
 */
public class Resources implements StandardResources {
    
    public static final String ABOUT_STRING=
            """
            Polisher v1.0
            
            This is a Java Swing Application that helps to find standard typographical errors with LaTeX files, such as punctuation errors, double words, etc.
            
            (w) by C. Saemann, repository is found here: https://github.com/cnsaeman/Polisher
            
            """;


    public MainFrame MF;
    
    public TextFile repfile;
    
    public final GuiTools guiTools;
    public double guiScaleFactor=1.3;
    
    public Configuration folderMemory;
    
    public ArrayList<Profile> profiles;
    
    public ArrayList<CheckingModule> modules;
    
    public Profile activeProfile;
    
    public Resources() {
        try {
            repfile = new TextFile("polisher.log", true);
            out("Starting up Polisher ... ");
            out("---------------------------");
            out(new Date().toString());
        } catch (Exception ex) {
            outEx(ex);
            System.out.println("Couldn't start log file, stopping...");
            System.exit(255);
        }
        guiTools=new GuiTools(this,Toolkit.getDefaultToolkit().getImage(Resources.class.getResource("images/polisher.png")),guiScaleFactor);
        
        registerModules();
        readInProfiles();
        readInFolderMemory();
    }
    
    public void registerModules() {
        modules=new ArrayList<>();
        modules.add(new RegExpMatcher(this));
        modules.add(new Homogenizer(this));
        modules.add(new PunctuationChecker(this));
        modules.add(new BibliographyChecker(this));
    }
    
    public void readInProfiles() {
        profiles=new ArrayList<>();
        String[] folders=(new File("config")).list();
        for (String folder : folders) {
            try {
                if (new File("config/"+folder).isDirectory()) profiles.add(new Profile(this,"config/"+folder));
            } catch (Exception ex) {
                outEx(ex);
            }
        }
    }
    
    public void readInFolderMemory() {
        try {
            folderMemory=new Configuration(this,"folders.cfg"); 
        } catch (Exception ex) {
            outEx(ex);
        }
    }

    /**
     *
     * @param s
     */
    @Override
    public void out(String s) {
        if (repfile!=null) {
            try {
                repfile.putString(s);
            } catch (Exception e) {
                System.out.println("Error writing log.");
            }
        }
        System.out.println(s);
    }
    
    /**
     *
     * @param exception
     */
    @Override
    public void outEx(Exception exception) {
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        out(sw.toString());
        if (MF!=null) {
            //guiTools.showWarning("Exception:", sw.toString().substring(0,100));
        }
    }

    @Override
    public GuiTools getGuiTools() {
        return guiTools;
    }

    @Override
    public Icons getIcons() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getDir(String name) {
        String folderName=folderMemory.get(name);
        if (folderName==null) folderName=".";
        return(folderName);
    }

    @Override
    public void rememberDir(String name, String absolutePath) {
        folderMemory.put(name,absolutePath);
    }

    public void setMF(MainFrame MF) {
        this.MF=MF;
        this.guiTools.MF=MF;
    }
    
    public void switchProfileTo(Profile profile) {
        this.activeProfile=profile;
        for (CheckingModule module : modules) {
            module.initForProfile(profile);
        }
    }
    
    public void close() {
        folderMemory.writeBack();
    }
    
    
}
