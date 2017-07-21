package gui;

import java.io.File;
import javax.swing.filechooser.*;

/* XMLFilter.java is used for loading and saving CSM. */
public class XMLFilter extends FileFilter {

	// Accept all directories and XML files.
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		String extension = getExtension(f);
		if (extension != null) {
			if (extension.equals("xml"))
				return true;
			else
				return false;
		}
		return false;
	}

	// The description of this filter
	public String getDescription() {
		return "XML Files";
	}

	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}
}
