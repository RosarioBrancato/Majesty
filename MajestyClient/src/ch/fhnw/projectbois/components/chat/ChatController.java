package ch.fhnw.projectbois.components.chat;

import ch.fhnw.projectbois._mvc.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ChatController extends Controller<ChatModel, ChatView> {
	
	public ChatController(ChatModel model, ChatView view) {
		super(model, view);
	}
	
	@FXML
	private TextArea txtChat;
	
	@FXML
	private TextArea txtMessage;
	
	@FXML
	private Button btnSend;
	
	@FXML
	private Button btnClose;
	
	@FXML
	private void btnSend_Click(ActionEvent event) {
		System.out.println("msg sent...");

	}
	
	@FXML
	private void btnMinimize_Click(ActionEvent event) {
		
	}
	
}
