package ch.fhnw.projectbois.playscreen;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.dto.LobbyListDTO;
import ch.fhnw.projectbois.time.Time;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * The PlayScreenController Class.
 * @author Dario Stoeckli
 *
 */

public class PlayScreenController extends Controller<PlayScreenModel, PlayScreenView> {

	private final String SIDE_A = translator.getTranslation("chcbx_PlayScreenView_CardSideA");
	private final String SIDE_B = translator.getTranslation("chcbx_PlayScreenView_CardSideB");
	
	private Time timer = null;
	
	private ChangeListener<Number> timerPropertyListener = null;
	private ChangeListener<LobbyListDTO> lobbyListPropertyListener = null;

	@FXML
	private ChoiceBox<String> cmbCardSide;
	@FXML
	private ListView<LobbyDTO> lstLobbies;

	/**
	 * Instantiates a new play screen controller.
	 *
	 * @param model the model
	 * @param view the view
	 */
	public PlayScreenController(PlayScreenModel model, PlayScreenView view) {
		super(model, view);
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Controller#initialize()
	 * Additionally:
	 * Fills the choice box for the Card Side
	 * Initializes the LobbyProperty that will update the GUI if availability of the lobby changes
	 * Sets a timer that automatically send an update request to the server every 5 seconds
	 */
	@Override
	protected void initialize() {
		super.initialize();

		this.fillChoiceBox();

		this.initLobbyListPropertyListener();
		this.model.getLobbiesProperty().addListener(this.lobbyListPropertyListener);

		this.model.getLobbies();
		
		this.timer = new Time();
		this.timer.startTimer(5000);
		
		this.initTimerPropertyListener();
		this.timer.getPeriodCounterProperty().addListener(this.timerPropertyListener);
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Controller#destroy()
	 * Additionally stops the timer and removes the listeners
	 */
	@Override
	public void destroy() {
		super.destroy();

		this.timer.getPeriodCounterProperty().removeListener(this.timerPropertyListener);
		this.timer.stop();
		
		this.model.getLobbiesProperty().removeListener(this.lobbyListPropertyListener);
	}

	/**
	 * Fill choice box.
	 */
	private void fillChoiceBox() {
		this.cmbCardSide.getItems().add(SIDE_A);
		this.cmbCardSide.getItems().add(SIDE_B);

		this.cmbCardSide.getSelectionModel().selectFirst();
	}

	/**
	 * Fill list view.
	 *
	 * @param lobbies the lobbies
	 */
	private void fillListView(LobbyListDTO lobbies) {
		Platform.runLater(() -> {
			lstLobbies.getItems().clear();
			lstLobbies.getItems().addAll(lobbies.getLobbies());
			lstLobbies.getSelectionModel().selectFirst();
		});
	}

	/**
	 * Inits the lobby list property listener.
	 * Automatically lsit available lobby on change
	 */
	private void initLobbyListPropertyListener() {
		this.lobbyListPropertyListener = (observer, oldValue, newValue) -> {
			fillListView(newValue);
		};
	}
	
	/**
	 * Inits the timer property listener.
	 */
	private void initTimerPropertyListener() {
		this.timerPropertyListener = (observer, oldValue, newValue) -> {
			model.getLobbies();
		};
	}

	/**
	 * Start Lobby on Button Click.
	 * Tells the server to start hosting a lobby with choices made by the user
	 *
	 * @param e the e
	 */
	@FXML
	public void btnStart_Click(ActionEvent e) {
		String side = cmbCardSide.getValue();
		boolean isSindA = side.equals(SIDE_A);

		LobbyDTO lobby = new LobbyDTO();
		lobby.setCardSideA(isSindA);

		model.createLobby(lobby);
	}

	/**
	 * Join Lobby on Button Click.
	 * Tells the server to join the chosen lobby
	 *
	 * @param e the e
	 */
	@FXML
	private void btnJoin_Click(ActionEvent e) {
		this.btnRefresh_Click(new ActionEvent());
		LobbyDTO lobby = lstLobbies.getSelectionModel().getSelectedItem();
		if (lobby != null) {
			model.joinLobby(lobby);
		}
	}

	/**
	 * Join Lobby on DoubleClick in ListView
	 *
	 * @param click the click
	 */
	@FXML
	private void lstLobbies_DoubleClick(MouseEvent click) {
		if (click.getButton() == MouseButton.PRIMARY && click.getClickCount() == 2) {
			this.btnJoin_Click(new ActionEvent());
		}
	}

	/**
	 * Refreshes the Lobbies on Click.
	 *
	 * @param e the e
	 */
	@FXML
	private void btnRefresh_Click(ActionEvent e) {
		model.getLobbies();
	}

}
