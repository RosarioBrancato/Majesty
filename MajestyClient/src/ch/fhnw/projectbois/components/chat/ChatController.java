package ch.fhnw.projectbois.components.chat;

import java.util.ArrayList;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.dto.LobbyDTO;
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
	
//	private ChatMember player = null;
	
	public ChatController(ChatModel model, ChatView view) {
		super(model, view);
	}
	
	//declaration of things I need need in code
	@FXML
	private TextArea txtChat;
	
	@FXML
	private TextArea txtMessage;
	
	@FXML
	private Button btnSend;
	
	@FXML
	private Button btnClose;
	
	private void showUserNames(LobbyDTO lobbyInfo) {
		//who is in the lobby?
		ArrayList<String> players = lobbyInfo.getPlayers();
		players.set(1, "TestPlayer");
			
//		ChatMember.Player1 = players.get(0);
//		ChatMember.Player2 = players.get(1);
//		ChatMember.Player3 = players.get(2);
//		ChatMember.Player4 = players.get(3);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		
		model.getLobbyProperty().addListener((observer, oldValue, newValue) -> {
			showUserNames(newValue);
		});
		//resultat:
		// ChatMember.Player1 -> arrayList.get(0)
		// ChatMember.Player2 -> arrayList.get(1)
		// ChatMember.Player3 -> arrayList.get(2)
		// ChatMember.Player4 -> arrayList.get(3)
		// Frage? Welcher bin ich?
		
		
		//GUI is ready now
		model.getChatProperty().addListener((observer, oldValue, newValue) -> {
			updateChatView(newValue);
		});
	}
	
//	private void readPlayerIndex() {
//		ArrayList<Player> players = this.gameState.getBoard().getPlayers();
//
//		for (int i = 0; i < players.size(); i++) {
//			if (players.get(i).getUsername().equals(Session.getCurrentUsername())) {
//				this.playerIndex = i;
//				break;
//			}
//		}
//	}
	
	
	@FXML
	private void btnSend_Click(ActionEvent event) {
		//create object
		MessageDTO message = new MessageDTO();
		
		//get text,receiver,author from textarea and assign to object
		message.setMessage(txtMessage.getText());
		message.setReceiver(ChatMember.All);
		message.setAuthor(ChatMember.Player1); // needs to be updated...dont know how, yet???
		
				
		//check if whispering -> do this here or in model??
		model.checkIfWhisper(message);
		
		
		model.sendMessage(message);
		
				
		txtMessage.clear();
	}
	
	
	@FXML
	private void btnMinimize_Click(ActionEvent event) {
		this.getViewRoot().setVisible(false);
	}
	
	private void updateChatView(MessageDTO message) {
		Platform.runLater(() -> {
			txtChat.appendText(message.getMessage()+"\n"); //add author later
		});
	}
	
}
