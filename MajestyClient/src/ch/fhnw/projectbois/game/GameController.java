package ch.fhnw.projectbois.game;

import java.util.ArrayList;
import java.util.Optional;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._utils.DialogUtils;
import ch.fhnw.projectbois.components.menubar.MenuBarController;
import ch.fhnw.projectbois.components.menubar.MenuBarModel;
import ch.fhnw.projectbois.components.menubar.MenuBarView;
import ch.fhnw.projectbois.game.meepletrader.MeepleTraderController;
import ch.fhnw.projectbois.game.meepletrader.MeepleTraderModel;
import ch.fhnw.projectbois.game.meepletrader.MeepleTraderView;
import ch.fhnw.projectbois.game.splitcardchooser.SplitCardChooserController;
import ch.fhnw.projectbois.game.splitcardchooser.SplitCardChooserModel;
import ch.fhnw.projectbois.game.splitcardchooser.SplitCardChooserView;
import ch.fhnw.projectbois.gameend.GameEndController;
import ch.fhnw.projectbois.gameend.GameEndModel;
import ch.fhnw.projectbois.gameend.GameEndView;
import ch.fhnw.projectbois.gameobjects.Card;
import ch.fhnw.projectbois.gameobjects.CardType;
import ch.fhnw.projectbois.gameobjects.GameMove;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.gameobjects.Location;
import ch.fhnw.projectbois.gameobjects.Player;
import ch.fhnw.projectbois.session.Session;
import ch.fhnw.projectbois.time.Time;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * The Class GameController.
 * 
 * @author Rosario Brancato
 */
public class GameController extends Controller<GameModel, GameView> {

	private GameResourceHelper resourceHelper = null;
	private boolean firstLoading = true;

	private Time turntimer = null;
	private ChangeListener<Number> turntimerPropertyListener = null;

	@FXML
	private GridPane pnlField;

	@FXML
	private Label lblRound;

	@FXML
	private Label lblTimer;

	@FXML
	private StackPane pnlDisplayCardStack;

	@FXML
	private Pane pnlHover;

	/**
	 * Instantiates a new game controller.
	 *
	 * @param model the model
	 * @param view  the view
	 */
	public GameController(GameModel model, GameView view) {
		super(model, view);

		this.resourceHelper = new GameResourceHelper();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.fhnw.projectbois._mvc.Controller#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();

		this.model.getGameStateProperty().addListener((observer, oldValue, newValue) -> {
			loadGameState(newValue);
		});

		this.model.getGameEndProperty().addListener((observer, oldValue, newValue) -> {
			showGameEndView(model.getGameState());
		});

		this.model.requestGameState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.fhnw.projectbois._mvc.Controller#destroy()
	 */
	@Override
	public void destroy() {
		super.destroy();

		if (this.turntimer != null) {
			this.turntimer.stop();
			this.turntimer.getPeriodCounterProperty().removeListener(this.turntimerPropertyListener);
			this.turntimer = null;
		}
	}

	/**
	 * Load game state.
	 *
	 * @param gameState the game state
	 */
	private void loadGameState(GameState gameState) {
		if (firstLoading) {
			model.definePlayersIndex();
			view.initGamePlayerContainers();
			view.initLocations();
			initDisplayCardClickEvent();

			initTurntimer(gameState.getTurntimer());

			firstLoading = false;
		}

		this.drawGui();
		view.highlightPlayerTurn();
	}

	/**
	 * Show game end view.
	 *
	 * @param gameState the game state
	 */
	private void showGameEndView(GameState gameState) {
		Platform.runLater(() -> {
			GameEndController controller = Controller.initMVC(GameEndController.class, GameEndModel.class,
					GameEndView.class);

			controller.setGameState(gameState);
			controller.showAndWait();

			MetaContainer.getInstance().destroyController(controller);
		});
	}

	// SET UP

	/**
	 * Inits the turntimer.
	 *
	 * @param seconds the seconds
	 */
	private void initTurntimer(int seconds) {
		this.turntimer = new Time();
		this.initTurntimerPropertyListener();
		this.turntimer.getPeriodCounterProperty().addListener(this.turntimerPropertyListener);
		this.turntimer.startCountdown(seconds);
	}

	/**
	 * Inits the turntimer property listener.
	 */
	private void initTurntimerPropertyListener() {
		this.turntimerPropertyListener = (observer, oldValue, newValue) -> {
			updateInfoBar();
		};
	}

	/**
	 * Inits the display card click event.
	 */
	private void initDisplayCardClickEvent() {
		for (int i = 0; i < 6; i++) {
			Pane pnlDisplayCard = (Pane) view.getRoot().lookup("#pnlDisplayCard" + (i + 1));
			this.addDisplayClickEvent(pnlDisplayCard, i);
		}
	}

	/**
	 * Adds the display click event.
	 *
	 * @param pnlDisplayCard the pnl display card
	 * @param displayIndex   the display index
	 */
	private void addDisplayClickEvent(Pane pnlDisplayCard, int displayIndex) {
		pnlDisplayCard.setOnMouseClicked(new EventHandler<Event>() {

			int index = displayIndex;

			@Override
			public void handle(Event event) {
				GameState gameState = model.getGameState();
				Player currentPlayer = gameState.getBoard().getPlayers().get(model.getPlayerIndex());
				int meeples = currentPlayer.getMeeples();

				boolean allowMove = isTurnPlayer();
				allowMove &= (index <= meeples);

				if (allowMove) {

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
						Location infirmary = gameState.getBoard().getPlayers().get(model.getPlayerIndex())
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
			}
		});
	}

	// MAIN METHODS

	/**
	 * Draw GUI
	 */
	private void drawGui() {
		this.loadGameInfos();
		this.drawDisplay();
		this.drawLocationCards();
	}

	/**
	 * Load game infos.
	 */
	private void loadGameInfos() {
		GameState gameState = model.getGameState();
		ArrayList<Player> players = gameState.getBoard().getPlayers();

		int turntimer = gameState.getTurntimer();
		if (gameState.isGameEnded()) {
			this.turntimer.stop();
		} else {
			this.turntimer.setCounter(turntimer);
		}

		this.updateInfoBar();

		{
			Player player = players.stream()
					.filter(f -> f.getUsername().equals(view.getPlayerContainer().getUsername())).findFirst().get();

			boolean isStartingPlayer = players.get(gameState.getStartPlayerIndex()).getUsername()
					.equals(Session.getCurrentUsername());

			this.loadGameInfoPlayer(player, view.getPlayerContainer(), isStartingPlayer);
		}

		for (GamePlayerContainer opponent : view.getOpponentContainers()) {
			Player player = players.stream().filter(f -> f.getUsername().equals(opponent.getUsername())).findFirst()
					.get();

			boolean isStartingPlayer = players.get(gameState.getStartPlayerIndex()).getUsername()
					.equals(opponent.getUsername());

			this.loadGameInfoPlayer(player, opponent, isStartingPlayer);
		}
	}

	/**
	 * Update info bar.
	 */
	private void updateInfoBar() {
		final String timer = translator.getTranslation("lbl_Timer", this.turntimer.getCounter());
		final String round = translator.getTranslation("lbl_Round", model.getGameState().getRound());

		Platform.runLater(() -> {
			this.lblRound.setText(round);
			this.lblTimer.setText(timer);
		});
	}

	/**
	 * Load game info player.
	 *
	 * @param player           the player
	 * @param container        the container
	 * @param isStartingPlayer is the starting player?
	 */
	private void loadGameInfoPlayer(Player player, GamePlayerContainer container, boolean isStartingPlayer) {
		String username;
		if (isStartingPlayer) {
			username = translator.getTranslation("lbl_First", player.getUsername());
		} else {
			username = player.getUsername();
		}

		String points = translator.getTranslation("lbl_Points", player.getPoints());
		String meeples = translator.getTranslation("lbl_Meeples", player.getMeeples());

		Platform.runLater(() -> {
			container.getLblUsername().setText(username);
			container.getLblPoints().setText(points);
			container.getLblMeeples().setText(meeples);
		});
	}

	/**
	 * Draw display.
	 */
	private void drawDisplay() {
		GameState gameState = model.getGameState();
		ArrayList<Card> displayCards = gameState.getBoard().getDisplay();
		Player player = gameState.getBoard().getPlayers().get(model.getPlayerIndex());

		for (int i = 0; i < displayCards.size(); i++) {
			Card card = displayCards.get(i);
			Pane pnlDisplayCard = (Pane) view.getRoot().lookup("#pnlDisplayCard" + (i + 1));
			Label lblCardMeeples = (Label) view.getRoot().lookup("#lblCardMeeples" + (i + 1));

			final Integer index = new Integer(i);
			final int meeples = player.getMeeples();
			final boolean isTurnPlayer = isTurnPlayer();
			final String url = this.resourceHelper.getUrlByCard(card);

			final int cardMeeples = card.getMeeples();

			Platform.runLater(() -> {
				pnlDisplayCard.getStyleClass().clear();
				pnlDisplayCard.getStyleClass().add("pnlImage");

				if (index <= meeples && isTurnPlayer) {
					pnlDisplayCard.getStyleClass().add("displayToHover");
				}
				pnlDisplayCard.setStyle("-fx-background-image: url('" + url + "');");

				lblCardMeeples.setText(String.valueOf(cardMeeples));
			});
		}
		
		// empty display slots
		for(int i = displayCards.size(); i < 6; i++ ) {
			Pane pnlDisplayCard = (Pane) view.getRoot().lookup("#pnlDisplayCard" + (i + 1));
			Label lblCardMeeples = (Label) view.getRoot().lookup("#lblCardMeeples" + (i + 1));
			
			Platform.runLater(() -> {
				pnlDisplayCard.getStyleClass().clear();
				pnlDisplayCard.setStyle("");
				lblCardMeeples.setText("");
			});
		}

		// Deck
		int deckBack = gameState.getBoard().getDeckBack();
		if (deckBack != Card.BACK_NONE) {
			String url = this.resourceHelper.getUrlByCardBack(deckBack);
			this.pnlDisplayCardStack.setStyle("-fx-background-image: url('" + url + "');");
		} else {
			this.pnlDisplayCardStack.setStyle("");
		}
	}

	/**
	 * Draw location cards.
	 */
	private void drawLocationCards() {
		ArrayList<Player> players = model.getGameState().getBoard().getPlayers();

		Player currentPlayer = players.stream().filter(f -> f.getUsername().equals(Session.getCurrentUsername()))
				.findFirst().get();

		this.drawLocationOfPlayer(currentPlayer, view.getPlayerContainer());

		for (GamePlayerContainer container : view.getOpponentContainers()) {
			Player player = players.stream().filter(f -> f.getUsername().equals(container.getUsername())).findFirst()
					.get();
			this.drawLocationOfPlayer(player, container);
		}
	}

	/**
	 * Draw cads in location of player.
	 *
	 * @param player    the player
	 * @param container the container
	 */
	private void drawLocationOfPlayer(Player player, GamePlayerContainer container) {
		final int playerRow = container.getPlayerRow();

		for (int i = 0; i < Location.MAX_COUNT; i++) {
			final int index = i;
			final Integer col = (i + 1);

			Platform.runLater(() -> {
				StackPane stackPane = (StackPane) pnlField.getChildren().stream()
						.filter(f -> GridPane.getColumnIndex(f) != null && GridPane.getRowIndex(f) != null
								&& GridPane.getColumnIndex(f) == col && GridPane.getRowIndex(f) == playerRow)
						.findFirst().get();
				stackPane.getChildren().clear();
				stackPane.setAlignment(Pos.TOP_RIGHT);

				Location location = player.getLocationByIndex(index);

				for (int k = 0; k < location.getCards().size(); k++) {
					Card card = location.getCards().get(k);

					Pane pane = new Pane();
					pane.setMinWidth(50);
					pane.setPrefWidth(50);
					pane.setMaxWidth(50);

					String style = "-fx-background-size: contain ; ";
					style += "-fx-background-position: top ; ";
					style += "-fx-background-repeat: stretch ; ";

					String url;
					if (index == Location.INFIRMARY) {
						url = resourceHelper.getUrlByCardBack(card.getCardBack());
						style += "-fx-background-image: url('" + url + "'); ";
					} else {
						url = resourceHelper.getUrlByCard(card);
						style += "-fx-background-image: url('" + url + "'); ";

						// display active split card part on top
						if (k != Location.INFIRMARY && card.getActiveCardType() == 2) {
							style += "-fx-rotate: 180; ";
							style += "-fx-background-position: bottom ; ";
						}
					}

					stackPane.getChildren().add(pane);
					StackPane.setAlignment(pane, Pos.TOP_RIGHT);
					int marginLeft = (k * 20) + 3;
					StackPane.setMargin(pane, new Insets(3, marginLeft, 0, 0));
					pane.setStyle(style);
					// hover a location card -> show in highlighted box
					pane.hoverProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							pnlHover.setStyle(pane.getStyle());
						}
					});
				}
			});
		}
	}

	/**
	 * Show split card chooser.
	 *
	 * @param splitCard     the split card
	 * @param splitCardInfo the split card info
	 * @return the int
	 */
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

	/**
	 * Checks if is turn player.
	 *
	 * @return true, if is turn player
	 */
	private boolean isTurnPlayer() {
		GameState gameState = model.getGameState();
		int playersTurn = gameState.getPlayersTurn();
		Player player = gameState.getBoard().getPlayers().get(playersTurn);
		String username = Session.getCurrentUsername();

		return player.getUsername().equals(username) && !gameState.isGameEnded();
	}

	// EVENT METHODS

	/**
	 * Click event for button leave.
	 *
	 * @param e the e
	 */
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
