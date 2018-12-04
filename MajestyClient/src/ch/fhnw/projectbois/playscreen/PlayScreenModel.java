package ch.fhnw.projectbois.playscreen;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.dto.LobbyListDTO;
import ch.fhnw.projectbois.dto.MessageDTO;
import ch.fhnw.projectbois.dto.ReportDTO;
import ch.fhnw.projectbois.dto.UserDTO;
import ch.fhnw.projectbois.enumerations.ChatMember;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.lobby.LobbyController;
import ch.fhnw.projectbois.lobby.LobbyModel;
import ch.fhnw.projectbois.lobby.LobbyView;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.session.Session;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

/**
 * 
 * @author Dario Stoeckli
 *
 */

public class PlayScreenModel extends Model {

	private SimpleObjectProperty<LobbyListDTO> lobbiesProperty = null;

	private UserDTO user = null;
	private MessageDTO message = null;

	public PlayScreenModel() {
		this.lobbiesProperty = new SimpleObjectProperty<>();

		this.user = new UserDTO();

		this.message = new MessageDTO();
		this.message.setReceiver(ChatMember.All);
		this.message.setAuthor(ChatMember.System);

		this.initResponseListener();
		determinePlayScreenUser();
	}

	public void createLobby(LobbyDTO lobby) {
		String json = JsonUtils.Serialize(lobby);
		Request request = new Request(Session.getCurrentUserToken(), RequestId.CREATE_LOBBY, json);
		Network.getInstance().sendRequest(request);
	}

	public void joinLobby(LobbyDTO lobby) {
		String json = JsonUtils.Serialize(lobby);
		Request request = new Request(Session.getCurrentUserToken(), RequestId.JOIN_LOBBY, json);
		Network.getInstance().sendRequest(request);

		message.setMessage(user.getUsername() + " " + translator.getTranslation("msg_LobbyView_PlayerJoined"));
		String json1 = JsonUtils.Serialize(message);
		Request request1 = new Request(Session.getCurrentUserToken(), RequestId.CHAT_SEND_MSG, json1);
		Network.getInstance().sendRequest(request1);
	}

	public void getLobbies() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.GET_LOBBIES, null);
		Network.getInstance().sendRequest(request);
	}

	public SimpleObjectProperty<LobbyListDTO> getLobbiesProperty() {
		return this.lobbiesProperty;
	}

	public UserDTO getUser() {
		return this.user;
	}

	// Determine the player (ServerClient) of the Lobby for comparison
	public void determinePlayScreenUser() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.GET_USER_OF_CLIENT, null);
		Network.getInstance().sendRequest(request);
	}

	@Override
	protected ChangeListener<Response> getChangeListener() {
		return (observer, oldValue, newValue) -> {
			if (newValue.getResponseId() == ResponseId.UPDATE_LOBBIES) {
				String json = newValue.getJsonDataObject();
				LobbyListDTO lobbies = JsonUtils.Deserialize(json, LobbyListDTO.class);

				lobbiesProperty.setValue(lobbies);

			} else if (newValue.getResponseId() == ResponseId.LOBBY_CREATED
					|| newValue.getResponseId() == ResponseId.LOBBY_JOINED) {

				String json = newValue.getJsonDataObject();
				LobbyDTO lobby = JsonUtils.Deserialize(json, LobbyDTO.class);
				showLobby(lobby);

			} else if (newValue.getResponseId() == ResponseId.LOBBY_USER_INFO) {
				String json = newValue.getJsonDataObject();
				user = JsonUtils.Deserialize(json, UserDTO.class);

			} else if (newValue.getResponseId() == ResponseId.PLAY_SCREEN_ERROR) {
				String json = newValue.getJsonDataObject();
				ReportDTO report = JsonUtils.Deserialize(json, ReportDTO.class);
				getReportProperty().setValue(report);
			}
		};
	}

	private void showLobby(LobbyDTO lobby) {
		Platform.runLater(() -> {
			Controller.initMVCAsRoot(LobbyController.class, LobbyModel.class, LobbyView.class);
		});
	}
}
