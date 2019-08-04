/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polisher;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JMenu;
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
      
    public static void main(String args[]) throws Exception {
        MF=new mainFrame();
        setComponentFont(MF.getComponents());
        MF.gui2(); 
    }
}
