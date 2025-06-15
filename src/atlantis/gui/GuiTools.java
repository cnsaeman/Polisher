package atlantis.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.Enumeration;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author cnsaeman
 */
public class GuiTools {
    
    public final StandardResources RSC;
    public JFrame MF;
    public final Image appIcon;
    
    public double guiScaleFactor;
    
    public final static Object[] OPTIONS_YES_NO_CANCEL = { "Yes", "No", "Cancel" };
    public final static Object[] OPTIONS_OK_CANCEL = { "OK", "Cancel" };
    public final static Object[] OPTIONS_YES_NO = { "Yes", "No" };    
    public final static int OPTIONS_YES = JOptionPane.YES_OPTION;
    public final static int OPTIONS_NO = JOptionPane.NO_OPTION;
    public final static int OPTIONS_CANCEL = JOptionPane.CANCEL_OPTION;
    
    public FontUIResource fnt11;
    public FontUIResource fnt12;
    public FontUIResource fntfixed12;
    
    public GuiTools(StandardResources RSC,Image appIcon,double guiScaleFactor) {
        this.RSC=RSC;
        this.MF=MF;
        this.appIcon=appIcon;
        this.guiScaleFactor=guiScaleFactor;
        initFonts();
        setLookAndFeel();
    }
    
    public void setMainFrame(JFrame MF) {
        this.MF=MF;
    }
    
    public void setScaleFactor(double scaleFactor) {
        
    }
    
    public int guiScale(int v) {
        return (int) (v*guiScaleFactor);
    }
    
    public void initFonts() {
        fnt11 = new FontUIResource(new java.awt.Font("SansSerif", 0, guiScale(11)));
        fnt12 = new FontUIResource(new java.awt.Font("SansSerif", 0, guiScale(12)));
        fntfixed12 = new FontUIResource(new java.awt.Font("Monospaced", 0, guiScale(12)));
    }
    
    public FontUIResource getFixedFont(int size) {
        return(new FontUIResource(new java.awt.Font("Monospaced", 0, guiScale(size))));
    }
    
    /**
     * Center the current JDialog frame over main frame
     */
    public void centerDialog(JDialog frame) {
        Point p=MF.getLocationOnScreen();
        Dimension d=MF.getSize();
        frame.setLocation(p.x+(d.width/2)-(frame.getWidth()/2),p.y+(d.height/2)-(frame.getHeight()/2));
        /*Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        frame.setLocation(bounds.x+dm.getWidth()/2 - (frame.getWidth()/2),bounds.y+dm.getHeight()/2 - (frame.getHeight()/2));*/
    }

    /**
     * Center the current JDialog frame over owner frame
     */
    public void centerDialog(JDialog frame,Frame owner) {
        Point p=owner.getLocationOnScreen();
        Dimension d=owner.getSize();
        frame.setLocation(p.x+(d.width/2)-(frame.getWidth()/2),p.y+(d.height/2)-(frame.getHeight()/2));
        /*Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        frame.setLocation(bounds.x+dm.getWidth()/2 - (frame.getWidth()/2),bounds.y+dm.getHeight()/2 - (frame.getHeight()/2));*/
    }
    
    public void scaleAndCenter(JDialog frame, Frame owner, int preferredWidth, int preferredHeight, double scale) {
        Rectangle screen=owner.getGraphicsConfiguration().getBounds();
        int maxWidth=(int)(scale*(double)screen.width);
        int maxHeight=(int)(scale*(double)screen.height);
        if (preferredWidth>maxWidth) preferredWidth=maxWidth;
        if (preferredHeight>maxHeight) preferredHeight=maxHeight;
        frame.setSize(preferredWidth,preferredHeight);
        Point p=owner.getLocationOnScreen();
        Dimension d=owner.getSize();
        frame.setLocation(p.x+(d.width/2)-(frame.getWidth()/2),p.y+(d.height/2)-(frame.getHeight()/2));
    }
    
    public void scaleAndCenter(JFrame frame, Frame owner, int preferredWidth, int preferredHeight, double scale) {
        Rectangle screen=owner.getGraphicsConfiguration().getBounds();
        int maxWidth=(int)(scale*(double)screen.width);
        int maxHeight=(int)(scale*(double)screen.height);
        if (preferredWidth>maxWidth) preferredWidth=maxWidth;
        if (preferredHeight>maxHeight) preferredHeight=maxHeight;
        frame.setSize(preferredWidth,preferredHeight);
        Point p=owner.getLocationOnScreen();
        Dimension d=owner.getSize();
        frame.setLocation(p.x+(d.width/2)-(frame.getWidth()/2),p.y+(d.height/2)-(frame.getHeight()/2));
    }
    
    /**
     * Center the given JFrame over main frame
     */
    public void centerFrame(JFrame frame) {
        Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        frame.setLocation(bounds.x+dm.getWidth()/2 - (frame.getWidth()/2),bounds.y+dm.getHeight()/2 - (frame.getHeight()/2));
    }
    
    /**
     * Shows an information message dialog
     * 
     * @param title : title of dialog
     * @param msg : message to be displayed
     */
    public void showInformation(String title, String msg) {        
        JOptionPane.showMessageDialog(MF,msg,title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a long information message dialog
     * 
     * @param title : title of dialog
     * @param message : message to be displayed
     */
    public void showLongInformation(String title, String message, boolean modal) {        
        MultiLineMessage MLM=new MultiLineMessage(this,title,message,modal);
        MLM.setVisible(true);
    }
    
    /**
     * Shows a warning message dialog
     * 
     * @param title : title of dialog
     * @param msg : message to be displayed
     */
    public void showWarning(String title,String message,Component component) {
        JOptionPane.showMessageDialog(component, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public void showWarning(String title,String message) {
        showWarning(title,message,MF);
    }
    
    /**
     * Question dialog with options OC
     * 
     * @param message
     * @param title
     * @return 
     */
    public int askQuestionOC(String title, String message, Component component) {
        return(JOptionPane.showOptionDialog(component,message, title, JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,null, OPTIONS_OK_CANCEL, OPTIONS_OK_CANCEL[0]));
    }
    
    /**
     * Returns a confirmation for an action, check if return value maches JOptionPane.OK_OPTION
     * 
     * @param title
     * @param message
     * @return 
     */
    public int askQuestionOC(String title, String message) {
        return(askQuestionOC(title,message,MF));
    }    
    
    /**
     * Question dialog with options YNC
     * 
     * @param title
     * @param message
     * @return 
     */
    public int askQuestionYNC(String title, String message) {
        return(JOptionPane.showOptionDialog(MF, message, title,
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
                null, OPTIONS_YES_NO_CANCEL, OPTIONS_YES_NO_CANCEL[0]));
    }
    
    /**
     * Question dialog with options YN
     * 
     * @param title
     * @param message
     * @return 
     */
    public int askQuestionYN(String title, String message) {
        return(JOptionPane.showOptionDialog(MF, message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, OPTIONS_YES_NO, OPTIONS_YES_NO[0]));
    }
    
    /**
     * Question dialog with two choices: A,B
     */
    public int askQuestionAB(String title, String message, String optionA, String optionB) {
        Object[] options=new Object[2];
        options[0]=optionA; options[1]=optionB;
        return(JOptionPane.showOptionDialog(MF, message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]));
    }

    /**
     * Question dialog with three choices: A, B, C
     */
    public int askQuestionABC(String title, String message, String optionA, String optionB, String optionC) {
        Object[] options=new Object[3];
        options[0]=optionA; options[1]=optionB; options[2]=optionC;
        return(JOptionPane.showOptionDialog(MF, message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]));
    }
            
    /**
     * Question dialog with three choices: A, B, C
     */
    public int askQuestionABCD(String title, String message, String optionA, String optionB, String optionC, String optionD) {
        Object[] options=new Object[4];
        options[0]=optionA; options[1]=optionB; options[2]=optionC; options[3]=optionD;
        return(JOptionPane.showOptionDialog(MF, message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]));
    }
    
    /**
     * Displays a SingleLineEditor to get a string
     * 
     * @param header
     * @param initial
     * @param content : whether initial is content or just grayed information
     * @return 
     */
    public String askQuestionString(String title, String initialText, boolean content) {
        final SingleLineEditor SLE = new SingleLineEditor(this, title, initialText,content);
        SLE.setVisible(true);
        if (!SLE.cancelled) {
            return(SLE.text);
        }
        return(null);        
    }


    /**
     * Let user select folder
     *
     * @param RSC
     * @param title : title of dialogue
     * @param name : name to remember directory
     * @return
     */
    public String selectFolder(String title, String name) {
        JFileChooser FC = new JFileChooser();
        FC.setDialogTitle(title);
        FC.setCurrentDirectory(new File(RSC.getDir(name)));
        FC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        FC.setDialogType(JFileChooser.OPEN_DIALOG);
        FC.setFileFilter(new FFilter("_DIR", "Folders"));
        // cancelled?
        if (!(FC.showOpenDialog(MF) == JFileChooser.CANCEL_OPTION)) {
            RSC.rememberDir(name, FC.getSelectedFile().getAbsolutePath());
            return FC.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    /**
     * Let user select a file for opening
     *
     * @param RSC
     * @param title : title of dialogue
     * @param name : name to remember directory
     * @param fileTypeShort : e.g. .csv, _ALL for all files
     * @param fileTypeLong : e.g. CSV-files
     * @return
     */
    public String selectFileForOpen(String title, String name, String fileTypeShort, String fileTypeLong) {
        JFileChooser FC = new JFileChooser();
        FC.setDialogTitle(title);
        FC.setCurrentDirectory(new File(RSC.getDir(name)));
        FC.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FC.setDialogType(JFileChooser.OPEN_DIALOG);
        FC.setFileFilter(new FFilter(fileTypeShort, fileTypeLong));
        // cancelled?
        if (!(FC.showOpenDialog(MF) == JFileChooser.CANCEL_OPTION)) {
            RSC.rememberDir(name, FC.getSelectedFile().getParent());
            return FC.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    /**
     * Let user select a file for saving
     *
     * @param RSC
     * @param title : title of dialogue
     * @param name : name to remember directory
     * @param fileTypeShort : e.g. .csv
     * @param fileTypeLong : e.g. CSV-files
     * @return
     */
    public String selectFileForSave(String title, String name, String fileTypeShort, String fileTypeLong) {
        JFileChooser FC = new JFileChooser();
        FC.setDialogTitle(title);
        FC.setCurrentDirectory(new File(RSC.getDir(name)));
        FC.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FC.setDialogType(JFileChooser.SAVE_DIALOG);
        FC.setFileFilter(new FFilter(fileTypeShort, fileTypeLong));
        // cancelled?
        if (!(FC.showOpenDialog(MF) == JFileChooser.CANCEL_OPTION)) {
            RSC.rememberDir(name, FC.getSelectedFile().getParent());
            return FC.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
    
    public static void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }

    private static void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }    
    
    public void setLookAndFeel() {
        // Change Nimbus defaults
        UIManager.put("nimbusBase", new Color(239,240,241));
        UIManager.put("nimbusBlueGrey", new Color(239,240,241));
        UIManager.put("control", new Color(239,240,241));
        UIManager.put("nimbusFocus", new Color(115,164,209));
        UIManager.put("nimbusSelectionBackground", new Color(61,174,233));
        UIManager.put("nimbusSelection", new Color(61,174,233));
        UIManager.put("textBackground", new Color(61,174,233));
        UIManager.put("textHighlight", new Color(61,174,233));
        UIManager.put("nimbusOrange", new Color(61,174,233));
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Below: Adjust font sizes for menus etc.
        // For some reaons, the ofnt for menu change itself doesn't work, done manually in MainFrame.java
        UIDefaults defaults = UIManager.getDefaults();
        UIManager.put("MenuBar:Menu[Selected].backgroundPainter",new atlantis.gui.FillPainter(new Color(61,174,233)));
        UIManager.put("MenuBar:Menu[Enabled].textForeground",new Color(0,0,0));
        UIManager.put("ProgressBar[Disabled+Finished].foregroundPainter",new atlantis.gui.FillPainter(new Color(61,174,233)));
        UIManager.put("ProgressBar[Disabled+Indeterminate].foregroundPainter",new atlantis.gui.FillPainter(new Color(61,174,233)));
        UIManager.put("ProgressBar[Disabled].foregroundPainter",new atlantis.gui.FillPainter(new Color(61,174,233)));
        UIManager.put("ProgressBar[Enabled+Finished].foregroundPainter",new atlantis.gui.FillPainter(new Color(61,174,233)));
        UIManager.put("ProgressBar[Enabled+Indeterminate].foregroundPainter",new atlantis.gui.FillPainter(new Color(61,174,233)));
        UIManager.put("ProgressBar[Enabled].foregroundPainter",new atlantis.gui.FillPainter(new Color(61,174,233)));
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
        UIManager.getLookAndFeelDefaults().put("ToolTip.font", fnt11);
        UIManager.getLookAndFeelDefaults().put("Table.font", fnt12);
        UIManager.put("Table.rowHeight", guiScale(18));
        UIManager.getLookAndFeelDefaults().put("TableHeader.font", fnt12);
        UIManager.getLookAndFeelDefaults().put("TextField.font", fnt12);
        UIManager.getLookAndFeelDefaults().put("FormattedTextField.font", fnt12);
       
        UIManager.getLookAndFeelDefaults().put("TextArea.font", fnt12);
        UIManager.getLookAndFeelDefaults().put("InternalFrameTitlePane.font", fnt12);
        UIManager.getLookAndFeelDefaults().put("InternalFrame.font", fnt12);
        UIManager.getLookAndFeelDefaults().put("InternalFrame.titleFont", fnt12);
        UIManager.getLookAndFeelDefaults().put("TabbedPane.font", fnt12);
        
        UIManager.getLookAndFeelDefaults().put("Menu.margin", new javax.swing.plaf.InsetsUIResource(guiScale(2),guiScale(2),guiScale(2),guiScale(2)));
    }

    public void setDarkLookAndFeel() {
        // Change Nimbus defaults
        UIManager.put("nimbusBase", new Color(60,63,65));
        UIManager.put("nimbusBlueGrey", new Color(60,63,65));
        UIManager.put("control", new Color(43,43,43));
        UIManager.put("nimbusFocus", new Color(115,164,209));
        UIManager.put("nimbusSelectionBackground", new Color(75,110,175));
        UIManager.put("nimbusSelection", new Color(75,110,175));
        UIManager.put("textBackground", new Color(61,174,233));
        UIManager.put("textHighlight", new Color(61,174,233));
        UIManager.put("nimbusOrange", new Color(61,174,233));
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Below: Adjust font sizes for menus etc.
        // For some reaons, the ofnt for menu change itself doesn't work, done manually in MainFrame.java
        UIDefaults defaults = UIManager.getDefaults();
        UIManager.put("MenuBar:Menu[Selected].backgroundPainter",new atlantis.gui.FillPainter(new Color(61,174,233)));
        UIManager.put("MenuBar:Menu[Enabled].textForeground",new Color(0,0,0));
        UIManager.put("ProgressBar[Disabled+Finished].foregroundPainter",new atlantis.gui.FillPainter(new Color(61,174,233)));
        UIManager.put("ProgressBar[Disabled+Indeterminate].foregroundPainter",new atlantis.gui.FillPainter(new Color(61,174,233)));
        UIManager.put("ProgressBar[Disabled].foregroundPainter",new atlantis.gui.FillPainter(new Color(61,174,233)));
        UIManager.put("ProgressBar[Enabled+Finished].foregroundPainter",new atlantis.gui.FillPainter(new Color(61,174,233)));
        UIManager.put("ProgressBar[Enabled+Indeterminate].foregroundPainter",new atlantis.gui.FillPainter(new Color(61,174,233)));
        UIManager.put("ProgressBar[Enabled].foregroundPainter",new atlantis.gui.FillPainter(new Color(61,174,233)));
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
        UIManager.getLookAndFeelDefaults().put("ToolTip.font", fnt11);
        UIManager.getLookAndFeelDefaults().put("Table.font", fnt12);
        UIManager.put("Table.rowHeight", guiScale(18));
        UIManager.getLookAndFeelDefaults().put("TableHeader.font", fnt12);
        UIManager.getLookAndFeelDefaults().put("TextField.font", fnt12);
        UIManager.getLookAndFeelDefaults().put("FormattedTextField.font", fnt12);
       
        UIManager.getLookAndFeelDefaults().put("TextArea.font", fnt12);
        UIManager.getLookAndFeelDefaults().put("InternalFrameTitlePane.font", fnt12);
        UIManager.getLookAndFeelDefaults().put("InternalFrame.font", fnt12);
        UIManager.getLookAndFeelDefaults().put("InternalFrame.titleFont", fnt12);
        UIManager.getLookAndFeelDefaults().put("TabbedPane.font", fnt12);
        
        UIManager.getLookAndFeelDefaults().put("Menu.margin", new javax.swing.plaf.InsetsUIResource(guiScale(2),guiScale(2),guiScale(2),guiScale(2)));
    }
    
            
}
