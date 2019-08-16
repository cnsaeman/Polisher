/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polisher;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import static java.awt.SystemColor.info;
import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author cnsaeman
 */
public class Resources {
    
    public final String VersionNumber = "v0.2";
    public final String aboutText = "Polisher is an open source tool which helps with polishing LaTeX drafts of papers.";
    public final mainFrame MF;
    public String currentFN;
    public HashMap<Integer,ArrayList<Rule>> globalRules;
    public ArrayList<Problem> problems;
    
    public final Color Green = new Color(58,153,0);
    public final Color Blue = new Color(64,135,213);
    public final Color Pink = new Color(255,107,172);
    public final Color Orange = new Color(255,153,0);
    public final Color Red = new Color(255,19,11);

    public final SimpleAttributeSet sasPink = new SimpleAttributeSet();
    public final SimpleAttributeSet sasOrange = new SimpleAttributeSet();
    public final SimpleAttributeSet sasRed = new SimpleAttributeSet();
    public final SimpleAttributeSet sasBold = new SimpleAttributeSet(); 
    public final SimpleAttributeSet sasGray = new SimpleAttributeSet(); 
    public final SimpleAttributeSet sasGreen = new SimpleAttributeSet(); 
    public final SimpleAttributeSet sasBlue = new SimpleAttributeSet(); 

    public Resources(mainFrame mf) {
        MF=mf;
        MF.RSC=this;
        StyleConstants.setForeground(sasPink, Pink);
        StyleConstants.setForeground(sasOrange, Orange);
        StyleConstants.setForeground(sasRed, Red);
        StyleConstants.setForeground(sasGreen, Green);
        StyleConstants.setForeground(sasBlue, Blue);
        StyleConstants.setBold(sasBold, true);
        StyleConstants.setForeground(sasGray, Color.GRAY);    
        readRules();
    }
    
    public void setComponentFont(Component[] comp)
    {
        for(int x = 0; x < comp.length; x++)
        {
          if(comp[x] instanceof Container) setComponentFont(((Container)comp[x]).getComponents());
          try{comp[x].setFont(new java.awt.Font("Arial", 0, 22));}
          catch(Exception e){}//do nothing
        }
    }    
    
    public void centerFrame(JDialog frame) {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle r=environment.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(r.width/2 - (frame.getWidth()/2),r.height/2 - (frame.getHeight()/2));
    }
    
    
    public void openFile(String s) {
        MF.pcount=0;
        try {
            // Read in TeX-file
            String contents = new String(Files.readAllBytes(Paths.get(s)));
            File bblFile = new File(s.replace(".tex",".bbl"));
            // Read in bbl-file
            if (bblFile.exists()) {
                contents+="\n\n@!@Bibliography@!@\n\n"+new String(Files.readAllBytes(Paths.get(s.replace(".tex",".bbl"))));
            }
            MF.loadText(contents);
        } catch (IOException ex) {
            Logger.getLogger(Resources.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void readRules() {
        globalRules=new HashMap<Integer,ArrayList<Rule>>();
        try {
            String config = new String(Files.readAllBytes(Paths.get("rules.cfg")));
            String[] lines=config.split("\\n");
            for (int i=0;i<lines.length;i++) {
                // is a comment?
                if (!lines[i].startsWith("#")) {
                    // not a comment
                    if (lines[i].startsWith("@!")) {
                        String[] ls=lines[i+3].trim().split(" ");
                        Rule rule=new Rule(lines[i].substring(2),lines[i+1].trim(),lines[i+2].trim(),Integer.valueOf(ls[0]),Integer.valueOf(ls[1]));
                        if (!globalRules.containsKey(rule.level)) globalRules.put(rule.level,(new ArrayList<Rule>()));
                        globalRules.get(rule.level).add(rule);
                        System.out.println("Found rule:\n"+rule.toString());
                        i=i+3;
                    } else if (lines[i].startsWith("@")) {
                        String[] ls=lines[i+2].trim().split(" ");
                        Rule rule=new Rule(lines[i].substring(1),lines[i+1].trim(),"",Integer.valueOf(ls[0]),Integer.valueOf(ls[1]));
                        if (!globalRules.containsKey(rule.level)) globalRules.put(rule.level,(new ArrayList<Rule>()));
                        globalRules.get(rule.level).add(rule);
                        System.out.println("Found rule:\n"+rule.toString());
                        i=i+2;
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Resources.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Information(java.awt.Component p, String msg,String head) {
        JOptionPane.showMessageDialog(p,msg,head, JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    public ArrayList<Problem> findProblems(String base, HashMap<Integer, ArrayList<Rule>> rules) {
        ArrayList<Problem> problems=new ArrayList<Problem>();
        int startBibPos=base.indexOf("@!@Bibliography@!@");
        int startDoc=base.indexOf("\\begin{document}");
        if ((startDoc>0) && (startBibPos<0)) Information(this.MF,"No Bibliography found","Attention!");
        
        rules.forEach((key,subRules) -> {
            String source=base;
            // adjust according to level
            if (key>0) {
                if (startDoc>0) source=new String(new char[startDoc])+source.substring(startDoc);
            } 
            if (key==3) {
                Matcher m = Pattern.compile("\\$[^\\$]+\\$").matcher(source);
                while(m.find())
                    source=source.substring(0,m.start())+stringRepeat('@',m.end()-m.start())+source.substring(m.end());
                m = Pattern.compile("(\\\\begin\\{equation\\}.+?\\\\end\\{equation\\}|\\\\begin\\{eqnarray\\}.+?\\\\end\\{eqnarray\\})",Pattern.DOTALL).matcher(source);
                while(m.find())
                    source=source.substring(0,m.start())+stringRepeat('@',m.end()-m.start())+source.substring(m.end());
                System.out.println(source);
            } else if (key==90) {
                if (startBibPos>0) {
                    source=new String(new char[startBibPos])+source.substring(startBibPos);
                } else {
                    source=new String("");
                }
            }
            for (Rule rule : subRules) {
                System.out.println("Applying rule: "+rule.toString());
                Matcher m = rule.pattern.matcher(source);
                while(m.find()) {
                    boolean block=false;
                    if (rule.exception!=null) {
                        System.out.println("Trying: "+m.group());
                        Matcher m2=rule.exception.matcher(m.group());
                        if (m2.find()) {
                            System.out.println("Found: "+m2.group());
                            block=true;
                        }
                    }
                    if (!block) {
                        System.out.println("Problem found: "+rule.toString()+" Source: "+m.toString());
                        problems.add(new Problem(m.start(),m.end(),rule.message,rule.severity));
                    }
                }                
            }
        });
        Collections.sort(problems,new Comparator<Problem>() {
            @Override
            public int compare(Problem lhs, Problem rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.start < rhs.start ? -1 : (lhs.start > rhs.start) ? 1 : 0;
            }
        });
        return(problems);
    }

    private String stringRepeat(char c, int i) {
        return CharBuffer.allocate( i ).toString().replace( '\0', c );
    }

    void highlightProblems(ArrayList<Problem> problems, JTextPane jTPSource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
