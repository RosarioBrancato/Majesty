package ch.fhnw.projectbois.components.chat;

import ch.fhnw.projectbois._mvc.Model;

public class ChatModel extends Model {
	
	
	public String message;
	
	public void sendMessage() {
	
//		get text
//		message = ChatModel.txtMessage.getText();   
		
//		create object
//		String json = JsonUtils.Serialize(message);
		
//		create and send request
//		Request request = new Request("XXX", RequestId.DO_MOVE, json);
//		
//		Network.getInstance().sendRequest(request);
//      postMessage("Me",message);
	}
	
	public void whisperMessage() {

	}
	
//	public void postMessage(sender, message) {
//		ChatController.txtChat.appendText(sender + ": " + message);
//	}
	
	public void getMessageFromServer() {
//		create request object
		
//		register call back
		handleResponse();
//		send request
	}
	
	public void handleResponse() {
//		check if reply is ok somehow...or do this on server side?
		
	}
	
	public void minimizeChatWindow() {
		
	}

}
