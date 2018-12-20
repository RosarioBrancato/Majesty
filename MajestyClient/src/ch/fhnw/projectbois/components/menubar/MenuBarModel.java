package ch.fhnw.projectbois.components.menubar;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;

import ch.fhnw.projectbois._mvc.Model;

/**
 * The Class MenuBarModel.
 * 
 * @author Dario Stoeckli
 */
public class MenuBarModel extends Model {

	/**
	 * Open manual.
	 */
	public void openManual() {
		if (Desktop.isDesktopSupported()) {
			File manual;
			// Open Manual in corresponding language, if Manual in language not available
			// open in English
			try {
				if (translator.getLocale().equals(Locale.GERMAN)) {
					manual = new File("files/manuals/majesty_ger_rules_lowres.pdf");
				} else {
					manual = new File("files/manuals/majesty_eng_rules_lowres.pdf");
				}
				Desktop.getDesktop().open(manual);

			} catch (IOException ex) {
				logger.log(Level.SEVERE, "MenuBarModel.openManual()", ex);
			}
		}

	}
}
