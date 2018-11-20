package ch.fhnw.projectbois.game;

import java.net.URL;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.View;
import ch.fhnw.projectbois.components.chat.ChatController;
import ch.fhnw.projectbois.components.chat.ChatModel;
import ch.fhnw.projectbois.components.chat.ChatView;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

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

		// StackPane - new root
		StackPane stackpane = new StackPane();

		// Add game pane
		Parent game = this.root;
		stackpane.getChildren().add(game);

		// Add chat pane
		ChatController chatController = Controller.initMVC(ChatController.class, ChatModel.class, ChatView.class);
		AnchorPane chat = (AnchorPane) chatController.getViewRoot();
		stackpane.getChildren().add(chat);
		StackPane.setAlignment(chat, Pos.CENTER_RIGHT);
		chat.setPrefWidth(200);
		chat.setMaxWidth(200);

		// Set gridpane as new root
		this.root = stackpane;

		// Load CSS
		stackpane.setStyle("-fx-background-color: #CCFF99");
		chat.setStyle("-fx-background-color: yellow; -fx-width: 100px;");
		String displayCSS = this.getClass().getResource("GameView.css").toExternalForm();
		this.root.getStylesheets().add(displayCSS);
	}

}
