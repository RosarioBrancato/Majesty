package ch.fhnw.projectbois.components.chat;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.dto.MessageDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;

/**
 * 
 * @author Leeroy Koller
 *
 */

public class ChatController extends Controller<ChatModel, ChatView> {
	
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
	
	@FXML
	private void btnSend_Click(ActionEvent event) {
		//create object
		MessageDTO message = new MessageDTO();
		
		//check if whispering -> do this here or in model??
//		if (txtMessage.getText().matches(null){
//			
//		}
		//get text from textarea and assign to object
		message.setMessage(txtMessage.getText());
		
		model.getChatProperty().addListener((observer, oldValue, newValue) -> {
			updateChatView(newValue);
		});

		model.getErrorProperty().addListener((observer, oldValue, newValue) -> {
			Platform.runLater(() -> {
				Alert dlg = new Alert(AlertType.ERROR);
				dlg.setTitle("ERROR");
				dlg.setHeaderText(null);
				dlg.setContentText("An error occured. Please try again.");
				
				dlg.initOwner(MetaContainer.getInstance().getMainStage());
				dlg.initModality(Modality.APPLICATION_MODAL);
				
				dlg.showAndWait();
				
				//getLobbies();
			});
		});
		
		
		
		
		model.sendMessage(message);
		
		txtMessage.clear();
		}
	
	
	@FXML
	private void btnMinimize_Click(ActionEvent event) {
		this.getViewRoot().setVisible(false);
	}
	
	private void updateChatView(MessageDTO post) {
		Platform.runLater(() -> {
			//txtChat.appendText(post);
		});
	}
	
}
