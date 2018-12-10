package ch.fhnw.projectbois._mvc;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois.dto.ReportDTO;
import ch.fhnw.projectbois.interfaces.IDialog;
import ch.fhnw.projectbois.log.LoggerFactory;
import ch.fhnw.projectbois.translate.Translator;
import ch.fhnw.projectbois.utils.DialogUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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

	private ChangeListener<ReportDTO> reportPropertyListener = null;

	public Controller(M model, V view) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.translator = Translator.getTranslator();

		this.model = model;
		this.view = view;
	}

	/**
	 * Calls the default constructors of the MVC-Classes and returns the instance of
	 * the Controller subclass.
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

			MetaContainer.getInstance().addController(controller);

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {

			logger.log(Level.SEVERE, "Controller.initMVC()", e);
		}

		return controller;
	}

	public static <C extends Controller<M, V>, V extends View<M>, M extends Model> C initMVCAsRoot(
			Class<C> controllerClass, Class<M> modelClass, Class<V> viewClass) {

		MetaContainer.getInstance().destroyControllers();

		C controller = initMVC(controllerClass, modelClass, viewClass);

		MetaContainer.getInstance().setRoot(controller.getViewRoot());

		return controller;
	}

	public static <C extends Controller<M, V>, V extends View<M>, M extends Model> C initMVCAsDlg(
			Class<C> controllerClass, Class<M> modelClass, Class<V> viewClass) {

		C controller = initMVC(controllerClass, modelClass, viewClass);

		if (controller instanceof IDialog) {
			((IDialog) controller).showAndWait();

			MetaContainer.getInstance().destroyController(controller);
		}

		return controller;
	}

	public Parent getViewRoot() {
		return this.view.getRoot();
	}

	public void destroy() {
		if (this.reportPropertyListener != null) {
			this.model.getReportProperty().removeListener(this.reportPropertyListener);
		}

		this.view.destroy();
		this.model.destroy();
	}

	@FXML
	protected void initialize() {

		this.reportPropertyListener = (observer, oldValue, newValue) -> {
			handleReport(newValue);
		};

		// listen for reports
		model.getReportProperty().addListener(this.reportPropertyListener);
	}

	protected void handleReport(ReportDTO report) {
		Platform.runLater(() -> {
			// AlertType
			AlertType type = AlertType.NONE;
			switch (report.getSeverity()) {
			case INFO:
				type = AlertType.INFORMATION;
				break;
			case WARNING:
				type = AlertType.WARNING;
				break;
			case ERROR:
				type = AlertType.ERROR;
				break;
			}

			// Context
			String translationKey = report.getTranslationKey();
			String message = translator.getTranslation(translationKey);

			// Show Alert
			if (message.length() > 0) {
				Alert alert = DialogUtils.getAlert(MetaContainer.getInstance().getMainStage(), type, message);
				alert.showAndWait();
			}
		});
	}
}
