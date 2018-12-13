package ch.fhnw.projectbois.game;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.components.Component;
import ch.fhnw.projectbois.components.ComponentLoader;
import ch.fhnw.projectbois.components.menubar.MenuBarController;
import ch.fhnw.projectbois.components.menubar.MenuBarModel;
import ch.fhnw.projectbois.components.menubar.MenuBarView;
import ch.fhnw.projectbois.fxml.FXMLUtils;
import ch.fhnw.projectbois.game.meepletrader.MeepleTraderController;
import ch.fhnw.projectbois.game.meepletrader.MeepleTraderModel;
import ch.fhnw.projectbois.game.meepletrader.MeepleTraderView;
import ch.fhnw.projectbois.game.splitcardchooser.SplitCardChooserController;
import ch.fhnw.projectbois.game.splitcardchooser.SplitCardChooserModel;
import ch.fhnw.projectbois.game.splitcardchooser.SplitCardChooserView;
import ch.fhnw.projectbois.gameend.GameEndController;
import ch.fhnw.projectbois.gameend.GameEndModel;
import ch.fhnw.projectbois.gameend.GameEndView;
import ch.fhnw.projectbois.gameobjects.Board;
import ch.fhnw.projectbois.gameobjects.Card;
import ch.fhnw.projectbois.gameobjects.CardType;
import ch.fhnw.projectbois.gameobjects.GameMove;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.gameobjects.Location;
import ch.fhnw.projectbois.gameobjects.Player;
import ch.fhnw.projectbois.session.Session;
import ch.fhnw.projectbois.time.Time;
import ch.fhnw.projectbois.utils.DialogUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
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

	private Time turntimer = null;
	private ChangeListener<Number> turntimerPropertyListener = null;

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

		this.model.getGameEndProperty().addListener((observer, oldValue, newValue) -> {
			showGameEndView(gameState);
		});

		this.model.getGameState();
	}

	@Override
	public void destroy() {
		super.destroy();

		if (this.turntimer != null) {
			this.turntimer.stop();
			this.turntimer.getPeriodCounterProperty().removeListener(this.turntimerPropertyListener);
			this.turntimer = null;
		}
	}

	private void loadGameState(GameState gameState) {
		boolean firstLoading = this.gameState == null;

		if (this.gameState == null || (this.gameState != null && gameState.getId() > this.gameState.getId())) {
			this.gameState = gameState;

			if (firstLoading) {
				this.readPlayerIndex();
				this.initGamePlayerContainers();
				this.initLocations();

				this.turntimer = new Time();
				this.initTurntimerPropertyListener();
				this.turntimer.getPeriodCounterProperty().addListener(this.turntimerPropertyListener);
				this.turntimer.startCountdown(gameState.getTurntimer());
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

	private void initTurntimerPropertyListener() {
		this.turntimerPropertyListener = (observer, oldValue, newValue) -> {
			updateInfoBar();
		};
	}

	// MAIN METHODS

	private void drawGui() {
		Board board = this.gameState.getBoard();

		this.loadGameInfos();
		this.drawDisplay(board.getDisplay());
	}

	private void loadGameInfos() {
		ArrayList<Player> players = this.gameState.getBoard().getPlayers();

		int turntimer = this.gameState.getTurntimer();
		if (this.gameState.isGameEnded()) {
			this.turntimer.stop();
		} else {
			this.turntimer.setCounter(turntimer);
		}
		this.updateInfoBar();

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

	private void updateInfoBar() {
		ArrayList<Player> players = this.gameState.getBoard().getPlayers();

		Integer round = new Integer(this.gameState.getRound());
		int playersTurn = this.gameState.getPlayersTurn();
		String username = new String(players.get(playersTurn).getUsername());
		int timeleft = this.turntimer.getCounter();

		String textToDisplay = translator.getTranslation("lbl_Game_Info", round, username, timeleft);
		Platform.runLater(() -> {
			this.lblGameInfo.setText(textToDisplay);
		});
	}

	private void loadGameInfoPlayer(Player player, GamePlayerContainer container, boolean isStartingPlayer) {
		String text = translator.getTranslation("lbl_Player_Info", player.getUsername(), player.getMeeples(),
				player.getPoints());

		if (isStartingPlayer) {
			text += translator.getTranslation("lbl_Player_Info_First");
		}

		String toUpdate = new String(text);
		Platform.runLater(() -> {
			container.getLblInfo().setText(toUpdate);

			for (int i = 0; i < player.getLocations().length; i++) {
				int cards = player.getLocationByIndex(i).getCards().size();
				String locationText = translator.getTranslation("lbl_Cards", cards);
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
		boolean allowMove = isTurnPlayer();

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
			vbox.getChildren().add(new Label(translator.getTranslation("lbl_Meeples", card.getMeeples())));

			// click event
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
		vbox.getChildren()
				.add(new Label(translator.getTranslation("lbl_Cards_Left", this.gameState.getBoard().getCardsLeft())));

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

				// handle split card
				Card card = gameState.getBoard().getDisplay().get(index);
				if (card.isSplitCard()) {
					int decision = showSplitCardChooser(card,
							translator.getTranslation("lbl_SplitCardChooser_DisplayCard"));
					card.setActiveCardType(decision);
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
							int decision = showSplitCardChooser(toRevive,
									translator.getTranslation("lbl_SplitCardChooser_ReviveCard"));
							move.getDecisions().add(decision);
						}
					}

					// handle noble meeple trade
				} else if (card.getCardTypeActive() == CardType.Noble && !gameState.isCardSideA()) {
					Player currentPlayer = gameState.getBoard().getPlayers().get(playerIndex);
					int meeples = currentPlayer.getMeeples();
					meeples -= index; // pay for the card, index = cost
					meeples += card.getMeeples(); // add meeples you get from card
					int points = currentPlayer.getPoints();

					if (points > 0 || meeples > 0) {
						MeepleTraderController controller = Controller.initMVC(MeepleTraderController.class,
								MeepleTraderModel.class, MeepleTraderView.class);

						controller.setCurrencies(meeples, points);
						controller.showAndWait();

						int decision = controller.getDecision();
						move.getDecisions().add(decision);

						MetaContainer.getInstance().destroyController(controller);
					}
				}

				model.sendMove(move);
			}
		});
	}

	private int showSplitCardChooser(Card splitCard, String splitCardInfo) {
		int decision = -1;

		if (splitCard.isSplitCard()) {
			SplitCardChooserController controller = Controller.initMVC(SplitCardChooserController.class,
					SplitCardChooserModel.class, SplitCardChooserView.class);

			controller.loadSplitCard(splitCard, splitCardInfo);
			controller.showAndWait();

			decision = controller.getDecision();

			MetaContainer.getInstance().destroyController(controller);
		}

		return decision;
	}

	private void showGameEndView(GameState gameState) {
		Platform.runLater(() -> {
			GameEndController controller = Controller.initMVC(GameEndController.class, GameEndModel.class,
					GameEndView.class);

			controller.setGameState(gameState);
			controller.showAndWait();

			MetaContainer.getInstance().destroyController(controller);
		});
	}

	public GameState getGameState() {
		return gameState;
	}

	private boolean isTurnPlayer() {
		int playersTurn = this.gameState.getPlayersTurn();
		Player player = this.gameState.getBoard().getPlayers().get(playersTurn);
		String username = Session.getCurrentUsername();

		return player.getUsername().equals(username) && !this.gameState.isGameEnded();
	}

	// EVENT METHODS

	@FXML
	private void btnLeave_Click(ActionEvent e) {
		String alertText = translator.getTranslation("inf_Leave_Game");
		String btnYesText = translator.getTranslation("btn_Leave_Game_Yes");
		String btnNoText = translator.getTranslation("btn_Leave_Game_No");

		ButtonType btnYes = new ButtonType(btnYesText, ButtonData.YES);

		Alert alert = DialogUtils.getAlert(MetaContainer.getInstance().getMainStage(), AlertType.CONFIRMATION,
				alertText, btnYes, new ButtonType(btnNoText, ButtonData.NO));

		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == btnYes) {
			model.leaveGame();

			Platform.runLater(() -> {
				Controller.initMVCAsRoot(MenuBarController.class, MenuBarModel.class, MenuBarView.class);
			});
		}
	}

}
