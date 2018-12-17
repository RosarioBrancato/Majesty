package ch.fhnw.projectbois.components.chat;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;

// TODO: Auto-generated Javadoc
/**
 * The Class ChatView.
 */
public class ChatView extends View<ChatModel> {
	
	/** The Constant PREF_HEIGHT. */
	public static final double PREF_HEIGHT = 300;

	/** The pref height open. */
	private double prefHeightOpen = 0;
	
	
	/**
	 * Instantiates a new chat view.
	 *
	 * @param model the model
	 */
	public ChatView(ChatModel model) {
		super(model);
		
		this.setPrefHeightOpen(PREF_HEIGHT);
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.View#getFXML()
	 */
	@Override
	protected URL getFXML() {
		return this.getClass().getResource("ChatView.fxml");
	}

	/**
	 * Gets the pref height open.
	 *
	 * @return the pref height open
	 */
	public double getPrefHeightOpen() {
		return prefHeightOpen;
	}

	/**
	 * Sets the pref height open.
	 *
	 * @param prefHeightOpen the new pref height open
	 */
	public void setPrefHeightOpen(double prefHeightOpen) {
		this.prefHeightOpen = prefHeightOpen;
	}

}
