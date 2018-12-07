package ch.fhnw.projectbois.playscreen;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.dto.LobbyListDTO;
import ch.fhnw.projectbois.dto.ReportDTO;
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

	public PlayScreenModel() {
		this.lobbiesProperty = new SimpleObjectProperty<>();

		this.initResponseListener();
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

		// Not correct - the join lobby request could be denied by the server
		// Do on server side

//		MessageDTO message = new MessageDTO();
//		message.setReceiver(ChatMember.All);
//		message.setAuthor(ChatMember.System);
//		message.setMessage(
//				Session.getCurrentUsername() + " " + translator.getTranslation("msg_LobbyView_PlayerJoined"));
//		
//		json = JsonUtils.Serialize(message);
//		request = new Request(Session.getCurrentUserToken(), RequestId.CHAT_SEND_MSG, json);
//		Network.getInstance().sendRequest(request);
	}

	public void getLobbies() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.GET_LOBBIES, null);
		Network.getInstance().sendRequest(request);
	}

	public SimpleObjectProperty<LobbyListDTO> getLobbiesProperty() {
		return this.lobbiesProperty;
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

			} else if (newValue.getResponseId() == ResponseId.PLAY_SCREEN_ERROR) {
				String json = newValue.getJsonDataObject();
				ReportDTO report = JsonUtils.Deserialize(json, ReportDTO.class);
				getReportProperty().setValue(report);
			}
		};
	}

	private void showLobby(LobbyDTO lobby) {
		if (lobby.getId() > 0) {
			Platform.runLater(() -> {
				Controller.initMVCAsRoot(LobbyController.class, LobbyModel.class, LobbyView.class);
			});
		}
	}
}
