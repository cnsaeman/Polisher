/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polisher;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author cnsaeman
 */
public class FFilter extends FileFilter {
	
	public String extension;
	public String description;
	
	public FFilter(String ext, String desc)	{
		extension=ext;
		description=desc;
	}

	public boolean accept(File arg0) {
		if (arg0.isDirectory()) return(true);
		if (extension.equals("_DIR")) return(false);
		if (extension.equals("_ALL")) return(true);
		try
		{
                    return((arg0.getCanonicalPath()).endsWith(extension));
		} catch(Exception e) { }
		return(false);
	}
	
	public String getDescription() {
		return(description);
	}

}
