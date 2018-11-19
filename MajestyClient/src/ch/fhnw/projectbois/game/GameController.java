package ch.fhnw.projectbois.game;

import java.util.ArrayList;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.gameobjects.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class GameController extends Controller<GameModel, GameView> {

	private GameResourceHelper resourceHelper = null;

	private int playerId = -1;
	private GameState gameState = null;

	private Label[] locationCardCount = null;

	@FXML
	private Label lblGameInfo;

	@FXML
	private Label lblPlayerInfo;

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
		if (!this.gameState.isCardSideA()) {
			sideChange = 10;
		}

		this.locationCardCount = new Label[8];

		for (int i = 0; i < 8; i++) {
			ImageView imgLocation = new ImageView();
			imgLocation.setPreserveRatio(true);
			imgLocation.setFitHeight(175);

			Image image = resourceHelper.getLocationImage(i + 1 + sideChange);
			imgLocation.setImage(image);

			BorderPane borderPane = new BorderPane(imgLocation);

			this.locationCardCount[i] = new Label("Cards: 0");

			VBox box = new VBox();
			box.setAlignment(Pos.TOP_CENTER);
			box.getChildren().add(this.locationCardCount[i]);
			borderPane.setBottom(box);

			Integer col = new Integer(i);
			Platform.runLater(() -> {
				this.pnlPlayer1Cards.add(borderPane, col, 0);
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

		this.loadGameInfos();
		this.drawDisplay(board.getDisplay());
	}

	private void loadGameInfos() {
		Board board = this.gameState.getBoard();

		// Player currentPlayer = board.getPlayers().stream()
		// .filter(f -> f.getUserToken() ==
		// Session.getCurrentUserToken()).findFirst().get();

		// TEMP
		Player currentPlayer = board.getPlayers().get(0);

		Platform.runLater(() -> {
			this.lblPlayerInfo
					.setText("Meeples: " + currentPlayer.getMeeples() + " Points: " + currentPlayer.getScore());

			this.lblGameInfo.setText("Round: " + board.getRound() + "/12 Player's turn: " + board.getPlayersTurn());
		});

	}

	private void drawDisplay(ArrayList<DisplayCard> displayCards) {
		Platform.runLater(() -> {
			this.pnlDisplay.getChildren().clear();
		});

		int col = 0;
		for (DisplayCard card : displayCards) {
			ImageView imgCard = new ImageView();
			imgCard.setPreserveRatio(true);
			imgCard.setFitHeight(175);

			Image image = resourceHelper.getCardImage(card);
			imgCard.setImage(image);

			imgCard.setOnMouseClicked((e) -> {
				boolean allowMove = allowMove();
				if (allowMove) {
					logger.info("Image clicked!");
					model.sendMove(new GameMove());
				}
			});

			VBox vbox = new VBox();
			vbox.getStyleClass().add("display");
			vbox.setAlignment(Pos.CENTER);
			vbox.getChildren().add(imgCard);
			vbox.getChildren().add(new Label("Meeples: " + card.getMeeples()));

			Integer colIndex = new Integer(col);
			Platform.runLater(() -> {
				this.pnlDisplay.add(vbox, colIndex, 0);
			});
			col++;
		}

		// Deck
		ImageView imgDeck = new ImageView();
		imgDeck.setPreserveRatio(true);
		imgDeck.setFitHeight(175);

		int deckBack = this.gameState.getBoard().getDeckBack();
		Image image = this.resourceHelper.getDeckBackImage(deckBack);
		imgDeck.setImage(image);

		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().add(imgDeck);
		vbox.getChildren().add(new Label("Cards left: --"));

		Platform.runLater(() -> {
			this.pnlDisplay.add(vbox, 6, 0);
		});
	}

	private boolean allowMove() {
		return this.gameState.getBoard().getPlayersTurn() == this.playerId;
	}

}
