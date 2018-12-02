package ch.fhnw.projectbois.game;

import java.util.ArrayList;

import ch.fhnw.projectbois.gameobjects.Card;
import ch.fhnw.projectbois.gameobjects.CardType;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.gameobjects.Location;
import ch.fhnw.projectbois.gameobjects.Player;

/**
 * 
 * @author Rosario
 *
 */
public class GameCalculations {

	private GameState gameState = null;

	private boolean isSideA;
	private Player currentPlayer;
	private ArrayList<Player> players;
	private ArrayList<Card> display;

	public GameCalculations(GameState gameState) {
		this.gameState = gameState;

		// shortcuts
		this.isSideA = this.gameState.isCardSideA();
		int playersTurn = this.gameState.getPlayersTurn();
		this.currentPlayer = this.gameState.getBoard().getPlayers().get(playersTurn);
		this.players = this.gameState.getBoard().getPlayers();
		this.display = this.gameState.getBoard().getDisplay();
	}

	public void payMeeplesForCard(int selectedIndex) {
		// displayIndex = meeples to play [0] -> cost 0; [1] -> cost 1; [2] -> cost 2
		currentPlayer.setMeeples(currentPlayer.getMeeples() - selectedIndex);

		// add 1 meeple up to displaycard before the selected card
		for (int i = 0; i < selectedIndex; i++) {
			Card displayCard = display.get(i);
			int meeples = displayCard.getMeeples();
			meeples += 1;
			displayCard.setMeeples(meeples);
		}
	}

	public void receiveMeeplesOfCard(Card selectedCard) {
		// move card meeples to player
		int meeples = currentPlayer.getMeeples();
		meeples += selectedCard.getMeeples();
		currentPlayer.setMeeples(meeples);
		selectedCard.setMeeples(0);
	}
	
	public void convertMeepleOverflowToPoints() {
		int meeples = currentPlayer.getMeeples();
		
		if (meeples > 5) {
			int points = currentPlayer.getPoints();
			points += (meeples - 5);
			currentPlayer.setPoints(points);
			currentPlayer.setMeeples(5);
		}
	}

	public void distributePoints(CardType type) {
		switch (type) {
		case Miller:
			this.calcMiller();
			break;

		case Brewer:
			this.calcBrewer();
			break;

		case Witch:
			this.calcWitch();
			break;

		case Guard:
			this.calcGuard();
			break;

		case Knight:
			this.calcKnight();
			break;

		case Innkeeper:
			this.calcInnkeeper();
			break;

		case Noble:
			this.calcNoble();
			break;

		default:
			break;
		}
	}

	public void tradeMeeples(int meeplesToTrade) {
		int points = currentPlayer.getPoints();
		int meeples = currentPlayer.getMeeples();

		// meeplesToTrade positive -> buy meeples
		// meeplesToTrade negative -> sell meeples
		if (meeplesToTrade > 0) {
			meeples += meeplesToTrade;
			points -= meeplesToTrade;

		} else if (meeplesToTrade < 0) {
			meeples -= Math.abs(meeplesToTrade);
			points += Math.abs(meeplesToTrade);
		}

		currentPlayer.setMeeples(meeples);
		currentPlayer.setPoints(points);
	}

	public void distributeFinalScoring() {
		// TO-DO: final scoring
		// 1. infirmary
		// 2. variety
		// 3. majority
	}

	private void calcMiller() {
		int millerCount = currentPlayer.getLocationByIndex(Location.MILL).getCards().size();
		int points = currentPlayer.getPoints();

		// for side A and B: per miller -> 2 points
		points += (millerCount * 2);
		currentPlayer.setPoints(points);

		// side B only: min. 1 witch -> 3 points
		if (!isSideA) {
			for (Player player : players) {
				if (player.getLocationByIndex(Location.COTTAGE).getCards().size() > 0) {
					points = player.getPoints();
					points += 3;
					player.setPoints(points);
				}
			}
		}
	}

	private void calcBrewer() {

	}

	private void calcWitch() {

	}

	private void calcGuard() {

	}

	private void calcKnight() {

	}

	private void calcInnkeeper() {

	}

	private void calcNoble() {
		// ignore first effect of side B
	}

}
