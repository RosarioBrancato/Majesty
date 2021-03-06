package ch.fhnw.projectbois.game;

import java.util.ArrayList;
import java.util.Collections;

import ch.fhnw.projectbois.gameobjects.Card;
import ch.fhnw.projectbois.gameobjects.CardType;
import ch.fhnw.projectbois.gameobjects.GameMove;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.gameobjects.Location;
import ch.fhnw.projectbois.gameobjects.Player;
import ch.fhnw.projectbois.network.Lobby;
import ch.fhnw.projectbois.network.ServerClient;
import ch.fhnw.projectbois.queries.UpdatePointsQuery;

/**
 * 
 * @author Rosario Brancato
 *
 */
public class GameLogic {

	private GameState gameState = null;
	private GameStateServer gameStateServer = null;

	public GameLogic(GameState gameState, GameStateServer gameStateServer) {
		this.gameState = gameState;
		this.gameStateServer = gameStateServer;

		// increment id to demonstrate changes
		this.gameState.setId(this.gameState.getId() + 1);
	}

	public void executeMove(String username, GameMove move) {
		GameCalculations calculations = new GameCalculations(gameState);

		ArrayList<Card> display = this.gameState.getBoard().getDisplay();
		ArrayList<Player> players = this.gameState.getBoard().getPlayers();

		int selectedIndex = move.getDisplayCardIndexSelected();
		Card selectedCard = display.get(selectedIndex);
		Player currentPlayer = players.stream().filter(f -> f.getUsername().equals(username)).findFirst().get();

		// pay meeples for card
		calculations.payMeeplesForCard(selectedIndex);

		// split card decision
		CardType currentCardType = selectedCard.getCardType1();
		if (selectedCard.isSplitCard()) {
			int decision = move.getNextDecision();
			if (decision == 2) {
				currentCardType = selectedCard.getCardType2();
				selectedCard.setActiveCardType(2);
			}
		} else {
			selectedCard.setActiveCardType(1);
		}

		// move card meeples to player
		calculations.receiveMeeplesOfCard(selectedCard);

		// add card to player
		int locationIndex = this.getLocationIndexByCardType(currentCardType);
		currentPlayer.getLocationByIndex(locationIndex).getCards().add(selectedCard);

		// add new card to display
		display.remove(selectedIndex);
		Card newCard = this.drawACard();
		if (newCard != null) {
			display.add(newCard);
		}

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

		// distribute points
		if (currentCardType == CardType.Noble && !this.gameState.isCardSideA()) {
			int meeplesToTrade = move.getNextDecision();
			if (meeplesToTrade >= -5 && meeplesToTrade <= 5) {
				calculations.tradeMeeples(meeplesToTrade);
			}
		}
		calculations.distributePoints(currentCardType);

		// meeple overflow
		calculations.convertMeepleOverflowToPoints();
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

			// check how many player are left
			long ingame = this.gameState.getBoard().getPlayers().stream().filter(f -> !f.isPlayerLeft()).count();

			if (ingame > 1) {
				Player player;
				int whileBreaker = 0;
				do {
					playersTurn++;
					if (playersTurn >= playersCount) {
						playersTurn = 0;
					}

					if (playersTurn == startPlayer) {
						round++;
					}

					// if a player left, skip his turn
					player = this.gameState.getBoard().getPlayers().get(playersTurn);
					whileBreaker++;
					if (whileBreaker > 4) {
						break;
					}
				} while (player.isPlayerLeft());
			}

			if (round > 12 || ingame <= 1) {
				gameOver = true;
				this.endGame();

			} else {
				this.gameState.setPlayersTurn(playersTurn);
				this.gameState.setRound(round);
			}
		}

		return gameOver;
	}

	public void endGame() {
		this.gameState.setGameEnded(true);
		this.gameStateServer.setGameEnded(true);

		// calculate and distribute points which will get forwarded to all clients with
		// latest gamestate
		GameCalculations calculations = new GameCalculations(gameState);
		calculations.distributeFinalScoring();

		// write scores to db
		ArrayList<Player> players = this.gameState.getBoard().getPlayers();
		UpdatePointsQuery updatepointsquery = new UpdatePointsQuery();
		updatepointsquery.setPoints(players);
	}

	public void removePlayer(Player player) {

		player.setPlayerLeft(true);
		if (!gameState.isGameEnded()) {

			// if only 1 player is left -> end game
			long ingame = this.gameState.getBoard().getPlayers().stream().filter(f -> !f.isPlayerLeft()).count();
			if (ingame <= 1) {
				this.endGame();

			} else {
				int playersTurn = this.gameState.getPlayersTurn();
				Player currentPlayer = this.gameState.getBoard().getPlayers().get(playersTurn);

				if (currentPlayer.getUsername().equals(player.getUsername())) {
					this.startNextTurn();
				}
			}
		}
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
			gameStateServer.getDeckTier2().add(new Card(CardType.Miller, Card.BACK_TIER2));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new Card(CardType.Brewer, Card.BACK_TIER2));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new Card(CardType.Witch, Card.BACK_TIER2));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new Card(CardType.Guard, Card.BACK_TIER2));
		}
		gameStateServer.getDeckTier2().add(new Card(CardType.Knight, Card.BACK_TIER2));
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new Card(CardType.Innkeeper, Card.BACK_TIER2));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new Card(CardType.Noble, Card.BACK_TIER2));
		}
		gameStateServer.getDeckTier2().add(new Card(CardType.Miller, CardType.Brewer, Card.BACK_TIER2));
		gameStateServer.getDeckTier2().add(new Card(CardType.Miller, CardType.Knight, Card.BACK_TIER2));
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new Card(CardType.Brewer, CardType.Witch, Card.BACK_TIER2));
		}
		gameStateServer.getDeckTier2().add(new Card(CardType.Brewer, CardType.Knight, Card.BACK_TIER2));
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new Card(CardType.Witch, CardType.Guard, Card.BACK_TIER2));
		}
		gameStateServer.getDeckTier2().add(new Card(CardType.Witch, CardType.Innkeeper, Card.BACK_TIER2));
		gameStateServer.getDeckTier2().add(new Card(CardType.Witch, CardType.Noble, Card.BACK_TIER2));
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new Card(CardType.Guard, CardType.Knight, Card.BACK_TIER2));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new Card(CardType.Knight, CardType.Innkeeper, Card.BACK_TIER2));
		}
		gameStateServer.getDeckTier2().add(new Card(CardType.Innkeeper, CardType.Noble, Card.BACK_TIER2));

		// TIER 1
		for (int i = 0; i < 7; i++) {
			gameStateServer.getDeckTier1().add(new Card(CardType.Miller, Card.BACK_TIER1));
		}
		for (int i = 0; i < 4; i++) {
			gameStateServer.getDeckTier1().add(new Card(CardType.Brewer, Card.BACK_TIER1));
		}
		for (int i = 0; i < 3; i++) {
			gameStateServer.getDeckTier1().add(new Card(CardType.Witch, Card.BACK_TIER1));
		}
		for (int i = 0; i < 3; i++) {
			gameStateServer.getDeckTier1().add(new Card(CardType.Guard, Card.BACK_TIER1));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier1().add(new Card(CardType.Knight, Card.BACK_TIER1));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier1().add(new Card(CardType.Innkeeper, Card.BACK_TIER1));
		}
		for (int i = 0; i < 3; i++) {
			gameStateServer.getDeckTier1().add(new Card(CardType.Noble, Card.BACK_TIER1));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier1().add(new Card(CardType.Miller, CardType.Brewer, Card.BACK_TIER1));
		}
		gameStateServer.getDeckTier1().add(new Card(CardType.Miller, CardType.Knight, Card.BACK_TIER1));
		gameStateServer.getDeckTier1().add(new Card(CardType.Brewer, CardType.Witch, Card.BACK_TIER1));
		gameStateServer.getDeckTier1().add(new Card(CardType.Witch, CardType.Guard, Card.BACK_TIER1));
		gameStateServer.getDeckTier1().add(new Card(CardType.Guard, CardType.Innkeeper, Card.BACK_TIER1));
		gameStateServer.getDeckTier1().add(new Card(CardType.Guard, CardType.Noble, Card.BACK_TIER1));
		gameStateServer.getDeckTier1().add(new Card(CardType.Knight, CardType.Innkeeper, Card.BACK_TIER1));
		gameStateServer.getDeckTier1().add(new Card(CardType.Innkeeper, CardType.Noble, Card.BACK_TIER1));

		Collections.shuffle(gameStateServer.getDeckTier2());
		Collections.shuffle(gameStateServer.getDeckTier1());
	}

	public void setCardsAside() {
		int totalTier1Cards = gameStateServer.getDeckTier1().size();

		int cardsToRemove = 0;

		switch (gameState.getBoard().getPlayers().size()) {
		case 2:
			cardsToRemove = totalTier1Cards - 6;
			break;
		case 3:
			cardsToRemove = totalTier1Cards - 14;
			break;
		case 4:
			cardsToRemove = totalTier1Cards - 26;
			break;
		}

		for (int i = 0; i < cardsToRemove; i++) {
			gameStateServer.getDeckTier1().remove(0);
		}

		this.updateCardsLeft();
	}

	public void fillDisplay() {
		for (int i = 0; i < 6; i++) {
			Card card = this.drawACard();
			gameState.getBoard().getDisplay().add(card);
		}
	}

	private Card drawACard() {
		Card card = null;

		if (gameStateServer.getDeckTier1().size() > 0) {
			card = gameStateServer.getDeckTier1().get(0);
			gameStateServer.getDeckTier1().remove(0);

		} else if (gameStateServer.getDeckTier2().size() > 0) {
			card = gameStateServer.getDeckTier2().get(0);
			gameStateServer.getDeckTier2().remove(0);
		}

		// Deck back for display
		if (gameStateServer.getDeckTier1().size() > 0) {
			gameState.getBoard().setDeckBack(Card.BACK_TIER1);
		} else if (gameStateServer.getDeckTier2().size() > 0) {
			gameState.getBoard().setDeckBack(Card.BACK_TIER2);
		} else {
			gameState.getBoard().setDeckBack(Card.BACK_NONE);
		}

		// update cards left
		this.updateCardsLeft();

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
