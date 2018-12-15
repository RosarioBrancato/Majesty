package ch.fhnw.projectbois.game;

import java.util.ArrayList;
import java.util.Optional;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class GameController extends Controller<GameModel, GameView> {

	private GameResourceHelper resourceHelper = null;

	private boolean firstLoading = true;
	private Time turntimer = null;
	private ChangeListener<Number> turntimerPropertyListener = null;

	@FXML
	private Label lblGameInfo;

	@FXML
	private GridPane pnlDisplay;

	@FXML
	private Label lblRound;

	@FXML
	private Label lblTimer;

	@FXML
	private Label pnlDisplayCardStack;

	public GameController(GameModel model, GameView view) {
		super(model, view);

		this.resourceHelper = new GameResourceHelper();
	}

	@Override
	protected void initialize() {
		super.initialize();

		this.model.getGameStateProperty().addListener((observer, oldValue, newValue) -> {
			if (newValue.getId() > oldValue.getId()) {
				loadGameState(newValue);
			}
		});

		this.model.getGameEndProperty().addListener((observer, oldValue, newValue) -> {
			showGameEndView(model.getGameState());
		});

		this.model.requestGameState();
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
		if (firstLoading) {
			model.definePlayersIndex();
			view.initGamePlayerContainers();
			view.initLocations();
			this.initDisplayCardClickEvent();

			this.initTurntimer(gameState.getTurntimer());
		}

		this.drawGui();
	}

	// SET UP

	private void initTurntimer(int seconds) {
		this.turntimer = new Time();
		this.initTurntimerPropertyListener();
		this.turntimer.getPeriodCounterProperty().addListener(this.turntimerPropertyListener);
		this.turntimer.startCountdown(seconds);
	}

	private void initTurntimerPropertyListener() {
		this.turntimerPropertyListener = (observer, oldValue, newValue) -> {
			updateInfoBar();
		};
	}

	private void initDisplayCardClickEvent() {
		for (int i = 0; i < 6; i++) {
			Pane pnlDisplayCard = (Pane) view.getRoot().lookup("pnlDisplayCard" + (i + 1));
			this.addDisplayClickEvent(pnlDisplayCard, i);
		}
	}

	private void addDisplayClickEvent(Pane pnlDisplayCard, int displayIndex) {
		pnlDisplayCard.setOnMouseClicked(new EventHandler<Event>() {

			int index = displayIndex;

			@Override
			public void handle(Event event) {
				boolean allowMove = isTurnPlayer();

				if (allowMove) {
					GameState gameState = model.getGameState();

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
						Player currentPlayer = gameState.getBoard().getPlayers().get(model.getPlayerIndex());
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
			}
		});
	}

	// MAIN METHODS

	private void drawGui() {
		Board board = model.getGameState().getBoard();

		this.loadGameInfos();
		this.drawDisplay(board.getDisplay());
	}

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

	private void updateInfoBar() {
		final String timer = translator.getTranslation("lbl_Timer", this.turntimer.getCounter());
		final String round = translator.getTranslation("lbl_Round", model.getGameState().getRound());

		Platform.runLater(() -> {
			this.lblRound.setText(round);
			this.lblTimer.setText(timer);
		});
	}

	private void loadGameInfoPlayer(Player player, GamePlayerContainer container, boolean isStartingPlayer) {
		String username;
		if (isStartingPlayer) {
			username = translator.getTranslation("lblFirst", player.getUsername());
		} else {
			username = player.getUsername();
		}

		String points = translator.getTranslation("lblPoints", player.getPoints());
		String meeples = translator.getTranslation("lbl_Meeples", player.getMeeples());

		Platform.runLater(() -> {
			container.getLblUsername().setText(username);
			container.getLblPoints().setText(points);
			container.getLblMeeples().setText(meeples);
		});
	}

	private void drawDisplay(ArrayList<Card> displayCards) {
		for (int i = 0; i < displayCards.size(); i++) {
			Card card = displayCards.get(i);

			Pane pnlDisplayCard = (Pane) view.getRoot().lookup("pnlDisplayCard" + (i + 1));
			pnlDisplayCard.getStylesheets().clear();

			String url = this.resourceHelper.getResourceByCard(card);
			pnlDisplayCard.setStyle("-fx-background-image: url('" + url + "');");
		}

		// Deck
		//int deckBack = gameState.getBoard().getDeckBack();
		//Image image = this.resourceHelper.getDeckBackImage(deckBack);
		this.pnlDisplayCardStack.setStyle("-fx-background-image: asf");
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

	private boolean isTurnPlayer() {
		GameState gameState = model.getGameState();
		int playersTurn = gameState.getPlayersTurn();
		Player player = gameState.getBoard().getPlayers().get(playersTurn);
		String username = Session.getCurrentUsername();

		return player.getUsername().equals(username) && !gameState.isGameEnded();
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
