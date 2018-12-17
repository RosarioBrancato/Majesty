package ch.fhnw.projectbois._utils;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * The Class FXMLUtils.
 * 
 * @author Rosario Brancato
 */
public class FXMLUtils {

	/**
	 * Load FXML.
	 *
	 * @param url the url
	 * @return the parent
	 */
	public static Parent loadFXML(URL url) {
		Parent root = null;

		try {
			if (url != null) {
				root = FXMLLoader.load(url);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return root;
	}

}
