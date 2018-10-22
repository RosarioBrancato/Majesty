package ch.fhnw.projectbois._mvc;

import java.io.IOException;
import java.net.URL;

import ch.fhnw.projectbois._application.MetaContainer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public abstract class View<M extends Model> {
	protected Scene scene;
	protected M model;

	/**
	 * Set any options for the stage in the subclass constructor
	 * 
	 * @param stage
	 * @param model
	 */
	protected View(M model) {
		this.model = model;
		this.scene = createScene();

		MetaContainer.getInstance().getMainStage().setScene(this.scene);
	}

	protected Scene createScene() {
		Parent root = null;

		// FXML
		try {
			URL fxml = getFXML();
			if (fxml != null) {
				root = FXMLLoader.load(fxml);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene scene = new Scene(root);

		// CSS
		String css = getCSS();
		if (css != null) {
			scene.getStylesheets().add(css);
		}

		return scene;
	}

	protected abstract URL getFXML();

	protected abstract String getCSS();

	public Scene getScene() {
		return this.scene;
	}

}
