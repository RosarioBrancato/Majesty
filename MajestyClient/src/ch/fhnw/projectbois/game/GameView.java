
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
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * The Class GameView.
 * 
 * @author Rosario Brancato
 */
public class GameView extends View<GameModel> {

	private GamePlayerContainer playerContainer = null;
	private ArrayList<GamePlayerContainer> opponentContainers = null;

	/**
	 * Instantiates a new game view.
	 *
	 * @param model the model
	 */
	public GameView(GameModel model) {
		super(model);

		this.opponentContainers = new ArrayList<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.fhnw.projectbois._mvc.View#getFXML()
	 */
	@Override
	protected URL getFXML() {
		return this.getClass().getResource("GameView.fxml");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.fhnw.projectbois._mvc.View#loadRoot(ch.fhnw.projectbois._mvc.Controller)
	 */
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
		chatController.closeChat();

		// Set gridpane as new root
		this.root = stackpane;
	}

	/**
	 * Inits the game player containers.
	 */
	public void initGamePlayerContainers() {
		GameState gameState = model.getGameState();
		int playerIndex = model.getPlayerIndex();

		ArrayList<Player> players = gameState.getBoard().getPlayers();

		Player currentPlayer = players.stream().filter(f -> f.getUsername().equals(Session.getCurrentUsername()))
				.findFirst().get();

		this.initGamePlayerContainerOpponent(1, currentPlayer, true);

		// turn clockwise -> player to left after current player
		int opponentNr = 2;
		for (int i = playerIndex + 1; i < players.size(); i++) {
			Player player = players.get(i);
			this.initGamePlayerContainerOpponent(opponentNr, player, false);
			opponentNr++;
		}

		for (int i = 0; i < playerIndex; i++) {
			Player player = players.get(i);
			this.initGamePlayerContainerOpponent(opponentNr, player, false);
			opponentNr++;
		}

		Platform.runLater(() -> {
			// Player count
			// removeIf:
			// https://stackoverflow.com/questions/23002532/javafx-2-how-do-i-delete-a-row-or-column-in-gridpane
			int playerCount = gameState.getBoard().getPlayers().size();
			GridPane pnlField = (GridPane) this.root.lookup("#pnlField");

			if (playerCount == 3) {
				pnlField.getRowConstraints().get(3).setPercentHeight(-1);
				pnlField.getRowConstraints().get(2).setPercentHeight(18);
				pnlField.getRowConstraints().get(1).setPercentHeight(18);
				pnlField.getChildren()
						.removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == 3);

			} else if (playerCount == 2) {
				pnlField.getRowConstraints().get(3).setPercentHeight(-1);
				pnlField.getRowConstraints().get(2).setPercentHeight(-1);
				pnlField.getRowConstraints().get(1).setPercentHeight(36);
				pnlField.getChildren()
						.removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == 3);
				pnlField.getChildren()
						.removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == 2);
			}
		});
	}

	/**
	 * Inits the game player container opponent.
	 *
	 * @param opponentNr      the opponent nr
	 * @param player          the player
	 * @param isCurrentPlayer the is current player
	 */
	public void initGamePlayerContainerOpponent(int opponentNr, Player player, boolean isCurrentPlayer) {
		GamePlayerContainer container = new GamePlayerContainer();
		container.setUsername(player.getUsername());

		Label lblName = (Label) this.root.lookup("#lblName" + opponentNr);
		Label lblPoints = (Label) this.root.lookup("#lblPoints" + opponentNr);
		Label lblMeeples = (Label) this.root.lookup("#lblMeeples" + opponentNr);

		container.setLblUsername(lblName);
		container.setLblPoints(lblPoints);
		container.setLblMeeples(lblMeeples);

		if (isCurrentPlayer) {
			container.setPlayerRow(5);
			this.playerContainer = container;
		} else {
			container.setPlayerRow(opponentNr - 1);
			this.opponentContainers.add(container);
		}

		Platform.runLater(() -> {
			lblName.setText(player.getUsername());
			lblPoints.setText(translator.getTranslation("lbl_Points", player.getPoints()));
			lblMeeples.setText(translator.getTranslation("lbl_Meeples", player.getMeeples()));
		});
	}

	/**
	 * Inits the locations.
	 */
	public void initLocations() {
		String cssFileName;

		if (model.getGameState().isCardSideA()) {
			cssFileName = "GameViewSideA.css";
		} else {
			cssFileName = "GameViewSideB.css";
		}

		Platform.runLater(() -> {
			String css = this.getClass().getResource(cssFileName).toExternalForm();
			this.root.getStylesheets().add(css);
		});
	}

	/**
	 * Sets the highlighting for the player, who has his current turn. 
	 */
	public void highlightPlayerTurn() {
		GameState gameState = model.getGameState();
		int pTurn = gameState.getPlayersTurn();

		if (pTurn == model.getPlayerIndex()) {
			this.setBorderColorOfPlayer(this.playerContainer.getPlayerRow(), true);
			for (GamePlayerContainer gamePlayerContainer : opponentContainers) {
				this.setBorderColorOfPlayer(gamePlayerContainer.getPlayerRow(), false);
			}

		} else {
			Player player = gameState.getBoard().getPlayers().get(pTurn);

			this.setBorderColorOfPlayer(this.playerContainer.getPlayerRow(), false);
			for (GamePlayerContainer gamePlayerContainer : opponentContainers) {
				this.setBorderColorOfPlayer(gamePlayerContainer.getPlayerRow(),
						gamePlayerContainer.getUsername().equals(player.getUsername()));
			}
		}
	}

	/**
	 * Sets the border color for the player'r row in the main grid pane.
	 * @param row
	 * @param isTurn
	 */
	private void setBorderColorOfPlayer(int row, boolean isTurn) {
		GridPane pnlField = (GridPane) this.root.lookup("#pnlField");

		pnlField.getChildren().forEach(f -> {
			if (GridPane.getRowIndex(f) != null && GridPane.getRowIndex(f) == row) {
				if (isTurn) {
					if (!f.getStyleClass().contains("pnlRowPlayersTurn")) {
						f.getStyleClass().add("pnlRowPlayersTurn");
					}

				} else if (f.getStyleClass().contains("pnlRowPlayersTurn")) {
					f.getStyleClass().remove("pnlRowPlayersTurn");
				}
			}
		});
	}

	// GETTER AND SETTER

	/**
	 * Gets the player container.
	 *
	 * @return the player container
	 */
	public GamePlayerContainer getPlayerContainer() {
		return playerContainer;
	}

	/**
	 * Sets the player container.
	 *
	 * @param playerContainer the new player container
	 */
	public void setPlayerContainer(GamePlayerContainer playerContainer) {
		this.playerContainer = playerContainer;
	}

	/**
	 * Gets the opponent containers.
	 *
	 * @return the opponent containers
	 */
	public ArrayList<GamePlayerContainer> getOpponentContainers() {
		return opponentContainers;
	}

	/**
	 * Sets the opponent containers.
	 *
	 * @param opponentContainers the new opponent containers
	 */
	public void setOpponentContainers(ArrayList<GamePlayerContainer> opponentContainers) {
		this.opponentContainers = opponentContainers;
	}

}
