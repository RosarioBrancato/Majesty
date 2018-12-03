package ch.fhnw.projectbois.playscreen;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.dto.LobbyListDTO;
import ch.fhnw.projectbois.time.Time;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * 
 * @author Dario Stöckli
 *
 */

public class PlayScreenController extends Controller<PlayScreenModel, PlayScreenView> {

	private final String SIDE_A = "Side A";
	private final String SIDE_B = "Side B";
	
	private Time timer = null;
	private ChangeListener<Number> timerPropertyListener = null;

	private ChangeListener<LobbyListDTO> lobbyListPropertyListener = null;

	@FXML
	private ChoiceBox<String> cmbCardSide;

	@FXML
	private ListView<LobbyDTO> lstLobbies;

	public PlayScreenController(PlayScreenModel model, PlayScreenView view) {
		super(model, view);
	}

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

	@Override
	public void destroy() {
		super.destroy();

		this.timer.getPeriodCounterProperty().removeListener(this.timerPropertyListener);
		this.timer.stop();
		
		this.model.getLobbiesProperty().removeListener(this.lobbyListPropertyListener);
	}

	private void fillChoiceBox() {
		this.cmbCardSide.getItems().add(SIDE_A);
		this.cmbCardSide.getItems().add(SIDE_B);

		this.cmbCardSide.getSelectionModel().selectFirst();
	}

	private void fillListView(LobbyListDTO lobbies) {
		Platform.runLater(() -> {
			lstLobbies.getItems().clear();
			lstLobbies.getItems().addAll(lobbies.getLobbies());
			lstLobbies.getSelectionModel().selectFirst();
		});
	}

	private void initLobbyListPropertyListener() {
		this.lobbyListPropertyListener = new ChangeListener<LobbyListDTO>() {

			@Override
			public void changed(ObservableValue<? extends LobbyListDTO> observable, LobbyListDTO oldValue,
					LobbyListDTO newValue) {

				fillListView(newValue);
			}
		};
	}
	
	private void initTimerPropertyListener() {
		this.timerPropertyListener = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				model.getLobbies();
			}
		};
	}

	@FXML
	public void btnStart_Click(ActionEvent e) {
		String side = cmbCardSide.getValue();
		boolean isSindA = side.equals(SIDE_A);

		LobbyDTO lobby = new LobbyDTO();
		lobby.setCardSideA(isSindA);

		model.createLobby(lobby);
	}

	@FXML
	private void btnJoin_Click(ActionEvent e) {
		this.btnRefresh_Click(new ActionEvent());
		LobbyDTO lobby = lstLobbies.getSelectionModel().getSelectedItem();
		if (lobby != null) {
			model.joinLobby(lobby);
		}
	}

	@FXML
	private void lstLobbies_DoubleClick(MouseEvent click) {
		if (click.getButton() == MouseButton.PRIMARY && click.getClickCount() == 2) {
			this.btnJoin_Click(new ActionEvent());
		}
	}

	@FXML
	private void btnRefresh_Click(ActionEvent e) {
		model.getLobbies();
	}

}
