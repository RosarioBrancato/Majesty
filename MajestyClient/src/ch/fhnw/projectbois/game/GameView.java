package ch.fhnw.projectbois.game;

import java.net.URL;
import java.util.ArrayList;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.View;
import ch.fhnw.projectbois.components.Component;
import ch.fhnw.projectbois.components.ComponentLoader;
import ch.fhnw.projectbois.components.chat.ChatController;
import ch.fhnw.projectbois.components.chat.ChatModel;
import ch.fhnw.projectbois.components.chat.ChatView;
import ch.fhnw.projectbois.fxml.FXMLUtils;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.gameobjects.Location;
import ch.fhnw.projectbois.gameobjects.Player;
import ch.fhnw.projectbois.session.Session;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameView extends View<GameModel> {

	private GameResourceHelper resourceHelper = null;
	private GamePlayerContainer playerContainer = null;
	private ArrayList<GamePlayerContainer> opponentContainers = null;

	public GameView(GameModel model) {
		super(model);

		this.resourceHelper = new GameResourceHelper();
		this.opponentContainers = new ArrayList<>();
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
		StackPane.setAlignment(chat, Pos.TOP_RIGHT);
		chat.setPrefWidth(200);
		chat.setMaxWidth(200);
		chat.setPrefHeight(ChatView.PREF_HEIGHT);
		chat.setMaxHeight(ChatView.PREF_HEIGHT);

		// Set gridpane as new root
		this.root = stackpane;

		// Load CSS
		stackpane.setStyle("-fx-background-color: #CCFF99");
		chat.setStyle("-fx-background-color: yellow; -fx-width: 100px;");
		String displayCSS = this.getClass().getResource("GameView.css").toExternalForm();
		this.root.getStylesheets().add(displayCSS);
	}

	public void initGamePlayerContainers() {
		GameState gameState = model.getGameState();
		int playerIndex = model.getPlayerIndex();

		ArrayList<Player> players = gameState.getBoard().getPlayers();

		Player p = players.stream().filter(f -> f.getUsername().equals(Session.getCurrentUsername())).findFirst().get();
		this.playerContainer = new GamePlayerContainer();
		this.playerContainer.setUsername(p.getUsername());
		this.playerContainer.setPnlCards((GridPane) this.root.lookup("pnlPlayer1Cards"));
		this.playerContainer.setLblInfo((Label) this.root.lookup("lblPlayerInfo"));

		// turn clockwise -> player to left after current player
		int opponentIndex = 0;
		for (int i = playerIndex + 1; i < players.size(); i++) {
			Player player = players.get(i);
			this.initGamePlayerContainerOpponent(opponentIndex, player);
			opponentIndex++;
		}

		for (int i = 0; i < playerIndex; i++) {
			Player player = players.get(i);
			this.initGamePlayerContainerOpponent(opponentIndex, player);
			opponentIndex++;
		}
	}

	public void initGamePlayerContainerOpponent(int index, Player player) {
		GridPane pnlOpponents = (GridPane) this.root.lookup("pnlOpponents");

		GamePlayerContainer container = new GamePlayerContainer();
		container.setUsername(player.getUsername());

		URL url = ComponentLoader.getResource(Component.PlayerField);
		GridPane gridPane = (GridPane) FXMLUtils.loadFXML(url);
		Label lblInfo = (Label) gridPane.getChildren().get(0);

		container.setPnlCards(gridPane);
		container.setLblInfo(lblInfo);
		this.opponentContainers.add(container);

		Integer i = new Integer(index);
		Platform.runLater(() -> {
			pnlOpponents.add(gridPane, i, 0);
		});
	}
	
	public void initLocations() {
		int playersCount = model.getGameState().getBoard().getPlayers().size();
		GridPane pnlOpponents = (GridPane)this.root.lookup("pnlOpponents");
		
		Platform.runLater(() -> {
			if (playersCount == 3) {
				pnlOpponents.getColumnConstraints().remove(2);
				pnlOpponents.getColumnConstraints().get(0).setPercentWidth(50);
				pnlOpponents.getColumnConstraints().get(1).setPercentWidth(50);

			} else if (playersCount == 2) {
				pnlOpponents.getColumnConstraints().remove(2);
				pnlOpponents.getColumnConstraints().remove(1);
				pnlOpponents.getColumnConstraints().get(0).setPercentWidth(100);
			}
		});

		this.fillGamePlayerComponent(this.playerContainer, true);

		for (GamePlayerContainer opponent : this.opponentContainers) {
			this.fillGamePlayerComponent(opponent, false);
		}
	}

	private void fillGamePlayerComponent(GamePlayerContainer container, boolean isPlayer) {
		GameState gameState = model.getGameState();
		
		int playersCount = gameState.getBoard().getPlayers().size();

		int sideChange = 0;
		if (!gameState.isCardSideA()) {
			sideChange = Location.OFFSET_B;
		}

		for (int i = 0; i < 8; i++) {

			ImageView imgLocation = new ImageView();
			imgLocation.setPreserveRatio(true);

			if (isPlayer) {
				imgLocation.setFitHeight(175);
			} else if (playersCount == 2) {
				imgLocation.setFitHeight(150);
			} else if (playersCount == 3) {
				imgLocation.setFitWidth(70);
			} else if (playersCount == 4) {
				imgLocation.setFitWidth(45);
			}

			Image image = resourceHelper.getLocationImage(i + sideChange);
			imgLocation.setImage(image);

			BorderPane borderPane = new BorderPane(imgLocation);
			Label label = new Label(translator.getTranslation("lbl_Cards", 0));

			container.setLabelCardCountByIndex(i, label);

			VBox box = new VBox();
			box.setAlignment(Pos.CENTER);
			box.getChildren().add(label);
			borderPane.setBottom(box);

			Integer col = new Integer(i);
			Platform.runLater(() -> {
				container.getPnlCards().add(borderPane, col, 0);
			});
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
