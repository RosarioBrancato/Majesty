package ch.fhnw.projectbois.components.chat;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.dto.MessageDTO;
import ch.fhnw.projectbois.enumerations.ChatMember;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * The Class ChatController.
 *
 * @author Leeroy Koller
 */

public class ChatController extends Controller<ChatModel, ChatView> {

	private ChangeListener<MessageDTO> chatPropertyListener = null;

	@FXML
	private TextArea txtChat;

	@FXML
	private TextField txtMessage;

	@FXML
	private Button btnSend;

	@FXML
	private ImageView imgSend;

	@FXML
	private ImageView imgArrow;

	@FXML
	private ImageView imgNotification;

	@FXML
	private Button btnClose;

	@FXML
	private AnchorPane pnlRoot;

	private boolean isClosed = false;

	/**
	 * Instantiates a new chat controller.
	 *
	 * @param model the model
	 * @param view  the view
	 */
	public ChatController(ChatModel model, ChatView view) {
		super(model, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.fhnw.projectbois._mvc.Controller#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();

		view.setPrefHeightOpen(ChatView.PREF_HEIGHT);
		imgNotification.setVisible(false);

		model.getLobbyInfo();

		this.initChatPropertyListener();
		model.getChatProperty().addListener(this.chatPropertyListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.fhnw.projectbois._mvc.Controller#destroy()
	 */
	@Override
	public void destroy() {
		super.destroy();

		model.getChatProperty().removeListener(this.chatPropertyListener);
	}

	/**
	 * Open chat.
	 */
	public void openChat() {
		isClosed = true;
		btnMinimize_Click(new ActionEvent());
	}

	/**
	 * Close chat.
	 */
	public void closeChat() {
		isClosed = false;
		btnMinimize_Click(new ActionEvent());

	}

	/**
	 * Inits the chat property listener.
	 */
	private void initChatPropertyListener() {
		this.chatPropertyListener = (observer2, oldValue2, newValue2) -> {
			updateChatView(newValue2);
		};
	}

	/**
	 * Update chat view.
	 * Posts the messages/whispers with the corresponding author
	 *
	 * @param message the message
	 */
	private void updateChatView(MessageDTO message) {
		String username = model.getUsernameByChatmember(message.getAuthor());
		
		String messageText = "";
		String key = message.getTranslationKey();
		if (key != null) {
			messageText = translator.getTranslation(key, message.getFormatVariablesAsArray());
		}
		
		if (messageText == null || messageText == "") {
			messageText = message.getMessage();
		}

		// show notification if chat is closed
		if (isClosed == true) {
			showNotification();
		}

		final String messageToPrint = messageText;
		Platform.runLater(() -> {
			if (message.getReceiver() == ChatMember.Player1 || message.getReceiver() == ChatMember.Player2
					|| message.getReceiver() == ChatMember.Player3 || message.getReceiver() == ChatMember.Player4) {
				txtChat.appendText("<w> " + username + ": " + messageToPrint + "\n");
			} else {
				txtChat.appendText(username + ": " + messageToPrint + "\n");
			}
		});
	}

	/**
	 * Btn send click.
	 * Handles the "send" button. Sends message/whisper to server.
	 * Checks if author is whispering and handles it.
	 *
	 * @param event the event
	 */
	@FXML
	private void btnSend_Click(ActionEvent event) {
		MessageDTO message = new MessageDTO();

		if (!txtMessage.getText().isEmpty()) {
			message.setMessage(txtMessage.getText());
			message.setAuthor(model.getCurrentUserChatMember());

			// check if whispering and set receiver
			String messageText = message.getMessage();
			String[] parts = messageText.split(":");
			String part1 = parts[0];

			ChatMember receiver = model.getUsernameMap().get(part1);
			System.out.println(receiver);
			if (receiver != null && receiver != model.getCurrentUserChatMember()) {
				String newMessage = "";
				for (int i = 1; i < parts.length; i++) {
					newMessage += parts[i];

					if (i + 1 < parts.length) {
						newMessage += ":";
					}
				}
				message.setMessage(newMessage);
				message.setReceiver(receiver);
			} else {
				message.setReceiver(ChatMember.All);
			}

			model.sendMessage(message);
		}

		txtMessage.clear();
	}

	/**
	 * On enter key.
	 *
	 * @param e the e
	 */
	@FXML
	private void onEnterKey(KeyEvent e) {
		KeyCode key = e.getCode();
		if (key == KeyCode.ENTER) {
			btnSend_Click(new ActionEvent());
		}
	}

	/**
	 * Btn minimize click.
	 * Handles minimize and maximize button.
	 *
	 * @param event the event
	 */
	@FXML
	private void btnMinimize_Click(ActionEvent event) {

		if (isClosed == false) {
			txtChat.setVisible(false);
			txtMessage.setVisible(false);
			btnSend.setVisible(false);
			imgSend.setVisible(false);

			pnlRoot.setMinHeight(20);
			pnlRoot.setMaxHeight(20);
			pnlRoot.setPrefHeight(20);

			imgArrow.setRotate(0);

			isClosed = true;
		} else if (isClosed == true) {
			txtChat.setVisible(true);
			txtMessage.setVisible(true);
			btnSend.setVisible(true);
			imgSend.setVisible(true);

			pnlRoot.setMaxHeight(view.getPrefHeightOpen());
			pnlRoot.setPrefHeight(view.getPrefHeightOpen());

			imgArrow.setRotate(180);

			isClosed = false;
		}

		imgNotification.setVisible(false);
	}

	/**
	 * Show notification.
	 */
	private void showNotification() {
		imgNotification.setVisible(true);
	}

	/**
	 * Sets the pref height open.
	 *
	 * @param height the new pref height open
	 */
	public void setPrefHeightOpen(double height) {
		view.setPrefHeightOpen(height);
	}

}
