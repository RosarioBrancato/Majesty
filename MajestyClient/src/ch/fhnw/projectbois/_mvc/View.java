package ch.fhnw.projectbois._mvc;

import java.net.URL;

import ch.fhnw.projectbois.fxml.FXMLUtils;
import javafx.scene.Parent;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
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
