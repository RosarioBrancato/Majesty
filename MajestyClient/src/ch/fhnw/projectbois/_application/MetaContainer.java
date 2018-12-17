package ch.fhnw.projectbois._application;

import java.util.ArrayList;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois._mvc.View;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Based on example from course Java 2.
 *
 * @author Rosario Brancato
 */
public class MetaContainer {

	private static MetaContainer instance = null;

	private Stage mainStage = null;
	private ArrayList<Controller<? extends Model, ? extends View<? extends Model>>> controllers = null;

	/**
	 * Instantiates a new meta container.
	 */
	private MetaContainer() {
		this.controllers = new ArrayList<>();
	}

	/**
	 * Gets the single instance of MetaContainer.
	 *
	 * @return single instance of MetaContainer
	 */
	public static MetaContainer getInstance() {
		if (instance == null) {
			instance = new MetaContainer();
		}
		return instance;
	}

	/**
	 * Sets the main stage.
	 *
	 * @param stage the new main stage
	 */
	public void setMainStage(Stage stage) {
		this.mainStage = stage;
	}

	/**
	 * Gets the main stage.
	 *
	 * @return the main stage
	 */
	public Stage getMainStage() {
		return this.mainStage;
	}

	/**
	 * Sets the root.
	 *
	 * @param root the new root
	 */
	public void setRoot(Parent root) {
		this.mainStage.getScene().setRoot(root);
		root.setId("pane");
	}

	/**
	 * Gets the scene.
	 *
	 * @return the scene
	 */
	public Scene getScene() {
		return this.mainStage.getScene();
	}

	/**
	 * Adds the controller.
	 *
	 * @param controller the controller
	 */
	public void addController(Controller<? extends Model, ? extends View<? extends Model>> controller) {
		this.controllers.add(controller);
	}

	/**
	 * Destroy controller.
	 *
	 * @param controller the controller
	 */
	public void destroyController(Controller<? extends Model, ? extends View<? extends Model>> controller) {
		if (this.controllers.contains(controller)) {
			controller.destroy();
			this.controllers.remove(controller);
		}
	}

	/**
	 * Destroy controllers.
	 */
	public void destroyControllers() {
		for (Controller<? extends Model, ? extends View<? extends Model>> c : this.controllers) {
			c.destroy();
		}

		this.controllers.clear();
	}

}
