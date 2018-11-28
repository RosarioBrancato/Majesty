package ch.fhnw.projectbois.game;

import java.util.ArrayList;
import java.util.Collections;

import ch.fhnw.projectbois.gameobjects.Board;
import ch.fhnw.projectbois.gameobjects.Card;
import ch.fhnw.projectbois.gameobjects.CardType;
import ch.fhnw.projectbois.gameobjects.DisplayCard;
import ch.fhnw.projectbois.gameobjects.GameMove;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.gameobjects.Location;
import ch.fhnw.projectbois.gameobjects.Player;
import ch.fhnw.projectbois.network.Lobby;
import ch.fhnw.projectbois.network.ServerClient;

public class GameLogic {

	private GameState gameState = null;
	private GameStateServer gameStateServer = null;

	public GameLogic(GameState gameState, GameStateServer gameStateServer) {
		this.gameState = gameState;
		this.gameStateServer = gameStateServer;
	}

	public void executeMove(String username, GameMove move) {
		ArrayList<DisplayCard> display = this.gameState.getBoard().getDisplay();
		ArrayList<Player> players = this.gameState.getBoard().getPlayers();

		int selectedIndex = move.getDisplayCardIndexSelected();
		DisplayCard selectedCard = display.get(selectedIndex);
		Player currentPlayer = players.stream().filter(f -> f.getUsername().equals(username)).findFirst().get();

		// split card decision
		CardType currentCardType = selectedCard.getCardType1();
		if (selectedCard.isSplitCard()) {
			int decision = move.getNextDecision();
			if (decision == 2) {
				currentCardType = selectedCard.getCardType2();
			}
		}

		// move card meeples to player
		int meeples = selectedCard.getMeeples();
		currentPlayer.setMeeples(currentPlayer.getMeeples() + meeples);
		selectedCard.setMeeples(0);

		// add card to display
		int locationIndex = this.getLocationIndexByCardType(currentCardType);
		currentPlayer.getLocationByIndex(locationIndex).getCards().add(selectedCard);

		// add new card to display
		display.remove(selectedIndex);
		DisplayCard newCard = this.drawACard();
		display.add(newCard);

		// card special effect
		switch (currentCardType) {
		case Witch:
			// Revive
			int size = currentPlayer.getLocationByIndex(Location.INFIRMARY).getCards().size();
			if (size > 0) {
				Card toRevive = currentPlayer.getLocationByIndex(Location.INFIRMARY).getCards().get(size - 1);
				currentPlayer.getLocationByIndex(Location.INFIRMARY).getCards().remove(size - 1);

				if (toRevive.isSplitCard()) {
					int decision = move.getNextDecision();
					toRevive.setActiveCardType(decision);
				}

				int reviveLocationIndex = this.getLocationIndexByCardType(toRevive.getCardTypeActive());
				currentPlayer.getLocationByIndex(reviveLocationIndex).getCards().add(toRevive);
			}
			break;
		case Knight:
			// Attack
			int knights = currentPlayer.getLocationByIndex(Location.BARACKS).getCards().size();
			for (Player p : players) {
				if (!p.getUsername().equals(currentPlayer.getUsername())) {
					int guards = p.getLocationByIndex(Location.GUARDHOUSE).getCards().size();
					if (knights > guards) {
						// kill left-most card - ignore infirmary
						for (int i = 0; i < p.getLocations().length - 1; i++) {
							Location location = p.getLocationByIndex(i);
							int locationSize = location.getCards().size();

							if (locationSize > 0) {
								// remove last card of list
								Card cardToKill = location.getCards().get(locationSize - 1);
								location.getCards().remove(locationSize - 1);
								p.getLocationByIndex(Location.INFIRMARY).getCards().add(cardToKill);
								break;
							}
						}
					}
				}
			}
			break;
		default:
			break;
		}

		// points

		// after points
	}

	public boolean startNextTurn() {
		boolean gameOver = false;

		int startPlayer = this.gameState.getStartPlayerIndex();
		int round = this.gameState.getRound();
		boolean isFirstTurn = round == 0;
		if (isFirstTurn) {
			this.gameState.setPlayersTurn(startPlayer);
			this.gameState.setRound(1);

		} else {
			int playersCount = this.gameState.getBoard().getPlayers().size();
			int playersTurn = this.gameState.getPlayersTurn();

			playersTurn++;
			if (playersTurn >= playersCount) {
				playersTurn = 0;
			}

			if (playersTurn == startPlayer) {
				round++;
			}

			if (round > 12) {
				gameOver = true;
			} else {
				this.gameState.setPlayersTurn(playersTurn + 1);
				this.gameState.setRound(round);
			}
		}

		return gameOver;
	}

	public void endGame() {

	}

	// SET UP METHODS

	public void definePlayers(Lobby lobby) {
		ArrayList<ServerClient> clients = lobby.getClients();

		for (ServerClient c : clients) {
			Player player = new Player();
			player.setUsername(c.getUser().getUsername());
			player.setMeeples(5);

			this.gameState.getBoard().getPlayers().add(player);
		}

		int playerMultiplicator = this.gameState.getBoard().getPlayers().size() - 1;
		int startPlayerIndex = (int) Math.round(Math.random() * playerMultiplicator);
		this.gameState.setStartPlayerIndex(startPlayerIndex);
	}

	public void fillDecks() {
		// TIER 2
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Miller));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Brewer));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Witch));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Guard));
		}
		gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Knight));
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Innkeeper));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Noble));
		}
		gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Miller, CardType.Brewer));
		gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Miller, CardType.Knight));
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Brewer, CardType.Witch));
		}
		gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Brewer, CardType.Knight));
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Witch, CardType.Guard));
		}
		gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Witch, CardType.Innkeeper));
		gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Witch, CardType.Noble));
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Guard, CardType.Knight));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Knight, CardType.Innkeeper));
		}
		gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Innkeeper, CardType.Noble));

		// TIER 1
		for (int i = 0; i < 7; i++) {
			gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Miller));
		}
		for (int i = 0; i < 4; i++) {
			gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Brewer));
		}
		for (int i = 0; i < 3; i++) {
			gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Witch));
		}
		for (int i = 0; i < 3; i++) {
			gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Guard));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Knight));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Innkeeper));
		}
		for (int i = 0; i < 3; i++) {
			gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Noble));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Miller, CardType.Brewer));
		}
		gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Miller, CardType.Knight));
		gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Brewer, CardType.Witch));
		gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Witch, CardType.Guard));
		gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Guard, CardType.Innkeeper));
		gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Guard, CardType.Noble));
		gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Knight, CardType.Innkeeper));
		gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Innkeeper, CardType.Noble));

		Collections.shuffle(gameStateServer.getDeckTier2());
		Collections.shuffle(gameStateServer.getDeckTier1());
	}

	public void setCardsAside() {
		int cardsToRemove = 0;

		switch (gameState.getBoard().getPlayers().size()) {
		case 2:
			cardsToRemove = 6;
			break;
		case 3:
			cardsToRemove = 14;
			break;
		case 4:
			cardsToRemove = 26;
			break;
		}

		for (int i = 0; i < cardsToRemove; i++) {
			gameStateServer.getDeckTier1().remove(0);
		}

		this.updateCardsLeft();
	}

	public void fillDisplay() {
		for (int i = 0; i < 6; i++) {
			DisplayCard card = this.drawACard();
			gameState.getBoard().getDisplay().add(card);
		}
	}

	private DisplayCard drawACard() {
		DisplayCard card = null;

		if (gameStateServer.getDeckTier1().size() > 0) {
			card = gameStateServer.getDeckTier1().get(0);
			gameStateServer.getDeckTier1().remove(0);

		} else if (gameStateServer.getDeckTier2().size() > 0) {
			card = gameStateServer.getDeckTier2().get(0);
			gameStateServer.getDeckTier2().remove(0);
		}

		// Deck back for display
		if (gameStateServer.getDeckTier1().size() > 0) {
			gameState.getBoard().setDeckBack(Board.DECKBACK_TIER1);
		} else if (gameStateServer.getDeckTier2().size() > 0) {
			gameState.getBoard().setDeckBack(Board.DECKBACK_TIER2);
		} else {
			gameState.getBoard().setDeckBack(Board.DECKBACK_EMPTY);
		}

		return card;
	}

	// PRIVATE METHODS

	private void updateCardsLeft() {
		int cardCount = this.gameStateServer.getDeckTier2().size();
		cardCount += this.gameStateServer.getDeckTier1().size();

		this.gameState.getBoard().setCardsLeft(cardCount);
	}

	private int getLocationIndexByCardType(CardType cardType) {
		switch (cardType) {
		case Miller:
			return Location.MILL;
		case Brewer:
			return Location.BREWERY;
		case Witch:
			return Location.COTTAGE;
		case Guard:
			return Location.GUARDHOUSE;
		case Knight:
			return Location.BARACKS;
		case Innkeeper:
			return Location.INN;
		case Noble:
			return Location.CASTLE;
		default:
			return -1;
		}
	}

}
