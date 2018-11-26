package ch.fhnw.projectbois.components.chat;

import java.sql.Timestamp;
import java.util.Date;

import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.dto.LobbyListDTO;
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
	private SimpleObjectProperty<Timestamp> errorProperty = null;
	
	
	public void sendMessage(MessageDTO message) {
//		create object
		String json = JsonUtils.Serialize(message);
		
//		create and send request. UserToken: identify user, RequestId: knows which logic to use, json: has all the infos
		Request request = new Request(Session.getCurrentUserToken(), RequestId.CHAT_SEND_MSG, json);
		
		Network.getInstance().sendRequest(request);
	}
	
	public void whisperMessage() {

	}
	
	//receive response from server
	@Override
	protected ChangeListener<Response> getChangeListener() {
		return new ChangeListener<Response>() {
			@Override
			public void changed(ObservableValue<? extends Response> observable, Response oldValue, Response newValue) {

				if (newValue.getResponseId() == ResponseId.RECEIVE_MSG) {
					String json = newValue.getJsonDataObject();
					MessageDTO post = JsonUtils.Deserialize(json, MessageDTO.class);

					postMessage(post);

				} else if (newValue.getResponseId() == ResponseId.CHAT_ERROR) {
					//errorProperty.setValue(new Timestamp(new Date().getTime()));
				}

			}
		};
	}
	
	public SimpleObjectProperty<MessageDTO> getChatProperty() {
		return this.chatProperty;
	}

	public SimpleObjectProperty<Timestamp> getErrorProperty() {
		return this.errorProperty;
	}
	
	public void postMessage(MessageDTO post) { //until now only message. receiver and author?
//		ChatController.txtChat.appendText(sender + ": " + message);

	}

}
