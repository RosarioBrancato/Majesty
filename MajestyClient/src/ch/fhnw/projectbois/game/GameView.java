package ch.fhnw.projectbois.game;

import java.net.URL;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.View;
import ch.fhnw.projectbois.components.chat.ChatController;
import ch.fhnw.projectbois.components.chat.ChatModel;
import ch.fhnw.projectbois.components.chat.ChatView;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

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

		// GridPane - new root
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setPrefWidth(Control.USE_COMPUTED_SIZE);
		gridPane.setPrefHeight(Control.USE_COMPUTED_SIZE);
		gridPane.setStyle("-fx-background-color: #CCFF99");

		ColumnConstraints constraintGame = new ColumnConstraints();
		constraintGame.setPercentWidth(80);
		gridPane.getColumnConstraints().add(constraintGame);

		ColumnConstraints constraintChat = new ColumnConstraints();
		constraintChat.setPercentWidth(20);
		gridPane.getColumnConstraints().add(constraintChat);

		RowConstraints rowContraint = new RowConstraints();
		rowContraint.setPercentHeight(100);
		gridPane.getRowConstraints().add(rowContraint);

		// Add game pane
		Parent game = this.root;
		gridPane.add(game, 0, 0);

		// Add chat pane
		Parent chat = Controller.initMVC(ChatController.class, ChatModel.class, ChatView.class).getViewRoot();
		chat.setStyle("-fx-background-color: yellow; -fx-width: 100px;");
		gridPane.add(chat, 1, 0);

		//Set gridpane as new root
		this.root = gridPane;

		//Load CSS
		String displayCSS = this.getClass().getResource("GameView.css").toExternalForm();
		this.root.getStylesheets().add(displayCSS);
	}

}
