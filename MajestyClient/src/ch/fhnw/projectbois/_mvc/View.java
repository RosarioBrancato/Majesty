package ch.fhnw.projectbois._mvc;

import java.net.URL;

import ch.fhnw.projectbois.fxml.FXMLUtils;
import javafx.scene.Parent;

/**
 * Based on example from course Java 2 
 */
public abstract class View<M extends Model> {
	protected Parent root;
	protected M model;

	/**
	 * Set any options for the stage in the subclass constructor
	 * 
	 * @param stage
	 * @param model
	 */
	protected View(M model) {
		this.model = model;
		this.root = createRoot();
	}

	protected Parent createRoot() {
		// FXML
		URL url = getFXML();
		Parent root = FXMLUtils.loadFXML(url);

		return root;
	}

	protected abstract URL getFXML();

	public Parent getRoot() {
		return this.root;
	}

}
