package ch.fhnw.projectbois.game;

import java.util.ArrayList;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.gameobjects.*;
import ch.fhnw.projectbois.session.Session;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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

	private int playerIndex = -1;
	private GameState gameState = null;

	private Label[] locationCardCount = null;

	@FXML
	private Label lblGameInfo;

	@FXML
	private GridPane pnlDisplay;

	@FXML
	private Label lblPlayerInfo;

	@FXML
	private GridPane pnlPlayer1Cards;

	@FXML
	private GridPane pnlOpponents;

	@FXML
	private Label lblOpponent1Info;

	@FXML
	private GridPane pnlOpponent1Cards;

	@FXML
	private Label lblOpponent2Info;

	@FXML
	private GridPane pnlOpponent2Cards;

	@FXML
	private Label lblOpponent3Info;

	@FXML
	private GridPane pnlOpponent3Cards;

	public GameController(GameModel model, GameView view) {
		super(model, view);

		this.resourceHelper = new GameResourceHelper();
	}

	@Override
	protected void initialize() {
		super.initialize();

		this.pnlPlayer1Cards.getStyleClass().add("playersCards");
		this.pnlOpponents.getStyleClass().add("opponents");

		this.model.getGameStateProperty().addListener((observer, oldValue, newValue) -> {
			loadGameState(newValue);
		});

		this.model.getGameState();
	}

	@FXML
	private void btnLeave_Click(ActionEvent e) {

	}

	private void readPlayerIndex() {
		ArrayList<Player> players = this.gameState.getBoard().getPlayers();

		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getUserToken() == Session.getCurrentUserToken()) {
				this.playerIndex = i;
			}
		}
	}

	private void initLocations() {
		int playersCount = this.gameState.getBoard().getPlayers().size();

		Platform.runLater(() -> {
			if (playersCount == 3) {
				this.pnlOpponents.getChildren().remove(this.pnlOpponent3Cards);
				this.pnlOpponents.getColumnConstraints().remove(2);
				this.pnlOpponents.getColumnConstraints().get(0).setPercentWidth(50);
				this.pnlOpponents.getColumnConstraints().get(1).setPercentWidth(50);

			} else if (playersCount == 2) {
				this.pnlOpponents.getChildren().remove(this.pnlOpponent3Cards);
				this.pnlOpponents.getChildren().remove(this.pnlOpponent2Cards);
				this.pnlOpponents.getColumnConstraints().remove(2);
				this.pnlOpponents.getColumnConstraints().remove(1);
				this.pnlOpponents.getColumnConstraints().get(0).setPercentWidth(100);
			}
		});

		int sideChange = 0;
		if (!this.gameState.isCardSideA()) {
			sideChange = Location.OFFSET_B;
		}
		this.locationCardCount = new Label[8 * playersCount];

		this.initLocationsForPlayer(0, sideChange, this.pnlPlayer1Cards);
		if (playersCount >= 2) {
			this.initLocationsForPlayer(8, sideChange, this.pnlOpponent1Cards);
		}
		if (playersCount >= 3) {
			this.initLocationsForPlayer(16, sideChange, this.pnlOpponent2Cards);
		}
		if (playersCount == 4) {
			this.initLocationsForPlayer(24, sideChange, this.pnlOpponent3Cards);
		}
	}

	private void initLocationsForPlayer(int playerStartRange, int sideChange, GridPane gridPaneToUpdate) {
		int playersCount = this.gameState.getBoard().getPlayers().size();

		for (int i = 0; i < 8; i++) {
			int idxOffset = i + playerStartRange;

			ImageView imgLocation = new ImageView();
			imgLocation.setPreserveRatio(true);
			if (playerStartRange == 0) {
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

			this.locationCardCount[idxOffset] = new Label("Cards: 0");

			VBox box = new VBox();
			box.setAlignment(Pos.CENTER);
			box.getChildren().add(this.locationCardCount[idxOffset]);
			borderPane.setBottom(box);

			Integer col = new Integer(i);
			Platform.runLater(() -> {
				gridPaneToUpdate.add(borderPane, col, 0);
			});
		}
	}

	private void loadGameState(GameState gameState) {
		boolean firstLoading = this.gameState == null;

		if (this.gameState == null || gameState.getId() > this.gameState.getId()) {
			this.gameState = gameState;

			if (firstLoading) {
				this.readPlayerIndex();
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

		Platform.runLater(() -> {
			this.lblGameInfo.setText("Round: " + board.getRound() + "/12 | Player's turn: " + board.getPlayersTurn());
		});

		for (int i = 0; i < board.getPlayers().size(); i++) {
			Player player = board.getPlayers().get(i);
			String text = player.getUsername() + " | Meeples: " + player.getMeeples() + " | Points: "
					+ player.getScore();

			if (i == 0) {
				Platform.runLater(() -> {
					this.lblPlayerInfo.setText(text);
				});
			} else if (i == 1) {
				Platform.runLater(() -> {
					this.lblOpponent1Info.setText(player.getUsername() + " | " + text);
				});
			} else if (i == 2) {
				Platform.runLater(() -> {
					this.lblOpponent2Info.setText(player.getUsername() + " | " + text);
				});
			} else if (i == 3) {
				Platform.runLater(() -> {
					this.lblOpponent3Info.setText(player.getUsername() + " | " + text);
				});
			}
		}
	}

	private void drawDisplay(ArrayList<DisplayCard> displayCards) {
		Platform.runLater(() -> {
			this.pnlDisplay.getChildren().clear();
		});

		// TEMP
		Player currentPlayer = this.gameState.getBoard().getPlayers().get(0);

		int col = 0;
		for (int i = 0; i < displayCards.size(); i++) {
			DisplayCard card = displayCards.get(i);

			ImageView imgCard = new ImageView();
			imgCard.setPreserveRatio(true);
			imgCard.setFitHeight(175);

			Image image = resourceHelper.getCardImage(card);
			imgCard.setImage(image);

			VBox vbox = new VBox();
			vbox.getStyleClass().add("display");
			vbox.setAlignment(Pos.CENTER);
			vbox.getChildren().add(imgCard);
			vbox.getChildren().add(new Label("Meeples: " + card.getMeeples()));

			// click event
			if (currentPlayer.getMeeples() >= i) {
				vbox.getStyleClass().add("displayToHover");

				imgCard.setOnMouseClicked((e) -> {
					boolean allowMove = allowMove();
					if (allowMove) {
						logger.info("Image clicked!");
						model.sendMove(new GameMove());
					}
				});
				this.addDisplayClickEvent(imgCard, i);
			}

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
		vbox.getChildren().add(new Label("Cards left: " + this.gameState.getBoard().getCardsLeft()));

		Platform.runLater(() -> {
			this.pnlDisplay.add(vbox, 6, 0);
		});
	}

	private void addDisplayClickEvent(ImageView imgCard, int displayIndex) {
		imgCard.setOnMouseClicked(new EventHandler<Event>() {

			int index = displayIndex;

			@Override
			public void handle(Event event) {
				boolean allowMove = allowMove();
				if (allowMove) {
					GameMove move = new GameMove();
					move.setDisplayCardIndexSelected(index);
					
					//TO-DO: additional decision: split cards, split card revival
					//move.setDecision1(-1);
					//move.setDecision2(-1);
					
					model.sendMove(move);
				}
			}
		});
	}

	private boolean allowMove() {
		return this.gameState.getBoard().getPlayersTurn() == this.playerIndex;
	}

}
