package ch.fhnw.projectbois.game;

import java.net.URL;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.View;
import ch.fhnw.projectbois.components.chat.ChatController;
import ch.fhnw.projectbois.components.chat.ChatModel;
import ch.fhnw.projectbois.components.chat.ChatView;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class GameView extends View<GameModel> {

	public GameView(GameModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("GameView.fxml");
	}
	
	@Override
	public <T extends Controller<GameModel, ? extends View<GameModel>>> void loadRoot(T controller) {
		super.loadRoot(controller);
		
		Parent root = this.root;
		
		BorderPane boarderPane = new BorderPane();
		boarderPane.setCenter(root);
		boarderPane.setRight(Controller.initMVC(ChatController.class, ChatModel.class, ChatView.class).getViewRoot());
		
		this.root = boarderPane;
	}

}
