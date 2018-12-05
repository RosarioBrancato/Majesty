package ch.fhnw.projectbois.lobby;

import java.util.Optional;
import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.components.menubar.MenuBarController;
import ch.fhnw.projectbois.components.menubar.MenuBarModel;
import ch.fhnw.projectbois.components.menubar.MenuBarView;
import ch.fhnw.projectbois.dto.LobbyDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class LobbyController extends Controller<LobbyModel, LobbyView> {

	private LobbyDTO lobby = null;
	
	@FXML
	private Label lblPlayerInformation;
	
	@FXML
	private Label lblInformation_Dynamic;
	
	@FXML
	private Label lblJoinedPlayersCount_Dynamic;
	
	@FXML
	private Label lblJoinedPlayers_Dynamic;
	
	@FXML
	private Label lblInstructions_Dynamic;
	
	@FXML
	private Label lblCountdown_Dynamic;
	
	@FXML
	private Button btnStart;

	public LobbyController(LobbyModel model, LobbyView view) {
		super(model, view);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		
		//setCountdown();
		
		updateUser();
		
		model.getLobbyProperty().addListener((observer, oldValue, newValue) -> {
			setLobby(newValue);
		});
	}

	public void setLobby(LobbyDTO lobby) {
		this.lobby = lobby;
		updateLobby(lobby);
	}
	
	//Handles all the dynamic fields and conditions in the lobby
	private void updateLobby(LobbyDTO lobby) {
		Platform.runLater(() -> {
		if (lobby.getPlayers().size() == 1) onePlayerLobby();
		else if (lobby.getPlayers().size() <= 3) twothreePlayerLobby();
		else if (lobby.getPlayers().size() == 4) fourPlayerLobby();
		this.lblJoinedPlayersCount_Dynamic.setText(lobby.getPlayers().size() + " " + translator.getTranslation("lbl_LobbyView_JoinedPlayersCountStatic") + " " + model.determineLobbyOwner(lobby));
		this.lblJoinedPlayers_Dynamic.setText(lobby.getPlayersAsString());
		});
	}
	
	//Handles Player related matters and determines the owner of the lobby with elevated privileges
	private void updateUser() {
		Platform.runLater(() -> {
		this.lblPlayerInformation.setText(translator.getTranslation("lbl_LobbyView_PlayerInformation") + " " + model.getUser().getUsername());
		});
	}
	
	//Lobby Timer
	/* 
	private void setCountdown() {
		Time timer = new Time();
		timer.startCountdown(10);
		timer.getPeriodCounterProperty().addListener((observer, oldValue, newValue) -> {
			if (timer.getCounterSimplified()==0) {
				timer.stop();
				if (model.isLobbyOwner(lobby, model.getUser())) {
				Platform.runLater(() -> {
				//Create Alert
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.initOwner(MetaContainer.getInstance().getMainStage());
				alert.setTitle(translator.getTranslation("dgr_LobbyView_CountdownAlertTitle"));
				alert.setHeaderText(translator.getTranslation("dgr_LobbyView_CountdownAlertHeader"));
				alert.setContentText(translator.getTranslation("dgr_LobbyView_CountdownAlertContent"));
				ButtonType buttonTypeExtend = new ButtonType(translator.getTranslation("dgr_LobbyView_CountdownButtonExtend"));
				ButtonType buttonTypeClose = new ButtonType(translator.getTranslation("dgr_LobbyView_CountdownButtonClose"));
				alert.getButtonTypes().setAll(buttonTypeExtend, buttonTypeClose);
				
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == buttonTypeClose){
					//Close Lobby
					model.ExitGame(lobby);
					//Reload PlayScreen
					MenuBarController menu = Controller.initMVC(MenuBarController.class, MenuBarModel.class, MenuBarView.class);
					MetaContainer.getInstance().setRoot(menu.getViewRoot());
				} else if (result.get() == buttonTypeExtend) {
				    setCountdown();
				}
				});
				}
			
			}
			
			Platform.runLater(() -> {
				lblCountdown_Dynamic.setText(translator.getTranslation("lbl_LobbyView_Countdown") + " " + timer.getCounter());
			 	});
		});
	}
	*/
	
	
	private void onePlayerLobby() {
		this.lblInformation_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InformationDynamic1"));
		this.lblInstructions_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InstructionsDynamic1"));
		disableStartButton();
	}
	
	private void twothreePlayerLobby() {
		this.lblInformation_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InformationDynamic2"));
		this.lblInstructions_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InstructionsDynamic2"));
		if (model.isLobbyOwner(lobby, model.getUser())) enableStartButton();
		else disableStartButton();
	}
	
	private void fourPlayerLobby() {
		this.lblInformation_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InformationDynamic2"));
		this.lblInstructions_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InstructionsDynamic3"));
		if (model.isLobbyOwner(lobby, model.getUser())) enableStartButton();
		else disableStartButton();
	}
	
	private void disableStartButton() {
		btnStart.setDisable(true);
	}
	
	private void enableStartButton() {
		btnStart.setDisable(false);
	}

	@FXML
	private void btnStartGame_Click(ActionEvent e) {
		model.startGame();
	}
	
	@FXML
	private void btnExitGame_Click(ActionEvent e) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(MetaContainer.getInstance().getMainStage());
		alert.setTitle(translator.getTranslation("dgr_LobbyView_ExitAlertTitle"));
		alert.setHeaderText(translator.getTranslation("dgr_LobbyView_ExitAlertHeader"));
		alert.setContentText(translator.getTranslation("dgr_LobbyView_ExitAlertContent"));

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			//Close Lobby
			model.ExitGame(lobby);
			//Reload PlayScreen
			Controller.initMVCAsRoot(MenuBarController.class, MenuBarModel.class, MenuBarView.class);
		} else {
		    //Dialogue cancelled, do nothing
		}
		
	}

}
