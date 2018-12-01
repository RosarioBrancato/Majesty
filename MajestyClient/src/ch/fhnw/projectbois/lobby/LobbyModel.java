package ch.fhnw.projectbois.lobby;

import java.sql.Timestamp;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.dto.UserDTO;
import ch.fhnw.projectbois.game.GameController;
import ch.fhnw.projectbois.game.GameModel;
import ch.fhnw.projectbois.game.GameView;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.session.Session;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class LobbyModel extends Model {
	
	private SimpleObjectProperty<LobbyDTO> lobbyProperty = null;
	private SimpleObjectProperty<Timestamp> errorProperty = null;
	private UserDTO user = null;

	public LobbyModel() {
		this.lobbyProperty = new SimpleObjectProperty<>();
		this.errorProperty = new SimpleObjectProperty<>();
		this.user = new UserDTO();
		this.initResponseListener();
		determineLobbyUser();
	}
	
	//Starts the Game of Majesty with 2 - 4 players that were in the lobby
	public void startGame() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.START_GAME, null);
		Network.getInstance().sendRequest(request);
	}
	
	//Forwards the Player to the Exit screen from the lobby
	public void ExitGame(LobbyDTO lobby) {
		String json = JsonUtils.Serialize(lobby);
		Request request = new Request(Session.getCurrentUserToken(), RequestId.LEAVE_LOBBY, json);
		Network.getInstance().sendRequest(request);
	}
	
	public SimpleObjectProperty<LobbyDTO> getLobbyProperty() {
		return this.lobbyProperty;
	}
	
	public UserDTO getUser() {
		return this.user;
	}

	public SimpleObjectProperty<Timestamp> getErrorProperty() {
		return this.errorProperty;
	}

	@Override
	protected ChangeListener<Response> getChangeListener() {
		return new ChangeListener<Response>() {

			@Override
			public void changed(ObservableValue<? extends Response> observable, Response oldValue, Response newValue) {
				
				//The game was launched and the board is loaded
				if (newValue.getResponseId() == ResponseId.GAME_STARTED) {
					showGameBoard();
				//A player joins and the GUI has to be updated
				} else if (newValue.getResponseId() == ResponseId.LOBBY_JOINED_MULTICAST 
						|| newValue.getResponseId() == ResponseId.LOBBY_LEFT_MULTICAST) {
					String json = newValue.getJsonDataObject();
					LobbyDTO lobby = JsonUtils.Deserialize(json, LobbyDTO.class);
					lobbyProperty.setValue(lobby);
				//The client needs to know what user is associated with it
				} else if (newValue.getResponseId() == ResponseId.LOBBY_USER_INFO) {
					String json = newValue.getJsonDataObject();
					user = JsonUtils.Deserialize(json, UserDTO.class);					
				}
			}
		};
	}
	
	//Determine the owner of the Lobby for advanced privileges
	public String determineLobbyOwner(LobbyDTO lobby) {
		String owner = lobby.getPlayers().get(0);
		return owner;
	}
	
	//Determine the player (ServerClient) of the Lobby for comparison
	public void determineLobbyUser() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.GET_USER_OF_CLIENT, null);
		Network.getInstance().sendRequest(request);
	}
	
	//Determine whether player and owner match
	public boolean isLobbyOwner(LobbyDTO lobby, UserDTO user) {
		if (determineLobbyOwner(lobby).equals(user.getUsername())) {
			return true;
		} else return false;
	}

	private void showGameBoard() {
		Platform.runLater(() -> {
			GameController controller = Controller.initMVC(GameController.class, GameModel.class, GameView.class);
			MetaContainer.getInstance().setRoot(controller.getViewRoot());
		});
	}
	
}
