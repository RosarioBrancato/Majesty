package ch.fhnw.projectbois.components.chat;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;

public class ChatView extends View<ChatModel> {
	
	public static final double PREF_HEIGHT = 300;

	private double prefHeightOpen = 0;
	
	
	public ChatView(ChatModel model) {
		super(model);
		
		this.setPrefHeightOpen(PREF_HEIGHT);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("ChatView.fxml");
	}

	public double getPrefHeightOpen() {
		return prefHeightOpen;
	}

	public void setPrefHeightOpen(double prefHeightOpen) {
		this.prefHeightOpen = prefHeightOpen;
	}

}
