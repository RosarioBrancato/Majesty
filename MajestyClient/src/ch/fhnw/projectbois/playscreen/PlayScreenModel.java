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

// TODO: Auto-generated Javadoc
/**
 * The PlayScreenModel class.
 * 
 * @author Dario Stoeckli
 * 
 * Handles all the logic within the PlayScreen MCV
 */

public class PlayScreenModel extends Model {

	private SimpleObjectProperty<LobbyListDTO> lobbiesProperty = null;

	/**
	 * Instantiates a new play screen model.
	 * ResponseListener in order to listen to updates from the server
	 * 
	 */
	public PlayScreenModel() {
		this.lobbiesProperty = new SimpleObjectProperty<>();

		this.initResponseListener();
	}

	/**
	 * Creates the lobby.
	 * Sends a creation request to the server
	 * @param lobby the lobby
	 */
	public void createLobby(LobbyDTO lobby) {
		String json = JsonUtils.Serialize(lobby);
		Request request = new Request(Session.getCurrentUserToken(), RequestId.CREATE_LOBBY, json);
		Network.getInstance().sendRequest(request);
	}

	/**
	 * Join lobby.
	 * Sends a join request to the server
	 *
	 * @param lobby the lobby
	 */
	public void joinLobby(LobbyDTO lobby) {
		String json = JsonUtils.Serialize(lobby);
		Request request = new Request(Session.getCurrentUserToken(), RequestId.JOIN_LOBBY, json);
		Network.getInstance().sendRequest(request);
	}

	/**
	 * Gets the lobbies.
	 * Sends a lobby list request to the server
	 * 
	 */
	public void getLobbies() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.GET_LOBBIES, null);
		Network.getInstance().sendRequest(request);
	}

	/**
	 * Gets the lobbies property.
	 *
	 * @return the lobbies property
	 */
	public SimpleObjectProperty<LobbyListDTO> getLobbiesProperty() {
		return this.lobbiesProperty;
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Model#getChangeListener()
	 * Shows associated Responses for this class
	 */
	@Override
	protected ChangeListener<Response> getChangeListener() {
		return (observer, oldValue, newValue) -> {
			// Getting the latest Lobby Information via the LobbyListDTO Object and update GUI accordingly
			if (newValue.getResponseId() == ResponseId.UPDATE_LOBBIES) {
				String json = newValue.getJsonDataObject();
				LobbyListDTO lobbies = JsonUtils.Deserialize(json, LobbyListDTO.class);

				lobbiesProperty.setValue(lobbies);
			// Load the Lobby MVC if the server responses with CREATED or JOINED
			} else if (newValue.getResponseId() == ResponseId.LOBBY_CREATED
					|| newValue.getResponseId() == ResponseId.LOBBY_JOINED) {

				String json = newValue.getJsonDataObject();
				LobbyDTO lobby = JsonUtils.Deserialize(json, LobbyDTO.class);
				showLobby(lobby);
			// // In case of an error create a ReportDTO to show the client some kind of feedback	
			} else if (newValue.getResponseId() == ResponseId.PLAY_SCREEN_ERROR) {
				String json = newValue.getJsonDataObject();
				ReportDTO report = JsonUtils.Deserialize(json, ReportDTO.class);
				getReportProperty().setValue(report);
			}
		};
	}

	/**
	 * Show lobby.
	 *
	 * @param lobby the lobby
	 */
	private void showLobby(LobbyDTO lobby) {
		if (lobby.getId() > 0) {
			Platform.runLater(() -> {
				Controller.initMVCAsRoot(LobbyController.class, LobbyModel.class, LobbyView.class);
			});
		}
	}
}
