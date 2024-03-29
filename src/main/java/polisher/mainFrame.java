/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polisher;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author cnsaeman
 */
public class mainFrame extends javax.swing.JFrame {
    
    public Resources RSC;
    
    public int pcount;
    public int[] starts;
    public int[] ends;
    public String[] messages;
    public int[] severities;
    
    public ArrayList<Problem> problems;

    /**
     * Creates new form mainFrame
     */
    public mainFrame() {
        RSC=new Resources(this);
        initComponents();
    }
    
    public void updateHighlighting() {
        String contents=jTPSource.getText();

        // structural elements bold
        String regx = "(\\\\section\\{.+\\n|\\\\subsection\\{.+\\n|\\\\tableofcontents|\\\\apendices|\\\\begin\\{document\\}|\\\\end\\{document\\}|\\\\bibliography\\{[^\\}]+\\})|\\\\bibliographystyle\\{[^\\}]+\\}}";
        Pattern p = Pattern.compile(regx);
        Matcher m = p.matcher(contents);
        while(m.find())
            jTPSource.getStyledDocument().setCharacterAttributes(m.start(),(m.end() - m.start()),RSC.sasBold, false);
        
        // header grey
        int i=jTPSource.getText().indexOf("\\begin{document}");
        if (i>0) {
            jTPSource.getStyledDocument().setCharacterAttributes(0,i,RSC.sasGray, false);
        }

        // header grey
        int startBibPos=jTPSource.getText().indexOf("@!@Bibliography@!@");
        int length=jTPSource.getText().length();
        if (startBibPos>0) {
            jTPSource.getStyledDocument().setCharacterAttributes(startBibPos,length,RSC.sasGreen, false);
        }
        
        // references green
        regx = "(\\\\cite\\{[^\\}]+\\}|\\\\ref\\{[^\\}]+\\}|\\\\eqref\\{[^\\}]+\\})";
        p = Pattern.compile(regx);
        m = p.matcher(contents);
        while(m.find())
            jTPSource.getStyledDocument().setCharacterAttributes(m.start(),(m.end() - m.start()),RSC.sasGreen, false);
        
        // formulas blue
        regx = "(\\\\begin\\{equation\\}.+?\\\\end\\{equation\\}|\\\\begin\\{eqnarray\\}.+?\\\\end\\{eqnarray\\})";
        p = Pattern.compile(regx,Pattern.DOTALL);
        m = p.matcher(contents);
        while(m.find())
            jTPSource.getStyledDocument().setCharacterAttributes(m.start(),(m.end() - m.start()),RSC.sasBlue, false);
        regx = "\\$[^\\$]+\\$";
        p = Pattern.compile(regx);
        m = p.matcher(contents);
        while(m.find())
            jTPSource.getStyledDocument().setCharacterAttributes(m.start(),(m.end() - m.start()),RSC.sasBlue, false);
    }
    
    public void markProblems() {
        for (int i=0;i<pcount;i++) {
            switch(severities[i]) {
                case 1: jTPSource.getStyledDocument().setCharacterAttributes(starts[i],(ends[i]- starts[i]),RSC.sasPink, false); break;
                case 50: jTPSource.getStyledDocument().setCharacterAttributes(starts[i],(ends[i]- starts[i]),RSC.sasOrange, false); break;
                case 100: jTPSource.getStyledDocument().setCharacterAttributes(starts[i],(ends[i]- starts[i]),RSC.sasRed, false); break;
            }
        }
    }
    
    public void findProblems() {
        problems=RSC.findProblems(jTPSource.getText(),RSC.globalRules);
        int size=problems.size();
        starts=new int[size+1];
        ends=new int[size];
        messages=new String[size];
        severities=new int[size];
        pcount=size;
        for (int i=0;i<pcount;i++) {
            starts[i]=problems.get(i).start;
            ends[i]=problems.get(i).end;
            messages[i]=problems.get(i).message;
            severities[i]=problems.get(i).severity;
        }
        starts[pcount]=jTPSource.getText().length();
        jTFProblems.setText("Problems found: "+Integer.toString(pcount));
        markProblems();
    }
    
    public void clearMarking() {
        
    }
    
    public void loadText(String contents) {
        pcount=0;
        starts=new int[1];
        ends=new int[1];
        messages=new String[1];
        severities=new int[1];
        jTPSource.setText("");
        jTPSource.setText(contents);
        updateHighlighting();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu4 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        jTFCaret = new javax.swing.JTextField();
        jTFProblems = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTPSource = new javax.swing.JTextPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTPMessages = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMFile = new javax.swing.JMenu();
        jMIOpen = new javax.swing.JMenuItem();
        jMIReload = new javax.swing.JMenuItem();
        jMISave = new javax.swing.JMenuItem();
        jMEdit = new javax.swing.JMenu();
        jMIPreviousProblem = new javax.swing.JMenuItem();
        jMINextProblem = new javax.swing.JMenuItem();
        jMTools = new javax.swing.JMenu();
        jMTester = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMReloadRules = new javax.swing.JMenuItem();
        jMHelp = new javax.swing.JMenu();
        jMIAbout = new javax.swing.JMenuItem();

        jMenu4.setText("jMenu4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 50));
        jPanel1.setPreferredSize(new java.awt.Dimension(1173, 50));

        jTFCaret.setText("Row 0, Column 0");
        jTFCaret.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFCaretActionPerformed(evt);
            }
        });

        jTFProblems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFProblemsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTFCaret, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFProblems, javax.swing.GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTFCaret, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(jTFProblems))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTPSource.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTPSourceCaretUpdate(evt);
            }
        });
        jScrollPane3.setViewportView(jTPSource);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1198, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1198, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 608, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setPreferredSize(new java.awt.Dimension(1198, 100));

        jTPMessages.setEditable(false);
        jScrollPane2.setViewportView(jTPMessages);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1198, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1198, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, java.awt.BorderLayout.NORTH);

        jMFile.setText("File");

        jMIOpen.setText("Open");
        jMIOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIOpenActionPerformed(evt);
            }
        });
        jMFile.add(jMIOpen);

        jMIReload.setText("Reload ");
        jMIReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIReloadActionPerformed(evt);
            }
        });
        jMFile.add(jMIReload);

        jMISave.setText("Save");
        jMISave.setEnabled(false);
        jMISave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMISaveActionPerformed(evt);
            }
        });
        jMFile.add(jMISave);

        jMenuBar1.add(jMFile);

        jMEdit.setText("Edit");

        jMIPreviousProblem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, java.awt.event.InputEvent.CTRL_MASK));
        jMIPreviousProblem.setText("Go to Previous Problem");
        jMIPreviousProblem.setEnabled(false);
        jMIPreviousProblem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIPreviousProblemActionPerformed(evt);
            }
        });
        jMEdit.add(jMIPreviousProblem);

        jMINextProblem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, java.awt.event.InputEvent.CTRL_MASK));
        jMINextProblem.setText("Go to Next Problem");
        jMINextProblem.setEnabled(false);
        jMINextProblem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMINextProblemActionPerformed(evt);
            }
        });
        jMEdit.add(jMINextProblem);

        jMenuBar1.add(jMEdit);

        jMTools.setText("Tools");

        jMTester.setText("Test a regular expression");
        jMTester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMTesterActionPerformed(evt);
            }
        });
        jMTools.add(jMTester);
        jMTools.add(jSeparator1);

        jMReloadRules.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK));
        jMReloadRules.setText("Reload Rules");
        jMReloadRules.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMReloadRulesActionPerformed(evt);
            }
        });
        jMTools.add(jMReloadRules);

        jMenuBar1.add(jMTools);

        jMHelp.setText("Help");

        jMIAbout.setText("About");
        jMIAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIAboutActionPerformed(evt);
            }
        });
        jMHelp.add(jMIAbout);

        jMenuBar1.add(jMHelp);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMIAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIAboutActionPerformed
        RSC.Information(this, RSC.aboutText, "About Polisher "+RSC.VersionNumber);// TODO add your handling code here:
    }//GEN-LAST:event_jMIAboutActionPerformed

    private void jTPSourceCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTPSourceCaretUpdate
        int pos = jTPSource.getCaretPosition();
        
        if (pcount>0) {
            if (pos<ends[0]) jMIPreviousProblem.setEnabled(false); else jMIPreviousProblem.setEnabled(true);
            if (pos<starts[pcount-1]) jMINextProblem.setEnabled(true); else jMINextProblem.setEnabled(false);
        }
        // update row/column information
        Element map = jTPSource.getDocument().getDefaultRootElement();
        int row = map.getElementIndex(pos);
        Element lineElem = map.getElement(row);
        int col = pos - lineElem.getStartOffset();
        jTFCaret.setText("Row: "+Integer.toString(row)+", Column: "+Integer.toString(col));
        // update problem message
        if (pcount>0) {
            int p=0;
            while ((pos>=starts[p]) && (p<pcount+1)) p++;
            p=p-1;
            if ((p>-1) && (ends[p]>pos)) {
                jTPMessages.setText("Current Problem : "+Integer.toString(p+1)+"\n"+messages[p]);
                jTFProblems.setText("Caret over problem "+Integer.toString(p+1)+" of "+Integer.toString(pcount)+" found problems.");
            } else {
                jTPMessages.setText("");
                jTFProblems.setText("Problems found: "+Integer.toString(pcount));
            }
        } else {
            if (jTPMessages.getText()!="") {
                jTPMessages.setText("");
                jTFProblems.setText("Problems found: "+Integer.toString(pcount));
            }
        }
    }//GEN-LAST:event_jTPSourceCaretUpdate

    private void jTFCaretActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFCaretActionPerformed
    }//GEN-LAST:event_jTFCaretActionPerformed

    private void jTFProblemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFProblemsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFProblemsActionPerformed

    private void jMIPreviousProblemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIPreviousProblemActionPerformed
        int pos = jTPSource.getCaretPosition();
        int p=pcount-1;
        while ((ends[p]>pos) && (p>0)) p--;
        jTPSource.setCaretPosition(starts[p]);
    }//GEN-LAST:event_jMIPreviousProblemActionPerformed

    private void jMINextProblemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMINextProblemActionPerformed
        int pos = jTPSource.getCaretPosition();
        int p=0;
        while ((starts[p]<=pos) && (p<pcount+1)) p++;
        jTPSource.setCaretPosition(starts[p]);
    }//GEN-LAST:event_jMINextProblemActionPerformed

    private void jMReloadRulesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMReloadRulesActionPerformed
        RSC.readRules();
    }//GEN-LAST:event_jMReloadRulesActionPerformed

    private void jMIOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIOpenActionPerformed
        JFileChooser FC = new JFileChooser();
        FC.setDialogTitle("Select main tex-file");
        FC.setDialogType(JFileChooser.OPEN_DIALOG);
        FC.setFileFilter(new FFilter("tex", "TeX-files"));
        RSC.setComponentFont(FC.getComponents());
        if (!(FC.showOpenDialog(this) == JFileChooser.CANCEL_OPTION)) {
            File file = FC.getSelectedFile();
            RSC.currentFN=file.getAbsolutePath();
            jTPSource.setDocument(new DefaultStyledDocument());
            jTPSource.setText("");
            RSC.openFile(RSC.currentFN);
            findProblems();
            updateHighlighting();
            jMISave.setEnabled(true);
        }
    }//GEN-LAST:event_jMIOpenActionPerformed

    private void jMISaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMISaveActionPerformed
        Path path1 = Paths.get(RSC.currentFN);
        Path path2 = Paths.get(RSC.currentFN+".bak");
        try {
            int i=jTPSource.getText().indexOf("@!@Bibliography@!@");
            Files.move(path1,path2);
            Files.write(path1, jTPSource.getText().substring(0,i).getBytes());
        } catch (IOException ex) {
            Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMISaveActionPerformed

    private void jMTesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMTesterActionPerformed
        RegExpTester RET=new RegExpTester(RSC);
        RSC.centerFrame(RET);
        RET.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_jMTesterActionPerformed

    private void jMIReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIReloadActionPerformed
        jTPSource.setDocument(new DefaultStyledDocument());
        jTPSource.setText("");
        RSC.openFile(RSC.currentFN);
        findProblems();
        updateHighlighting();
        jMISave.setEnabled(true);
    }//GEN-LAST:event_jMIReloadActionPerformed

    public void gui2() {
        this.setTitle("Polisher "+RSC.VersionNumber);
        jTPSource.setFont(new java.awt.Font("Monospaced", 0, 24));
        pack();
        setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMEdit;
    private javax.swing.JMenu jMFile;
    private javax.swing.JMenu jMHelp;
    private javax.swing.JMenuItem jMIAbout;
    private javax.swing.JMenuItem jMINextProblem;
    private javax.swing.JMenuItem jMIOpen;
    private javax.swing.JMenuItem jMIPreviousProblem;
    private javax.swing.JMenuItem jMIReload;
    private javax.swing.JMenuItem jMISave;
    private javax.swing.JMenuItem jMReloadRules;
    private javax.swing.JMenuItem jMTester;
    private javax.swing.JMenu jMTools;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTextField jTFCaret;
    private javax.swing.JTextField jTFProblems;
    private javax.swing.JTextPane jTPMessages;
    public javax.swing.JTextPane jTPSource;
    // End of variables declaration//GEN-END:variables
}
