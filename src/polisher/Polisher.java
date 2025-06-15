package polisher;

import atlantis.latex.LaTeXDocument;
import atlantis.tools.TextFile;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author cnsaeman
 */
public class Polisher {
    
    public void testParser() {
        LaTeXDocument document = LaTeXDocument.fromFile("doc2.tex");
        String parseResult = document.toString();
        System.out.println(parseResult);
        TextFile.writeStringToFile(parseResult, "parseResult.txt");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final Resources RSC=new Resources();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                RSC.close();
            }
        });
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run()  {
                (new MainFrame(RSC,"1.0")).setVisible(true);
                /*splash.toFront();
                splash.setTimeTrigger();
                if (RSC.database.ex!=null) {
                    StringWriter sw = new StringWriter();
                    RSC.database.ex.printStackTrace(new PrintWriter(sw));
                    int i=RSC.guiTools.askQuestionOC("Error initialising database:", "Continue or cancel?\nDetailed message:\n"+sw.toString().substring(0,100));
                    if (i==1) {
                        System.exit(100);
                    }
                }*/
            }
        });
    }
    
}
