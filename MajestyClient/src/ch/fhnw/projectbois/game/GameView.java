package ch.fhnw.projectbois.game;

import java.net.URL;
import java.util.ArrayList;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.View;
import ch.fhnw.projectbois.components.chat.ChatController;
import ch.fhnw.projectbois.components.chat.ChatModel;
import ch.fhnw.projectbois.components.chat.ChatView;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.gameobjects.Player;
import ch.fhnw.projectbois.session.Session;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class GameView extends View<GameModel> {

	private GamePlayerContainer playerContainer = null;
	private ArrayList<GamePlayerContainer> opponentContainers = null;

	public GameView(GameModel model) {
		super(model);

		this.opponentContainers = new ArrayList<>();
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("GameView2.fxml");
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
		StackPane.setAlignment(chat, Pos.TOP_RIGHT);
		chat.setPrefWidth(200);
		chat.setMaxWidth(200);
		chat.setPrefHeight(ChatView.PREF_HEIGHT);
		chat.setMaxHeight(ChatView.PREF_HEIGHT);

		// Set gridpane as new root
		this.root = stackpane;
	}

	public void initGamePlayerContainers() {
		GameState gameState = model.getGameState();
		int playerIndex = model.getPlayerIndex();

		ArrayList<Player> players = gameState.getBoard().getPlayers();

		Player p = players.stream().filter(f -> f.getUsername().equals(Session.getCurrentUsername())).findFirst().get();
		this.playerContainer = new GamePlayerContainer();
		this.playerContainer.setUsername(p.getUsername());
		this.playerContainer.setLblUsername((Label) this.root.lookup("lblName1"));
		this.playerContainer.setLblPoints((Label) this.root.lookup("lblPoints1"));
		this.playerContainer.setLblMeeples((Label) this.root.lookup("lblMeeples1"));

		// turn clockwise -> player to left after current player
		int opponentNr = 2;
		for (int i = playerIndex + 1; i < players.size(); i++) {
			Player player = players.get(i);
			this.initGamePlayerContainerOpponent(opponentNr, player);
			opponentNr++;
		}

		for (int i = 0; i < playerIndex; i++) {
			Player player = players.get(i);
			this.initGamePlayerContainerOpponent(opponentNr, player);
			opponentNr++;
		}
	}

	public void initGamePlayerContainerOpponent(int opponentNr, Player player) {
		GamePlayerContainer container = new GamePlayerContainer();
		container.setUsername(player.getUsername());
		container.setLblUsername((Label) this.root.lookup("lblName" + opponentNr));
		container.setLblPoints((Label) this.root.lookup("lblPoints" + opponentNr));
		container.setLblMeeples((Label) this.root.lookup("lblMeeples" + opponentNr));
		
		this.opponentContainers.add(container);
	}
	
	public void initLocations() {
		if(model.getGameState().isCardSideA()) {
			this.root.getStylesheets().add("CSSA");
		} else {
			this.root.getStylesheets().add("CSS_B");
		}
	}

	// GETTER AND SETTER

	public GamePlayerContainer getPlayerContainer() {
		return playerContainer;
	}

	public void setPlayerContainer(GamePlayerContainer playerContainer) {
		this.playerContainer = playerContainer;
	}

	public ArrayList<GamePlayerContainer> getOpponentContainers() {
		return opponentContainers;
	}

	public void setOpponentContainers(ArrayList<GamePlayerContainer> opponentContainers) {
		this.opponentContainers = opponentContainers;
	}

}
