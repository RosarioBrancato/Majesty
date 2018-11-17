package ch.fhnw.projectbois.playscreen;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.dto.LobbyListDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

/**
 * 
 * @author Dario Stöckli
 *
 */

public class PlayScreenController extends Controller<PlayScreenModel, PlayScreenView> {

	private final String SIDE_A = "Side A";
	private final String SIDE_B = "Side B";

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

		model.getLobbiesProperty().addListener((observer, oldValue, newValue) -> {
			fillListView(newValue);
		});

		this.getLobbies();
	}

	private void fillChoiceBox() {
		this.cmbCardSide.getItems().add(SIDE_A);
		this.cmbCardSide.getItems().add(SIDE_B);

		this.cmbCardSide.getSelectionModel().selectFirst();
	}

	private void getLobbies() {
		model.getLobbies();
	}

	private void fillListView(LobbyListDTO lobbies) {
		Platform.runLater(() -> {
			lstLobbies.getItems().clear();
			lstLobbies.getItems().addAll(lobbies.getLobbies());
		});
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
		LobbyDTO lobby = lstLobbies.getSelectionModel().getSelectedItem();
		if (lobby != null) {
			model.joinLobby(lobby);
		}
	}

	@FXML
	private void btnRefresh_Click(ActionEvent e) {
		getLobbies();
	}

}
