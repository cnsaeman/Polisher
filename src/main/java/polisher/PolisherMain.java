/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polisher;

import java.awt.Component;
import java.awt.Container;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;
import polisher.Resources;

/**
 *
 * @author cnsaeman
 */
public class PolisherMain {
    
    public static void setComponentFont(Component[] comp)
    {
        for(int x = 0; x < comp.length; x++)
        {
            if (comp[x] instanceof JMenu) {
                //System.out.println("Menu Component:"+((JMenu)comp[x]).getText());
                setComponentFont(((JMenu)comp[x]).getMenuComponents());
            } else 
            if (comp[x] instanceof Container) {
                //System.out.println("Component...");
                setComponentFont(((Container)comp[x]).getComponents());
            }     
          try {
              comp[x].setFont(new java.awt.Font("Arial", 0, 24));
          } catch(Exception e){}//do nothing
        }
    }  
    
    public static mainFrame MF;
    
    public static void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Resources.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(Resources.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Resources.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(Resources.class.getName()).log(Level.SEVERE, null, ex);
            }
        }    
        UIDefaults defaults = UIManager.getDefaults();
        final FontUIResource fnt11 = new FontUIResource(new java.awt.Font("SansSerif", 0, 22));
        final FontUIResource fnt12 = new FontUIResource(new java.awt.Font("SansSerif", 0, 24));
        Enumeration enumer = UIManager.getLookAndFeelDefaults().keys();
        while(enumer.hasMoreElements())
        {
          Object key = enumer.nextElement();
          Object value = UIManager.get(key);
          if (value instanceof javax.swing.plaf.FontUIResource)
          {
            UIManager.put( key, new javax.swing.plaf.FontUIResource(fnt12) );
          }
        }
        UIManager.getLookAndFeelDefaults().put("MenuBar.font",fnt12); 
        UIManager.getLookAndFeelDefaults().put("Menu.font",fnt12); 
        UIManager.getLookAndFeelDefaults().put("MenuItem.font",fnt12); 
        UIManager.getLookAndFeelDefaults().put("RadioButtonMenuItem.font",fnt12); 
        UIManager.getLookAndFeelDefaults().put("CheckBoxMenuItem.font",fnt12); 
        UIManager.getLookAndFeelDefaults().put("PopupMenu.font",fnt12); 
        UIManager.getLookAndFeelDefaults().put("PopupMenuItem.font",fnt12); 
        UIManager.getLookAndFeelDefaults().put("Button.font", fnt11);
        UIManager.getLookAndFeelDefaults().put("ToggleButton.font", fnt11);
        UIManager.getLookAndFeelDefaults().put("RadioButton.font", fnt11);
        UIManager.getLookAndFeelDefaults().put("CheckBox.font", fnt11);
        UIManager.getLookAndFeelDefaults().put("ComboBox.font", fnt11);
        UIManager.getLookAndFeelDefaults().put("List.font", fnt11);
        UIManager.getLookAndFeelDefaults().put("Label.font", fnt11);
        UIManager.getLookAndFeelDefaults().put("EditorPane.font", fnt11);
        UIManager.getLookAndFeelDefaults().put("Tree.font", fnt11);
        UIManager.getLookAndFeelDefaults().put("TitledBorder.font", fnt11);
        UIManager.getLookAndFeelDefaults().put("Table.font", fnt12);
        UIManager.getLookAndFeelDefaults().put("TableHeader.font", fnt12);
        UIManager.getLookAndFeelDefaults().put("TextField.font", fnt12);
        UIManager.getLookAndFeelDefaults().put("TextArea.font", fnt12);
        UIManager.getLookAndFeelDefaults().put("Menu.margin", new javax.swing.plaf.InsetsUIResource(4,4,4,4));
    }
    
    
      
    public static void main(String args[]) throws Exception {
        setLookAndFeel();
        MF=new mainFrame();
        setComponentFont(MF.getComponents());
        MF.gui2(); 
    }
}
