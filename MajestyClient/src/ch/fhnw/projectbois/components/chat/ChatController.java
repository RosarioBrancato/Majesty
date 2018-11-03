package ch.fhnw.projectbois.components.chat;

import ch.fhnw.projectbois._mvc.Controller;

public class ChatController extends Controller<ChatModel, ChatView> {

	public ChatController() {
	}
	
	public ChatController(ChatModel model, ChatView view) {
		super(model, view);
	}
	
}
