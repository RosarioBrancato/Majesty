package ch.fhnw.projectbois._mvc;

import javafx.fxml.FXML;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
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

	@FXML
	private void initialize() {
	}
}
