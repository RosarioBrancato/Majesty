package ch.fhnw.projectbois.lobby;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.components.menubar.MenuBarController;
import ch.fhnw.projectbois.components.menubar.MenuBarModel;
import ch.fhnw.projectbois.components.menubar.MenuBarView;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.dto.MessageDTO;
import ch.fhnw.projectbois.dto.ReportDTO;
import ch.fhnw.projectbois.enumerations.ChatMember;
import ch.fhnw.projectbois.game.GameController;
import ch.fhnw.projectbois.game.GameModel;
import ch.fhnw.projectbois.game.GameView;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.session.Session;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

/**
 * The LobbyModel Class.
 * 
 * @author Dario Stoeckli
 *
 * Handles all logic within the created lobby that will represent a game once started
 */

public class LobbyModel extends Model {

	private SimpleObjectProperty<LobbyDTO> lobbyProperty = null;

	/**
	 * Instantiates a new lobby model.
	 * 
	 * lobbyProperty in order to tell updates to the Controller
	 * ResponseListener in order to listen to updates from the server
	 * 
	 */
	public LobbyModel() {
		this.lobbyProperty = new SimpleObjectProperty<>();
		this.initResponseListener();
	}

	/**
	 * Gets the lobby of user.
	 * Sends a Request to the server 
	 * This will contain all necessary information about the Lobby in the DTO
	 *
	 */
	public void getLobbyOfUser() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.GET_LOBBY_OF_CLIENT, null);
		Network.getInstance().sendRequest(request);
	}

	/**
	 * Starts the Game of Majesty with 2 - 4 players that were in the lobby
	 */
	public void startGame() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.START_GAME, null);
		Network.getInstance().sendRequest(request);
	}

	/**
	 * Initiates separation procedure between client and associated lobby
	 *
	 * @param lobby the lobby
	 */
	public void exitGame(LobbyDTO lobby) {
		// Request to server about a player leaving the lobby
		String json = JsonUtils.Serialize(lobby);
		Request request = new Request(Session.getCurrentUserToken(), RequestId.LEAVE_LOBBY, json);
		Network.getInstance().sendRequest(request);

		// Message other clients via server that the player left
		MessageDTO message = new MessageDTO();
		message.setReceiver(ChatMember.All);
		message.setAuthor(ChatMember.System);
		message.setMessage(Session.getCurrentUsername() + " " + translator.getTranslation("msg_LobbyView_PlayerLeft"));
		String json1 = JsonUtils.Serialize(message);
		Request request1 = new Request(Session.getCurrentUserToken(), RequestId.CHAT_SEND_MSG, json1);
		Network.getInstance().sendRequest(request1);
		// Message other clients via server that the owner left
		if (isLobbyOwner(lobby)) {
			message.setMessage(translator.getTranslation("msg_LobbyView_PlayerOwner"));
			String json2 = JsonUtils.Serialize(message);
			Request request2 = new Request(Session.getCurrentUserToken(), RequestId.CHAT_SEND_MSG, json2);
			Network.getInstance().sendRequest(request2);
		}
	}

	/**
	 * Extend lifetime.
	 * Handles the Lobby Lifetime Extend Answer
	 */
	public void extendLifetime() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.EXTEND_LIFETIME_LOBBY, null);
		Network.getInstance().sendRequest(request);

		// TO-DO: Server should message clients about that
		MessageDTO message = new MessageDTO();
		message.setReceiver(ChatMember.All);
		message.setAuthor(ChatMember.System);
		message.setMessage(
				Session.getCurrentUsername() + " " + translator.getTranslation("msg_LobbyView_LobbyLifetimeExtended"));
		String json1 = JsonUtils.Serialize(message);
		Request request1 = new Request(Session.getCurrentUserToken(), RequestId.CHAT_SEND_MSG, json1);
		Network.getInstance().sendRequest(request1);
	}
	
	/**
	 * Destroy lobby.
	 */
	public void destroyLobby() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.DESTROY_LOBBY,null);
		Network.getInstance().sendRequest(request);
	}

	/**
	 * Gets the lobby property.
	 *
	 * @return the lobby property
	 */
	public SimpleObjectProperty<LobbyDTO> getLobbyProperty() {
		return this.lobbyProperty;
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Model#getChangeListener()
	 * Shows associated Responses for this class
	 */
	@Override
	protected ChangeListener<Response> getChangeListener() {
		return (observable, oldValue, newValue) -> {

			// The game was launched and the board is loaded
			if (newValue.getResponseId() == ResponseId.GAME_STARTED) {
				showGameBoard();

			// A player joins, leaves or the the lifetime gets extended - the GUI has to be updated
			} else if (newValue.getResponseId() == ResponseId.LOBBY_INFO
					|| newValue.getResponseId() == ResponseId.LOBBY_JOINED_MULTICAST
					|| newValue.getResponseId() == ResponseId.LOBBY_LEFT_MULTICAST
					|| newValue.getResponseId() == ResponseId.LOBBY_LIFETIME_EXTENDED) {

				String json = newValue.getJsonDataObject();
				LobbyDTO lobby = JsonUtils.Deserialize(json, LobbyDTO.class);
				lobbyProperty.setValue(lobby);
			// In case of an error create a ReportDTO to show the client some kind of feedback
			} else if (newValue.getResponseId() == ResponseId.LOBBY_ERROR) {
				String json = newValue.getJsonDataObject();
				ReportDTO report = JsonUtils.Deserialize(json, ReportDTO.class);
				getReportProperty().setValue(report);
			// Lobby Lifetime was not extended, initiate appropriate action
			} else if (newValue.getResponseId() == ResponseId.LOBBY_DIED) {
				String json = newValue.getJsonDataObject();
				ReportDTO report = JsonUtils.Deserialize(json, ReportDTO.class);
				getReportProperty().setValue(report);
				Controller.initMVCAsRoot(MenuBarController.class, MenuBarModel.class, MenuBarView.class);
			}

		};
	}
	
	/**
	 * Determine lobby owner.
	 *
	 * @param lobby the lobby
	 * @return the owner as string
	 */
	public String determineLobbyOwner(LobbyDTO lobby) {
		return lobby.getPlayers().get(0);
	}

	/**
	 * Determine whether player and owner match
	 *
	 * @param lobby the lobby
	 * @return true, if is lobby owner
	 */
	public boolean isLobbyOwner(LobbyDTO lobby) {
		String owner = determineLobbyOwner(lobby);
		if (owner.equals(Session.getCurrentUsername())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Show game board.
	 * Game will begin
	 */
	private void showGameBoard() {
		Platform.runLater(() -> {
			Controller.initMVCAsRoot(GameController.class, GameModel.class, GameView.class);
		});
	}

}
