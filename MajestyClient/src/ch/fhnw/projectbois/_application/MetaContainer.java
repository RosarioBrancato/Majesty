package ch.fhnw.projectbois._application;

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

	private MetaContainer() {

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

	public void setRoot(Parent root) {
		Scene scene = new Scene(root);
		this.mainStage.setScene(scene);
	}

}
