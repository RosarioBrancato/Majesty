package ch.fhnw.projectbois.lobby;

import java.util.Optional;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.components.menubar.MenuBarController;
import ch.fhnw.projectbois.components.menubar.MenuBarModel;
import ch.fhnw.projectbois.components.menubar.MenuBarView;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.login.LoginController;
import ch.fhnw.projectbois.login.LoginModel;
import ch.fhnw.projectbois.login.LoginView;
import ch.fhnw.projectbois.profile.ProfileController;
import ch.fhnw.projectbois.profile.ProfileModel;
import ch.fhnw.projectbois.profile.ProfileView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class LobbyController extends Controller<LobbyModel, LobbyView> {

	private LobbyDTO lobby = null;

	@FXML
	private Label lblJointPlayers_Dynamic;
	
	@FXML
	private Label lblJointPlayersCount_Dynamic;

	public LobbyController(LobbyModel model, LobbyView view) {
		super(model, view);
	}

	public void setLobby(LobbyDTO lobby) {
		this.lobby = lobby;
		this.lblJointPlayersCount_Dynamic.setText(translator.getTranslation("lbl_LobbyView_JointPlayersCountStatic"));
		this.lblJointPlayers_Dynamic.setText(translator.getTranslation("lbl_LobbyView_Information") + this.lobby.getPlayersAsString());
	}

	@FXML
	private void btnStartGame_Click(ActionEvent e) {
		model.startGame();
	}
	
	@FXML
	private void btnExitGame_Click(ActionEvent e) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(translator.getTranslation("dgr_LobbyView_ExitAlertTitle"));
		alert.setHeaderText(translator.getTranslation("dgr_LobbyView_ExitAlertHeader"));
		alert.setContentText(translator.getTranslation("dgr_LobbyView_ExitAlertContent"));

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			//Close Lobby
			model.ExitGame();
			//Reload PlayScreen
			MenuBarController menu = Controller.initMVC(MenuBarController.class, MenuBarModel.class, MenuBarView.class);
			MetaContainer.getInstance().setRoot(menu.getViewRoot());
		} else {
		    //Dialogue cancelled, do nothing
		}
		
	}

}
