package ch.fhnw.projectbois.components.chat;

import java.util.ArrayList;
import java.util.HashMap;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.dto.MessageDTO;
import ch.fhnw.projectbois.enumerations.ChatMember;
import ch.fhnw.projectbois.session.Session;
import ch.fhnw.projectbois.utils.MapUtils;
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

	private HashMap<String, ChatMember> userNameMap = new HashMap<>();

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

		model.getLobbyProperty().addListener((observer, oldValue, newValue) -> {
			mapUserNames(newValue);

			// -------------GUI is ready now-------------
			model.getChatProperty().addListener((observer2, oldValue2, newValue2) -> {
				updateChatView(newValue2);
			});
		});
	}

	private void mapUserNames(LobbyDTO lobbyInfo) {
		// who is in the lobby?
		ArrayList<String> players = lobbyInfo.getPlayers();

		userNameMap.put(players.get(0), ChatMember.Player1);
		userNameMap.put(players.get(1), ChatMember.Player2);
		userNameMap.put(players.get(2), ChatMember.Player3);
		userNameMap.put(players.get(3), ChatMember.Player4);
	}

	private ChatMember identifyOwnChatMember() {
		String username = Session.getCurrentUsername();
		ChatMember ownChatMember = userNameMap.get(username);

		return ownChatMember;
	}

	@FXML
	private void btnSend_Click(ActionEvent event) {
		MessageDTO message = new MessageDTO();

		message.setMessage(txtMessage.getText());
		message.setAuthor(identifyOwnChatMember());

		// check if whispering
		String messageText = message.getMessage();
		String[] parts = messageText.split(":");
		String part1 = parts[0];
		
		ChatMember receiver = userNameMap.get(part1);
		if(receiver != null && receiver != identifyOwnChatMember()) {
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

	private void updateChatView(MessageDTO message) {
		Platform.runLater(() -> { 
			//check if author is Player1-4, then post message
			if (message.getAuthor() != ChatMember.System || message.getAuthor() != ChatMember.All || 
					message.getAuthor() != null) {
				
				String username = MapUtils.getKeysByValue(userNameMap, message.getAuthor());
				txtChat.appendText(username + ": " + message.getMessage() + "\n");
			}
			
		});
	}

}
