package ch.fhnw.projectbois.components.chat;

import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.dto.MessageDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.session.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * 
 * @author Leeroy Koller
 *
 */

public class ChatModel extends Model {

	private SimpleObjectProperty<MessageDTO> chatProperty = null;
	private SimpleObjectProperty<LobbyDTO> lobbyProperty = null;

	public ChatModel() {
		this.chatProperty = new SimpleObjectProperty<>();
		this.lobbyProperty = new SimpleObjectProperty<>();
		this.initResponseListener();
	}

	public void sendMessage(MessageDTO message) {
//		create object
		String json = JsonUtils.Serialize(message);

//		create and send request. UserToken: identify user, RequestId: knows which logic to use, json: has all the infos
		Request request = new Request(Session.getCurrentUserToken(), RequestId.CHAT_SEND_MSG, json);

		Network.getInstance().sendRequest(request);
	}

	public void whisperMessage() {

	}

	// receive response from server
	@Override
	protected ChangeListener<Response> getChangeListener() {
		return new ChangeListener<Response>() {
			@Override
			public void changed(ObservableValue<? extends Response> observable, Response oldValue, Response newValue) {

				if (newValue.getResponseId() == ResponseId.RECEIVE_MSG) {
					String json = newValue.getJsonDataObject();
					MessageDTO message = JsonUtils.Deserialize(json, MessageDTO.class);

					chatProperty.setValue(message);

				} else if (newValue.getResponseId() == ResponseId.LOBBY_INFO) {
					String json = newValue.getJsonDataObject();
					LobbyDTO lobbyInfo = JsonUtils.Deserialize(json, LobbyDTO.class);

					lobbyProperty.setValue(lobbyInfo);

				} else if (newValue.getResponseId() == ResponseId.CHAT_ERROR) {
					
				} 

			}
		};
	}

	public SimpleObjectProperty<MessageDTO> getChatProperty() {
		return this.chatProperty;
	}
	
	public SimpleObjectProperty<LobbyDTO> getLobbyProperty() {
		return this.lobbyProperty;
	}

	public void checkIfWhisper(MessageDTO message) {
		// in UserDTO, getUsername()
//		if (message.getText().matches(null){
//			
//		}

	}

}
