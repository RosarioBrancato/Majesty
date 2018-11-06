package ch.fhnw.projectbois._mvc;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Based on example from course Java 2
 * 
 * @author Rosario Brancato
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
	}

	public Parent getRoot() {
		return this.root;
	}

	protected abstract URL getFXML();

	@SuppressWarnings("rawtypes")
	public <T extends Controller> void loadRoot(T controller) {
		URL url = this.getFXML();
		FXMLLoader loader = new FXMLLoader(url);
		loader.setController(controller);

		try {
			this.root = loader.load();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
