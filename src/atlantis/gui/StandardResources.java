package atlantis.gui;

import java.awt.Color;
import java.awt.Image;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *  This is the interface that all Resources files have to implement in order to collaborate with the Atlantis Tools.
 * 
 * @author cnsaeman
 */
public interface StandardResources {

    public void out(String s);
    public void outEx(Exception ex);
    
    public GuiTools getGuiTools();
    public Icons getIcons();
            
    public String getDir(String name);
    public void rememberDir(String name, String absolutePath);

}
