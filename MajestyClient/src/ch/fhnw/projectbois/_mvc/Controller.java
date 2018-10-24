package ch.fhnw.projectbois._mvc;

import javafx.fxml.FXML;
import javafx.scene.Parent;

/**
 * Based on example from course Java 2 
 */
public abstract class Controller<M extends Model, V extends View<M>> {
	protected M model;
	protected V view;

	/**
	 * Empty constructor for the FXML loader
	 */
	protected Controller() {
	}

	protected Controller(M model, V view) {
		this.model = model;
		this.view = view;
	}

	public Parent getViewRoot() {
		return this.view.getRoot();
	}

	@FXML
	private void initialize() {
	}
}
