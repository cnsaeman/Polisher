/*
 * Class acting as an icon wallet.
 */

package atlantis.gui;

import atlantis.tools.Parser;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author cnsaeman
 */
public class Icons extends HashMap<String,ImageIcon> {
    
    public final String filesep = System.getProperty("file.separator");  // EndOfLine signal

    public final ArrayList<String> Types;
    public String basefolder;
    
    public final double guiScaleFactor;

    public Icons(Class clazz,String locationTree, String locationJAR, double guiScaleFactor) {
        super();
        this.guiScaleFactor=guiScaleFactor;
        try {
            for (String s : listOfIcons(clazz, locationTree,locationJAR)) {
                loadIcon(clazz,s);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Types=new ArrayList<String>();
    }
    
    private void readIn(String folder,String pre) {
        String[] flist = (new File(folder)).list();
        String n;
        for (int i = 0; (i < flist.length); i++) {
            if (flist[i].endsWith(".png")) {
                try {
                    ImageIcon I = new ImageIcon(Toolkit.getDefaultToolkit().getImage(folder+filesep+flist[i]));
                    n=Parser.cutUntilLast(flist[i], ".png");
                    I.setDescription(n);
                    put(pre+n, I);
                    Types.add(pre+n);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(flist[i]);
                }
            }
            if (new File(folder+filesep+flist[i]).isDirectory()) {
                readIn(folder+filesep+flist[i],flist[i]+filesep);
            }
        }        
    }
    
    public ArrayList<String> listOfIcons(Class clazz, String locationTree, String locationJAR) throws URISyntaxException, UnsupportedEncodingException, IOException {
        ArrayList<String> list=new ArrayList<>();
        URL dirURL = clazz.getResource(locationTree);
        if (dirURL!=null && dirURL.getProtocol().equals("file")) {
            // just a file, no jar file
            for (String entry : new File(dirURL.toURI()).list()) {
                if (entry.endsWith(".png")) list.add(entry);
            }
        } else { 
            if (dirURL == null) {
                String me = clazz.getName().replace(".", "/") + ".class";
                dirURL = clazz.getClassLoader().getResource(me);
            }
            if (dirURL.getProtocol().equals("jar")) {
                String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"));
                JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
                Enumeration<JarEntry> entries = jar.entries();
                String path=locationJAR;
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(path)) {
                        String entry = name.substring(path.length());
                        int checkSubdir = entry.indexOf("/");
                        if (checkSubdir >= 0) {
                            // if it is a subdirectory, we just return the directory name
                            entry = entry.substring(0, checkSubdir);
                        }
                        if (entry.endsWith(".png")) list.add(entry);
                    }
                }
            }
        }
        return(list);
    }

    private void loadIcon(Class clazz,String s) {
        try {
            ImageIcon I = new ImageIcon(Toolkit.getDefaultToolkit().getImage(clazz.getResource("images/" + s)));
            s=s.substring(0,s.length()-4);
            I.setDescription(s);
            put(s, I);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ImageIcon getIcon(String s) {
        //if (s==null) return(get("default"));
        if (s==null) return(null);
        if (s.equals("")) return(null);
        if (s.length()==0) return(get("default"));
        if (!containsKey(s)) return(get("notavailable"));
        return(get(s));
    }

    public DefaultComboBoxModel getDCBM() {
        DefaultComboBoxModel DCBM = new DefaultComboBoxModel();
        for (String ft : Types) {
            DCBM.addElement(ft);
        }
        return(DCBM);
    }
    
    public ImageIcon getScaledIcon(String s) {
        if (guiScaleFactor>1.9) {
            s=s+".2x";
        }
        //if (s==null) return(get("default"));
        if (s==null) return(null);
        if (s.equals("")) return(null);
        if (s.length()==0) return(get("default"));
        if (!containsKey(s)) return(get("notavailable"));
        return(get(s));
    }
    
    
   
}
