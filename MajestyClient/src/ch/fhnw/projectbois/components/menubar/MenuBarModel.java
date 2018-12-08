package ch.fhnw.projectbois.components.menubar;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import ch.fhnw.projectbois._mvc.Model;

public class MenuBarModel extends Model {

	public void openManual() {
		if (Desktop.isDesktopSupported()) {
			File manual;
			//Open Manual in corresponding language, if Manual in language not available open in English
		    try {
		    	if (translator.getLocale().equals(Locale.GERMAN)) {
		    		manual = new File("resources/manuals/majesty_ger_rules_lowres.pdf");
		    	} else {
		    		manual = new File("resources/manuals/majesty_eng_rules_lowres.pdf");
		    	}
		    	Desktop.getDesktop().open(manual);
		    } catch (IOException ex) {
		        //No PDF application available to open file with, thus no action
		    }
		}

	}
}
