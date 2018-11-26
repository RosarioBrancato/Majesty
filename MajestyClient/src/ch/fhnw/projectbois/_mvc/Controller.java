package ch.fhnw.projectbois._mvc;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.fhnw.projectbois.log.LoggerFactory;
import ch.fhnw.projectbois.translate.Translator;
import javafx.fxml.FXML;
import javafx.scene.Parent;

/**
 * Based on example from course Java 2
 * 
 * @author Rosario Brancato
 */
public abstract class Controller<M extends Model, V extends View<M>> {
	protected Logger logger;
	protected Translator translator;
	
	protected M model;
	protected V view;

	public Controller(M model, V view) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.translator = Translator.getTranslator();
		
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

		Logger logger = LoggerFactory.getLogger(Controller.class);
		C controller = null;

		try {
			M model = modelClass.newInstance();
			V view = viewClass.getDeclaredConstructor(modelClass).newInstance(model);

			controller = controllerClass.getDeclaredConstructor(modelClass, viewClass).newInstance(model, view);
			
			view.loadRoot(controller);

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {

			logger.log(Level.SEVERE, "Controller.initMVC()", e);
		}

		return controller;
	}

	public Parent getViewRoot() {
		return this.view.getRoot();
	}

	@FXML
	protected void initialize() {
	}
}
