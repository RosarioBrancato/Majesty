package ch.fhnw.projectbois.playscreen;

import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LobbyListDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Network;
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
	
	public PlayScreenModel() {
		this.lobbiesProperty = new SimpleObjectProperty<LobbyListDTO>();
		this.initResponseListener();
	}
	
	public void createLobby(String gamemode) {
		// do not forget to give the gamemode information
		Request request = new Request("PLACEHOLDER TOKEN", RequestId.CREATE_LOBBY, null);
		Network.getInstance().sendRequest(request);
	}
	
	public void getLobbies() {
		Request request = new Request("PLACEHOLDER TOKEN", RequestId.GET_LOBBIES, null);
		Network.getInstance().sendRequest(request);
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

					System.out.println("PlayScreenModel - Lobbies updated!");
				}

			}
		};
	}
	
	public SimpleObjectProperty<LobbyListDTO> getLobbiesProperty() {
		return this.lobbiesProperty;
	}
}
