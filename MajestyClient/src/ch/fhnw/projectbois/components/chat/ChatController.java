package ch.fhnw.projectbois.components.chat;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.dto.MessageDTO;
import ch.fhnw.projectbois.enumerations.ChatMember;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * 
 * @author Leeroy Koller
 *
 */

public class ChatController extends Controller<ChatModel, ChatView> {

	// declaration of things I need need in code
	@FXML
	private TextArea txtChat;

	@FXML
	private TextArea txtMessage;

	@FXML
	private Button btnSend;

	@FXML
	private Button btnClose;

	public ChatController(ChatModel model, ChatView view) {
		super(model, view);
	}

	@Override
	protected void initialize() {
		super.initialize();

		model.getLobbyInfo();

		model.getChatProperty().addListener((observer2, oldValue2, newValue2) -> {
			updateChatView(newValue2);
		});
	}

	private void updateChatView(MessageDTO message) {
		Platform.runLater(() -> {
			// check if author is Player1-4, then post message as globally
			if (message.getAuthor() != ChatMember.System || message.getAuthor() != ChatMember.All
					|| message.getAuthor() != null) {

				String username = model.getUsernameByChatmember(message.getAuthor());
				txtChat.appendText(username + ": " + message.getMessage() + "\n");
			}

		});
	}

	@FXML
	private void btnSend_Click(ActionEvent event) {
		MessageDTO message = new MessageDTO();

		message.setMessage(txtMessage.getText());
		message.setAuthor(model.getCurrentUserChatMember());

		// check if whispering and set receiver
		String messageText = message.getMessage();
		String[] parts = messageText.split(":");
		String part1 = parts[0];

		ChatMember receiver = model.getUsernameMap().get(part1);
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
		txtMessage.clear();
	}

	@FXML
	private void btnMinimize_Click(ActionEvent event) {
		this.getViewRoot().setVisible(false);
	}

}
