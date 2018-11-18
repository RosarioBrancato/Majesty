package ch.fhnw.projectbois.game;

import java.util.ArrayList;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.gameobjects.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class GameController extends Controller<GameModel, GameView> {

	private GameResourceHelper resourceHelper = null;
	
	private int playerId = -1;
	private GameState gameState = null;

	@FXML
	private GridPane pnlDisplay;

	@FXML
	private GridPane pnlPlayer1Cards;

	public GameController(GameModel model, GameView view) {
		super(model, view);

		this.resourceHelper = new GameResourceHelper();
	}

	@Override
	protected void initialize() {
		super.initialize();

		this.pnlPlayer1Cards.getStyleClass().add("playersCards");

		this.model.getGameStateProperty().addListener((observer, oldValue, newValue) -> {
			loadGameState(newValue);
		});

		this.model.getGameState();
	}

	@FXML
	private void btnLeave_Click(ActionEvent e) {

	}

	private void initLocations() {
		int sideChange = 0;
		if(!this.gameState.isCardSideA()) {
			sideChange = 10;
		}
		
		for (int i = 0; i < 8; i++) {
			ImageView imgLocation = new ImageView();
			imgLocation.setPreserveRatio(true);
			imgLocation.setFitHeight(150);
			
			Image image = resourceHelper.getLocationImage(i + 1 + sideChange);
			imgLocation.setImage(image);

			Integer col = new Integer(i);
			Platform.runLater(() -> {
				this.pnlPlayer1Cards.add(imgLocation, col, 0);
			});
		}
	}

	private void loadGameState(GameState gameState) {
		boolean firstLoading = this.gameState == null;

		if (this.gameState == null || gameState.getId() > this.gameState.getId()) {
			this.gameState = gameState;

			if (firstLoading) {
				this.initLocations();
			}

			this.drawGui();
		}
	}

	private void drawGui() {
		Board board = this.gameState.getBoard();

		this.drawDisplay(board.getDisplay());
	}

	private void drawDisplay(ArrayList<DisplayCard> displayCards) {
		Platform.runLater(() -> {
			this.pnlDisplay.getChildren().clear();
		});

		int col = 0;
		for (DisplayCard card : displayCards) {
			ImageView imgCard = new ImageView();
			imgCard.setPreserveRatio(true);
			imgCard.setFitHeight(150);
			
			Image image = resourceHelper.getCardImage(card);
			imgCard.setImage(image);

			imgCard.setOnMouseClicked((e) -> {
				boolean allowMove = allowMove();
				if (allowMove) {
					logger.info("Image clicked!");
					model.sendMove(new GameMove());
				}
			});

			BorderPane borderPane = new BorderPane(imgCard);
			borderPane.getStyleClass().add("display");

			Integer colIndex = new Integer(col);
			Platform.runLater(() -> {
				this.pnlDisplay.add(borderPane, colIndex, 0);
			});
			col++;
		}

		// Deck
		ImageView imgDeck = new ImageView();
		imgDeck.setPreserveRatio(true);
		imgDeck.setFitHeight(150);

		int deckBack = this.gameState.getBoard().getDeckBack();
		Image image = this.resourceHelper.getDeckBackImage(deckBack);
		imgDeck.setImage(image);

		BorderPane borderPane = new BorderPane(imgDeck);

		Platform.runLater(() -> {
			this.pnlDisplay.add(borderPane, 6, 0);
		});
	}

	private boolean allowMove() {
		return this.gameState.getBoard().getPlayersTurn() == this.playerId;
	}

}
