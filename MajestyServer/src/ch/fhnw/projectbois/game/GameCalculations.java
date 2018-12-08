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
			if (points >= meeplesToTrade) {
				meeples += meeplesToTrade;
				points -= meeplesToTrade;
			}

		} else if (meeplesToTrade < 0) {
			if (meeples >= meeplesToTrade) {
				meeples -= Math.abs(meeplesToTrade);
				points += Math.abs(meeplesToTrade);
			}
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
		int millerCount = currentPlayer.getLocationByIndex(Location.MILL).getCards().size();
		int brewerCount = currentPlayer.getLocationByIndex(Location.BREWERY).getCards().size();
		int innkeeperCount = currentPlayer.getLocationByIndex(Location.INN).getCards().size();
		int nobleCount = currentPlayer.getLocationByIndex(Location.CASTLE).getCards().size();

		int points = currentPlayer.getPoints();
		int meeples = currentPlayer.getMeeples();

		// for side A
		if (isSideA) {
			points += (brewerCount * 2);
			currentPlayer.setPoints(points);
			meeples += (brewerCount * 1); // does this work??
			currentPlayer.setMeeples(meeples);

			for (Player player : players) {
				if (player.getLocationByIndex(Location.MILL).getCards().size() > 0) {
					points = player.getPoints();
					points += 2;
					player.setPoints(points);
				}
			}
			// for side B
		} else if (!isSideA) {
			meeples += (brewerCount * 1);
			meeples += (millerCount * 1);

			if (innkeeperCount > 0 && nobleCount > 0) {
				points += 10;
			}
			currentPlayer.setPoints(points);
		}	
	}

	private void calcWitch() {
		int millerCount = currentPlayer.getLocationByIndex(Location.MILL).getCards().size();
		int brewerCount = currentPlayer.getLocationByIndex(Location.BREWERY).getCards().size();
		int witchCount = currentPlayer.getLocationByIndex(Location.COTTAGE).getCards().size();

		int points = currentPlayer.getPoints();
	
		if (isSideA) {
			points += (millerCount * 2);
			points += (brewerCount * 2);
			points += (witchCount * 2);
			currentPlayer.setPoints(points);
		} else if (!isSideA) {
			points += (witchCount * 3);
			currentPlayer.setPoints(points);
		}
	}

	private void calcGuard() {
		int brewerCount = currentPlayer.getLocationByIndex(Location.BREWERY).getCards().size();
		int witchCount = currentPlayer.getLocationByIndex(Location.COTTAGE).getCards().size();
		int guardCount = currentPlayer.getLocationByIndex(Location.GUARDHOUSE).getCards().size();
		int knightCount = currentPlayer.getLocationByIndex(Location.BARACKS).getCards().size();
		int innkeeperCount = currentPlayer.getLocationByIndex(Location.INN).getCards().size();

		int points = currentPlayer.getPoints();
		
		if (isSideA) {
			points += (guardCount * 2);
			points += (knightCount * 2);
			points += (innkeeperCount * 2);
			currentPlayer.setPoints(points);
		} else if (!isSideA) {
			points += (brewerCount * 2);
			points += (witchCount * 2);
			points += (guardCount * 2);
			
			for (Player player : players) {
				if (player.getLocationByIndex(Location.INN).getCards().size() > 0) {
					points = player.getPoints();
					points += 2;
					player.setPoints(points);
				}
			}
		}
	}

	private void calcKnight() {
		int knightCount = currentPlayer.getLocationByIndex(Location.BARACKS).getCards().size();
		int innkeeperCount = currentPlayer.getLocationByIndex(Location.INN).getCards().size();
		int nobleCount = currentPlayer.getLocationByIndex(Location.CASTLE).getCards().size();

		int points = currentPlayer.getPoints();
		
		if (isSideA) {
			points += (knightCount * 3);
			currentPlayer.setPoints(points);
		} else if (!isSideA) {
			points += (knightCount * 3);
			points += (innkeeperCount * 3);
			points += (nobleCount * 3);
			currentPlayer.setPoints(points);
		}

	}

	private void calcInnkeeper() {
		int innkeeperCount = currentPlayer.getLocationByIndex(Location.INN).getCards().size();

		int points = currentPlayer.getPoints();

		if (isSideA) {
			points += (innkeeperCount * 4);
			
			for (Player player : players) {
				if (player.getLocationByIndex(Location.BREWERY).getCards().size() > 0) {
					points = player.getPoints();
					points += 3;
					player.setPoints(points);
				}
			}
		} else if (!isSideA) {
			points += (innkeeperCount * 2); // what does the sign on card mean???
			currentPlayer.setPoints(points);
		}

	}

	private void calcNoble() {
		int nobleCount = currentPlayer.getLocationByIndex(Location.CASTLE).getCards().size();
		int infirmaryCount = currentPlayer.getLocationByIndex(Location.INFIRMARY).getCards().size();

		
		int points = currentPlayer.getPoints();
		int meeples = currentPlayer.getMeeples();

		if (isSideA) {
			points += (nobleCount * 5);
			meeples += (nobleCount * 1);
			currentPlayer.setPoints(points);
		} else if (!isSideA) {
			// ignore first effect of side B
			
			points += (nobleCount * 4);
			points += (infirmaryCount * 4);
			
			currentPlayer.setPoints(points);

		}
		
		
		
	}

}
