package ch.fhnw.projectbois.lobby;

import java.net.URL;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.View;
import ch.fhnw.projectbois.components.chat.ChatController;
import ch.fhnw.projectbois.components.chat.ChatModel;
import ch.fhnw.projectbois.components.chat.ChatView;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * The Class LobbyView.
 * @author Dario Stoeckli
 */
public class LobbyView extends View<LobbyModel> {

	/**
	 * Instantiates a new lobby view.
	 *
	 * @param model the model
	 */
	public LobbyView(LobbyModel model) {
		super(model);
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.View#getFXML()
	 */
	@Override
	protected URL getFXML() {
		return this.getClass().getResource("LobbyView.fxml");
	}
	
	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.View#loadRoot(ch.fhnw.projectbois._mvc.Controller)
	 * Additionally create a stackpane to accommodate the Chat MVC
	 */
	@Override
	public <T extends Controller<LobbyModel, ? extends View<LobbyModel>>> void loadRoot(T controller) {
		super.loadRoot(controller);
		
		// StackPane - new root
		StackPane stackpane = new StackPane();
		
		//Add lobby pane
		Parent game = this.root;
		stackpane.getChildren().add(game);
		
		// Add chat pane
		ChatController chatController = Controller.initMVC(ChatController.class, ChatModel.class, ChatView.class);
		AnchorPane chat = (AnchorPane) chatController.getViewRoot();
		stackpane.getChildren().add(chat);
		StackPane.setAlignment(chat, Pos.TOP_RIGHT);
		chat.setPrefWidth(200);
		chat.setMaxWidth(200);
		chatController.setPrefHeightOpen(Control.USE_COMPUTED_SIZE);

		// Set gridpane as new root
		this.root = stackpane;
	}

}
