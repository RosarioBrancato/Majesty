package ch.fhnw.projectbois.lobby;

import java.util.Optional;
import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._utils.DialogUtils;
import ch.fhnw.projectbois.components.menubar.MenuBarController;
import ch.fhnw.projectbois.components.menubar.MenuBarModel;
import ch.fhnw.projectbois.components.menubar.MenuBarView;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.session.Session;
import ch.fhnw.projectbois.time.Time;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

/**
 * The LobbyController Class.
 *
 * @author Dario Stoeckli
 * 
 * The Lobby can contain at max 4 players from here the game will be launched
 * once the owner wants to
 */

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

	/**
	 * Instantiates a new lobby controller.
	 *
	 * @param model the model
	 * @param view the view
	 */
	public LobbyController(LobbyModel model, LobbyView view) {
		super(model, view);
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Controller#initialize()
	 * Additionally:
	 * Adds a LobbyProperty Listener that updates the GUI on events like player joined,left
	 * Determines which user is associated with the session
	 * Determines which lobby the user is associated with so the "latest" information can be loaded
	 * 
	 */
	@Override
	protected void initialize() {
		super.initialize();

		updateUser();

		model.getLobbyProperty().addListener((observer, oldValue, newValue) -> {
			setLobby(newValue);
		});
		
		model.getLobbyOfUser();
	}

	/**
	 * Sets the lobby.
	 *
	 * @param lobby the new lobby
	 */
	public void setLobby(LobbyDTO lobby) {
		this.lobby = lobby;
		updateLobby(lobby);
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Controller#destroy()
	 * Additionally stops the running countdown
	 */
	@Override
	public void destroy() {
		super.destroy();

		this.stopCountdown();
	}

	/**
	 * Update lobby.
	 * Handles all the dynamic fields and conditions in the lobby and releases functions accordingly
	 *
	 * @param lobby the lobby
	 */
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

	/**
	 * Shows Label with currently logged in user
	 */
	private void updateUser() {
		Platform.runLater(() -> {
			this.lblPlayerInformation.setText(
					translator.getTranslation("lbl_LobbyView_PlayerInformation") + " " + Session.getCurrentUsername());
		});
	}

	/**
	 * Start countdown.
	 * This is the Lobby Timer
	 * When a lobby is launched this is set to 360 seconds
	 * When a player joins the DTO contains the current lifetime of the lobby and sets it accordingly
	 *
	 * @param seconds the seconds
	 */
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

	/**
	 * Stop countdown.
	 * Used when the lobby gets destroyed
	 */
	private void stopCountdown() {
		if (this.timer != null) {
			this.timer.getPeriodCounterProperty().removeListener(this.periodCounterPropertyListener);
			this.timer.stop();
		}
	}

	/**
	 * Inits the period counter property listener.
	 * Once the lifetime has expired the owner will be asked to extend or kill the lobby
	 */
	private void initPeriodCounterPropertyListener() {
		this.periodCounterPropertyListener = (observer, oldValue, newValue) -> {
			if (newValue.intValue() == 0) {
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

	/**
	 * Update countdown in the GUI
	 */
	private void updateCountdownGUI() {
		int counter = timer.getCounter();

		if (counter >= 0) {
			Platform.runLater(() -> {
				lblCountdown_Dynamic
						.setText(translator.getTranslation("lbl_LobbyView_Countdown") + " " + timer.getCounterAsString());
			});
		}
	}

	/**
	 * One player in the lobby.
	 * Set labels and information accordingly
	 * 
	 * Do not allow the owner to start the game
	 */
	private void onePlayerLobby() {
		this.lblInformation_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InformationDynamic1"));
		this.lblInstructions_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InstructionsDynamic1"));
		disableStartButton();
	}

	/**
	 * Two or three players in the lobby.
	 * Set labels and information accordingly
	 * 
	 * Allow the owner to start the game
	 */
	private void twothreePlayerLobby() {
		this.lblInformation_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InformationDynamic2"));
		this.lblInstructions_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InstructionsDynamic2"));
		if (model.isLobbyOwner(lobby))
			enableStartButton();
		else
			disableStartButton();
	}

	/**
	 * Four players in the lobby.
	 * Set labels and information accordingly
	 * 
	 * Allow the owner to start the game (but do not force even though lobby is full)
	 */
	private void fourPlayerLobby() {
		this.lblInformation_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InformationDynamic2"));
		this.lblInstructions_Dynamic.setText(translator.getTranslation("lbl_LobbyView_InstructionsDynamic3"));
		if (model.isLobbyOwner(lobby))
			enableStartButton();
		else
			disableStartButton();
	}

	/**
	 * Disables the start button.
	 */
	private void disableStartButton() {
		btnStart.setDisable(true);
	}

	/**
	 * Enables the start button.
	 */
	private void enableStartButton() {
		btnStart.setDisable(false);
	}

	/**
	 * Btn start game click.
	 * Launches the boardgame with all the players in the lobby
	 *
	 * @param e the e
	 */
	@FXML
	private void btnStartGame_Click(ActionEvent e) {
		model.startGame();
	}

	/**
	 * Btn exit game click.
	 * Prompts the user with an alert window to confirm
	 * Either exits the game or stays
	 *
	 * @param e the e
	 */
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
