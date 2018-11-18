package ch.fhnw.projectbois.playscreen;

import java.util.Date;
import java.sql.Timestamp;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.dto.LobbyListDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.lobby.LobbyController;
import ch.fhnw.projectbois.lobby.LobbyModel;
import ch.fhnw.projectbois.lobby.LobbyView;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.session.Session;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * 
 * @author Dario Stöckli
 *
 */

public class PlayScreenModel extends Model {

	private SimpleObjectProperty<LobbyListDTO> lobbiesProperty = null;
	private SimpleObjectProperty<Timestamp> errorProperty = null;
	

	public PlayScreenModel() {
		this.lobbiesProperty = new SimpleObjectProperty<>();
		this.errorProperty = new SimpleObjectProperty<>();
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
	}

	public void getLobbies() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.GET_LOBBIES, null);
		Network.getInstance().sendRequest(request);
	}

	public SimpleObjectProperty<LobbyListDTO> getLobbiesProperty() {
		return this.lobbiesProperty;
	}
	
	public SimpleObjectProperty<Timestamp> getErrorProperty() {
		return this.errorProperty;
	}

	@Override
	protected ChangeListener<Response> getChangeListener() {
		return new ChangeListener<Response>() {
			@Override
			public void changed(ObservableValue<? extends Response> observable, Response oldValue, Response newValue) {

				if (newValue.getResponseId() == ResponseId.UPDATE_LOBBIES) {
					String json = newValue.getJsonDataObject();
					LobbyListDTO lobbies = JsonUtils.Deserialize(json, LobbyListDTO.class);

					lobbiesProperty.setValue(lobbies);

				} else if (newValue.getResponseId() == ResponseId.LOBBY_CREATED
						|| newValue.getResponseId() == ResponseId.LOBBY_JOINED) {

					String json = newValue.getJsonDataObject();
					LobbyDTO lobby = JsonUtils.Deserialize(json, LobbyDTO.class);
					showLobby(lobby);
				
				} else if (newValue.getResponseId() == ResponseId.LOBBY_ERROR) {
					errorProperty.setValue(new Timestamp(new Date().getTime()));
				}

			}
		};
	}

	private void showLobby(LobbyDTO lobby) {
		LobbyController controller = Controller.initMVC(LobbyController.class, LobbyModel.class, LobbyView.class);
		controller.setLobby(lobby);

		Platform.runLater(() -> {
			MetaContainer.getInstance().setRoot(controller.getViewRoot());
		});
	}
}
