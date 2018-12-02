package ch.fhnw.projectbois.utils;

import ch.fhnw.projectbois._application.MetaContainer;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class DialogUtils {

	public static Stage getStageModal(Window owner) {
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.DECORATED);
		stage.setResizable(false);

		stage.getIcons().add(MetaContainer.getInstance().getMainStage().getIcons().get(0));

		// prevent closing the window
		stage.setOnCloseRequest((e) -> {
			e.consume();
		});
		
		return stage;
	}
	
	public static Alert getAlert(Window owner, AlertType alertType, String message) {
		Alert alert = new Alert(alertType);
		alert.initOwner(MetaContainer.getInstance().getMainStage());
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setHeaderText(null);
		alert.setContentText(message);
		
		return alert;
	}
	
}
