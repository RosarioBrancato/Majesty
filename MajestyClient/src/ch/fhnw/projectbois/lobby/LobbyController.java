package ch.fhnw.projectbois.lobby;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.dto.LobbyDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LobbyController extends Controller<LobbyModel, LobbyView> {

	private LobbyDTO lobby = null;

	@FXML
	private Label lblInfo;

	public LobbyController(LobbyModel model, LobbyView view) {
		super(model, view);
	}

	public void setLobby(LobbyDTO lobby) {
		this.lobby = lobby;

		this.lblInfo.setText("Lobby's players: " + this.lobby.getPlayersAsString());
	}

	@FXML
	private void btnStartGame_Click(ActionEvent e) {
		model.startGame();
	}

}
