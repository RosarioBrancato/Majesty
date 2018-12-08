package ch.fhnw.projectbois._application;

import java.util.ArrayList;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois._mvc.View;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Based on example from course Java 2
 * 
 * @author Rosario Brancato
 */
public class MetaContainer {

	private static MetaContainer instance = null;

	private Stage mainStage = null;

	private ArrayList<Controller<? extends Model, ? extends View<? extends Model>>> controllers = null;

	private MetaContainer() {
		this.controllers = new ArrayList<>();
	}

	public static MetaContainer getInstance() {
		if (instance == null) {
			instance = new MetaContainer();
		}
		return instance;
	}

	public void setMainStage(Stage stage) {
		this.mainStage = stage;
	}

	public Stage getMainStage() {
		return this.mainStage;
	}

	public void setRoot(Parent root) {
		this.mainStage.getScene().setRoot(root);
		root.setId("pane");
	}

	public Scene getScene() {
		return this.mainStage.getScene();
	}

	public void addController(Controller<? extends Model, ? extends View<? extends Model>> controller) {
		this.controllers.add(controller);
		System.out.println("Controllers: " + controllers.size());
	}

	public void destroyController(Controller<? extends Model, ? extends View<? extends Model>> controller) {
		if (this.controllers.contains(controller)) {
			controller.destroy();
			this.controllers.remove(controller);
		}
		System.out.println("Controllers: " + controllers.size());
	}

	public void destroyControllers() {
		for (Controller<? extends Model, ? extends View<? extends Model>> c : this.controllers) {
			c.destroy();
		}

		this.controllers.clear();
		System.out.println("Controllers: " + controllers.size());
	}

}
