package ch.fhnw.projectbois.lobby;

import java.util.Optional;
import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.components.menubar.MenuBarController;
import ch.fhnw.projectbois.components.menubar.MenuBarModel;
import ch.fhnw.projectbois.components.menubar.MenuBarView;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.session.Session;
import ch.fhnw.projectbois.time.Time;
import ch.fhnw.projectbois.utils.DialogUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class LobbyController extends Controller<LobbyModel, LobbyView> {

	private LobbyDTO lobby = null;
	private Time timer;
	private ChangeListener<Number> periodCounterPropertyListener = null;

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

		updateUser();

		model.getLobbyProperty().addListener((observer, oldValue, newValue) -> {
			setLobby(newValue);
		});

		model.getLobbyOfUser();
	}

	public void setLobby(LobbyDTO lobby) {
		this.lobby = lobby;
		updateLobby(lobby);
	}

	@Override
	public void destroy() {
		super.destroy();

		this.stopCountdown();
	}

	// Handles all the dynamic fields and conditions in the lobby
	private void updateLobby(LobbyDTO lobby) {
		Platform.runLater(() -> {
			if (lobby.getPlayers().size() == 1)
				onePlayerLobby();
			else if (lobby.getPlayers().size() <= 3)
				twothreePlayerLobby();
			else if (lobby.getPlayers().size() == 4)
				fourPlayerLobby();
			this.lblJoinedPlayersCount_Dynamic.setText(lobby.getPlayers().size() + " "
					+ translator.getTranslation("lbl_LobbyView_JoinedPlayersCountStatic") + " "
					+ model.determineLobbyOwner(lobby));
			this.lblJoinedPlayers_Dynamic.setText(lobby.getPlayersAsString());
		});

		this.startCountdown(lobby.getLifetime());
	}

	// Handles Player related matters and determines the owner of the lobby with
	// elevated privileges
	private void updateUser() {
		Platform.runLater(() -> {
			this.lblPlayerInformation.setText(
					translator.getTranslation("lbl_LobbyView_PlayerInformation") + " " + Session.getCurrentUsername());
		});
	}

	// Lobby Timer
	private void startCountdown(int seconds) {
		if (this.timer == null) {
			this.timer = new Time();
			this.initPeriodCounterPropertyListener();
			this.timer.getPeriodCounterProperty().addListener(this.periodCounterPropertyListener);
			this.timer.startCountdown(seconds);

		} else {
			this.timer.setCounter(seconds);
		}
	}

	private void stopCountdown() {
		if (this.timer != null) {
			this.timer.getPeriodCounterProperty().removeListener(this.periodCounterPropertyListener);
			this.timer.stop();
		}
	}

	private void initPeriodCounterPropertyListener() {
		this.periodCounterPropertyListener = (observer, oldValue, newValue) -> {
			if (newValue.intValue() == 0) {
				// timer.stop();
				if (model.isLobbyOwner(lobby)) {
					Platform.runLater(() -> {
						// Create Alert
						ButtonType buttonTypeExtend = new ButtonType(
								translator.getTranslation("dgr_LobbyView_CountdownButtonExtend"));
						ButtonType buttonTypeClose = new ButtonType(
								translator.getTranslation("dgr_LobbyView_CountdownButtonClose"));
						ButtonType[] buttons = new ButtonType[] { buttonTypeExtend, buttonTypeClose };

						Alert alert = DialogUtils.getAlert(MetaContainer.getInstance().getMainStage(),
								AlertType.CONFIRMATION,
								translator.getTranslation("dgr_LobbyView_CountdownAlertContent"), buttons);

						Optional<ButtonType> result = alert.showAndWait();
						if (result.get() == buttonTypeClose) {
							
							model.destroyLobby();
							Controller.initMVCAsRoot(MenuBarController.class, MenuBarModel.class, MenuBarView.class);
							
						} else if (result.get() == buttonTypeExtend) {
							model.extendLifetime();
						}
					});
				}

			}
			updateCountdownGUI();
		};
	}

	private void updateCountdownGUI() {
		int counter = timer.getCounter();

		if (counter >= 0) {
			Platform.runLater(() -> {
				lblCountdown_Dynamic
						.setText(translator.getTranslation("lbl_LobbyView_Countdown") + " " + timer.getCounterAsString());
			});
		}
	}

	private void onePlayerLobby() {
		this.lblInformation_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InformationDynamic1"));
		this.lblInstructions_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InstructionsDynamic1"));
		disableStartButton();
	}

	private void twothreePlayerLobby() {
		this.lblInformation_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InformationDynamic2"));
		this.lblInstructions_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InstructionsDynamic2"));
		if (model.isLobbyOwner(lobby))
			enableStartButton();
		else
			disableStartButton();
	}

	private void fourPlayerLobby() {
		this.lblInformation_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InformationDynamic2"));
		this.lblInstructions_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InstructionsDynamic3"));
		if (model.isLobbyOwner(lobby))
			enableStartButton();
		else
			disableStartButton();
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
		Alert alert = DialogUtils.getAlert(MetaContainer.getInstance().getMainStage(), AlertType.CONFIRMATION,
				translator.getTranslation("dgr_LobbyView_ExitAlertContent"));

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			// Close Lobby
			model.exitGame(lobby);
			// Reload PlayScreen
			Controller.initMVCAsRoot(MenuBarController.class, MenuBarModel.class, MenuBarView.class);
		}
	}

}
