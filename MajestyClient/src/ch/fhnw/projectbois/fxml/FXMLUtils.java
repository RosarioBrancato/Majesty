package ch.fhnw.projectbois.fxml;

import java.io.IOException;
import java.net.URL;

import ch.fhnw.projectbois.components.Component;
import ch.fhnw.projectbois.components.ComponentLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class FXMLUtils {

	public static Parent loadFXML(Component component) {
		Parent parent = null;

		try {
			parent = loadFXML(ComponentLoader.getResource(component));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return parent;
	}

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
