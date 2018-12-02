package ch.fhnw.projectbois.game;

import java.net.URL;
import java.util.ArrayList;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.components.Component;
import ch.fhnw.projectbois.components.ComponentLoader;
import ch.fhnw.projectbois.fxml.FXMLUtils;
import ch.fhnw.projectbois.game.splitcardchooser.SplitCardChooserController;
import ch.fhnw.projectbois.game.splitcardchooser.SplitCardChooserModel;
import ch.fhnw.projectbois.game.splitcardchooser.SplitCardChooserView;
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

	private GamePlayerContainer player = null;
	private ArrayList<GamePlayerContainer> opponents = null;

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

	public GameController(GameModel model, GameView view) {
		super(model, view);

		this.opponents = new ArrayList<>();
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

	private void loadGameState(GameState gameState) {
		boolean firstLoading = this.gameState == null;

		if (this.gameState == null || (this.gameState != null && gameState.getId() > this.gameState.getId())) {
			this.gameState = gameState;

			if (firstLoading) {
				this.readPlayerIndex();
				this.initGamePlayerContainers();
				this.initLocations();
			}

			this.drawGui();
		}
	}

	// SET UP

	private void readPlayerIndex() {
		ArrayList<Player> players = this.gameState.getBoard().getPlayers();

		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getUsername().equals(Session.getCurrentUsername())) {
				this.playerIndex = i;
				break;
			}
		}
	}

	private void initGamePlayerContainers() {
		ArrayList<Player> players = this.gameState.getBoard().getPlayers();

		Player p = players.stream().filter(f -> f.getUsername().equals(Session.getCurrentUsername())).findFirst().get();
		this.player = new GamePlayerContainer();
		this.player.setUsername(p.getUsername());
		this.player.setPnlCards(this.pnlPlayer1Cards);
		this.player.setLblInfo(this.lblPlayerInfo);

		// turn clockwise -> player to left after current player
		int opponentIndex = 0;
		for (int i = this.playerIndex + 1; i < players.size(); i++) {
			Player player = players.get(i);
			this.initGamePlayerContainerOpponent(opponentIndex, player);
			opponentIndex++;
		}

		for (int i = 0; i < this.playerIndex; i++) {
			Player player = players.get(i);
			this.initGamePlayerContainerOpponent(opponentIndex, player);
			opponentIndex++;
		}
	}

	private void initGamePlayerContainerOpponent(int index, Player player) {
		GamePlayerContainer container = new GamePlayerContainer();
		container.setUsername(player.getUsername());

		URL url = ComponentLoader.getResource(Component.PlayerField);
		GridPane gridPane = (GridPane) FXMLUtils.loadFXML(url);
		Label lblInfo = (Label) gridPane.getChildren().get(0);

		container.setPnlCards(gridPane);
		container.setLblInfo(lblInfo);
		this.opponents.add(container);

		Integer i = new Integer(index);
		Platform.runLater(() -> {
			this.pnlOpponents.add(gridPane, i, 0);
		});
	}

	private void initLocations() {
		int playersCount = this.gameState.getBoard().getPlayers().size();

		Platform.runLater(() -> {
			if (playersCount == 3) {
				this.pnlOpponents.getColumnConstraints().remove(2);
				this.pnlOpponents.getColumnConstraints().get(0).setPercentWidth(50);
				this.pnlOpponents.getColumnConstraints().get(1).setPercentWidth(50);

			} else if (playersCount == 2) {
				this.pnlOpponents.getColumnConstraints().remove(2);
				this.pnlOpponents.getColumnConstraints().remove(1);
				this.pnlOpponents.getColumnConstraints().get(0).setPercentWidth(100);
			}
		});

		this.fillGamePlayerComponent(this.player, true);

		for (GamePlayerContainer opponent : this.opponents) {
			this.fillGamePlayerComponent(opponent, false);
		}
	}

	private void fillGamePlayerComponent(GamePlayerContainer container, boolean isPlayer) {
		int playersCount = this.gameState.getBoard().getPlayers().size();

		int sideChange = 0;
		if (!this.gameState.isCardSideA()) {
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
			Label label = new Label("Cards: 0");

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

	// MAIN METHODS

	private void drawGui() {
		Board board = this.gameState.getBoard();

		this.loadGameInfos();
		this.drawDisplay(board.getDisplay());
	}

	private void loadGameInfos() {
		ArrayList<Player> players = this.gameState.getBoard().getPlayers();

		Integer round = new Integer(this.gameState.getRound());
		int playersTurn = this.gameState.getPlayersTurn();
		String username = new String(players.get(playersTurn).getUsername());
		Platform.runLater(() -> {
			this.lblGameInfo.setText("Round: " + round + "/12 | Player's turn: " + username);
		});

		{
			Player player = players.stream().filter(f -> f.getUsername().equals(this.player.getUsername())).findFirst()
					.get();

			boolean isStartingPlayer = players.get(this.gameState.getStartPlayerIndex()).getUsername()
					.equals(Session.getCurrentUsername());

			this.loadGameInfoPlayer(player, this.player, isStartingPlayer);
		}

		for (GamePlayerContainer opponent : this.opponents) {
			Player player = players.stream().filter(f -> f.getUsername().equals(opponent.getUsername())).findFirst()
					.get();

			boolean isStartingPlayer = players.get(this.gameState.getStartPlayerIndex()).getUsername()
					.equals(opponent.getUsername());

			this.loadGameInfoPlayer(player, opponent, isStartingPlayer);
		}
	}

	private void loadGameInfoPlayer(Player player, GamePlayerContainer container, boolean isStartingPlayer) {
		String text = player.getUsername() + " | Meeples: " + player.getMeeples() + " | Points: " + player.getPoints();

		if (isStartingPlayer) {
			text += " | 1st";
		}

		String toUpdate = new String(text);
		Platform.runLater(() -> {
			container.getLblInfo().setText(toUpdate);

			for (int i = 0; i < player.getLocations().length; i++) {
				String locationText = "Cards: ";
				locationText += String.valueOf(player.getLocationByIndex(i).getCards().size());
				container.getLabelCardCountByIndex(i).setText(locationText);
			}
		});
	}

	private void drawDisplay(ArrayList<Card> displayCards) {
		Platform.runLater(() -> {
			this.pnlDisplay.getChildren().clear();
		});

		Player currentPlayer = this.gameState.getBoard().getPlayers().stream()
				.filter(f -> f.getUsername().equals(Session.getCurrentUsername())).findFirst().get();

		int col = 0;
		for (int i = 0; i < displayCards.size(); i++) {
			Card card = displayCards.get(i);

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
			boolean allowMove = allowMove();
			if (currentPlayer.getMeeples() >= i && allowMove) {
				vbox.getStyleClass().add("displayToHover");
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
				GameMove move = new GameMove();
				move.setDisplayCardIndexSelected(index);

				// TO-DO: additional decision: split cards, split card revival
				// move.setDecision1(-1);
				// move.setDecision2(-1);

				// handle split card
				Card card = gameState.getBoard().getDisplay().get(index);
				if (card.isSplitCard()) {
					int decision = showSplitCardChooser(card);
					move.getDecisions().add(decision);
				}

				// handle witch
				if (card.getCardTypeActive() == CardType.Witch) {
					Location infirmary = gameState.getBoard().getPlayers().get(playerIndex)
							.getLocationByIndex(Location.INFIRMARY);
					int cardCount = infirmary.getCards().size();
					if (cardCount > 0) {
						Card toRevive = infirmary.getCards().get(cardCount - 1);

						if (toRevive.isSplitCard()) {
							int decision = showSplitCardChooser(toRevive);
							move.getDecisions().add(decision);
						}
					}

					// handle noble points/meeples trade
				} else if (card.getCardTypeActive() == CardType.Noble && !gameState.isCardSideA()) {
					// TO-DO
				}

				model.sendMove(move);
			}
		});
	}

	private int showSplitCardChooser(Card splitCard) {
		int decision = -1;

		if (splitCard.isSplitCard()) {
			SplitCardChooserController controller = Controller.initMVC(SplitCardChooserController.class,
					SplitCardChooserModel.class, SplitCardChooserView.class);

			controller.loadSplitCard(splitCard);
			controller.showAndWait();

			decision = controller.getDecision();
		}

		return decision;
	}

	private boolean allowMove() {
		int playersTurn = this.gameState.getPlayersTurn();
		Player player = this.gameState.getBoard().getPlayers().get(playersTurn);

		return player.getUsername().equals(Session.getCurrentUsername()) && !this.gameState.isGameEnded();
	}

	// EVENT METHODS

	@FXML
	private void btnLeave_Click(ActionEvent e) {

	}

}
