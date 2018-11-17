package ch.fhnw.projectbois.game;

import java.net.URL;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.View;
import ch.fhnw.projectbois.components.chat.ChatController;
import ch.fhnw.projectbois.components.chat.ChatModel;
import ch.fhnw.projectbois.components.chat.ChatView;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
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
		
		BorderPane borderPane = new BorderPane();
		borderPane.prefWidthProperty().bind(MetaContainer.getInstance().getScene().widthProperty());
		borderPane.setStyle("-fx-background-color: #CCFF99");

		Parent game = this.root;
		borderPane.setCenter(game);
		
		AnchorPane chat = (AnchorPane)Controller.initMVC(ChatController.class, ChatModel.class, ChatView.class).getViewRoot();
		chat.prefHeightProperty().bind(borderPane.heightProperty());
		chat.setStyle("-fx-background-color: yellow; -fx-width: 100px;");
		borderPane.setRight(chat);
		
		this.root = borderPane;

		String displayCSS = this.getClass().getResource("GameView.css").toExternalForm();
		this.root.getStylesheets().add(displayCSS);
	}

}
