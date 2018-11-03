package ch.fhnw.projectbois.components.chat;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;

public class ChatView extends View<ChatModel> {

	protected ChatView(ChatModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("ChatView.fxml");
	}

}
