package ch.fhnw.projectbois.components.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.dto.MessageDTO;
import ch.fhnw.projectbois.enumerations.ChatMember;
import ch.fhnw.projectbois.session.Session;
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
	private boolean initMessageListener = true;

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

			if (initMessageListener) {
				// -------------GUI is ready now-------------
				model.getChatProperty().addListener((observer2, oldValue2, newValue2) -> {
					updateChatView(newValue2);
				});
				
				initMessageListener = false;
			}
		});

		model.getLobbyInfo();
	}

	private void mapUserNames(LobbyDTO lobbyInfo) {
		userNameMap.clear();
		ArrayList<String> players = lobbyInfo.getPlayers();

		userNameMap.put(players.get(0), ChatMember.Player1);
		if (players.size() >= 2) {
			userNameMap.put(players.get(1), ChatMember.Player2);
		}
		if (players.size() >= 3) {
			userNameMap.put(players.get(2), ChatMember.Player3);
		}
		if (players.size() >= 4) {
			userNameMap.put(players.get(3), ChatMember.Player4);
		}
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

		// check if whispering and set receiver
		String messageText = message.getMessage();
		String[] parts = messageText.split(":");
		String part1 = parts[0];

		ChatMember receiver = userNameMap.get(part1);
		if (receiver != null && receiver != identifyOwnChatMember()) {
			String newMessage = "";
			for(int i = 1; i < parts.length; i++) {
				newMessage += parts[i];
				
				if(i + 1 < parts.length) {
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

	private void updateChatView(MessageDTO message) {
		Platform.runLater(() -> {
			// check if author is Player1-4, then post message as globally
			if (message.getAuthor() != ChatMember.System || message.getAuthor() != ChatMember.All
					|| message.getAuthor() != null) {

				String username = getUsernameByChatmember(message.getAuthor());
				txtChat.appendText(username + ": " + message.getMessage() + "\n");
			}

		});
	}
	
	private String getUsernameByChatmember(ChatMember chatMember) {
		String username = "";
		
		Set<String> usernames = userNameMap.keySet();
		
		for(String name : usernames) {
			ChatMember member = userNameMap.get(name);
			
			if(member == chatMember) {
				username = name;
				break;
			}
		}
		
		return username;
	}
	

}
