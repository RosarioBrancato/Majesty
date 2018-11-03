package ch.fhnw.projectbois._mvc;

import java.lang.reflect.InvocationTargetException;

import javafx.fxml.FXML;
import javafx.scene.Parent;

/**
 * Based on example from course Java 2
 * 
 * @author Rosario Brancato
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

	/**
	 * Calls the default constructors of the MVC-Classes and returns the instance of the Controller subclass.
	 * 
	 * @param controllerClass
	 * @param modelClass
	 * @param viewClass
	 * @return instance of the Controller subclass
	 */
	public static <C extends Controller<M, V>, V extends View<M>, M extends Model> C initMVC(Class<C> controllerClass,
			Class<M> modelClass, Class<V> viewClass) {

		C controller = null;

		try {
			M model = modelClass.newInstance();
			V view = viewClass.getDeclaredConstructor(modelClass).newInstance(model);

			controller = controllerClass.getDeclaredConstructor(modelClass, viewClass).newInstance(model, view);

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {

			e.printStackTrace();
		}

		return controller;
	}

	public Parent getViewRoot() {
		return this.view.getRoot();
	}

	@FXML
	private void initialize() {
	}
}
