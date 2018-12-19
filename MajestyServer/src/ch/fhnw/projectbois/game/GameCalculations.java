package ch.fhnw.projectbois.game;

import java.util.ArrayList;

import ch.fhnw.projectbois.gameobjects.Card;
import ch.fhnw.projectbois.gameobjects.CardType;
import ch.fhnw.projectbois.gameobjects.FinalCalculation;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.gameobjects.Location;
import ch.fhnw.projectbois.gameobjects.Player;

/**
 * 
 * @author Leeroy Koller & Dario Stoeckli
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
	
	/**
	 * This handles all the final scoring when the game ended,
	 * which is the case if only one player is left or 12 rounds have been played
	 *
	 */
	public void distributeFinalScoring() {
		// final scoring
		for (Player player : players) {
			player.setFinalCalculation(new FinalCalculation());
			player.getFinalCalculation().setGameCount(player.getPoints());

			// 1. infirmary
			int infirmaryCount = player.getLocationByIndex(Location.INFIRMARY).getCards().size();
			player.getFinalCalculation().setInfirmaryCount(infirmaryCount * -1);

			// 2. variety - ignore infirmary
			int locationCount = 0;
			for (int i = 0; i < Location.MAX_COUNT - 1; i++) {
				if (player.getLocationByIndex(i).getCards().size() > 0) {
					locationCount++;
				}
			}
			locationCount *= locationCount;
			// update points for player and turn to the final calculations with majority
			player.getFinalCalculation().setLocationCount(locationCount);
		}

		// 3. majority
		int highestCount = 0;
		ArrayList<Player> majorityWinners = new ArrayList<>();

		for (int i = 0; i < Location.MAX_COUNT; i++) {
			highestCount = 0;
			majorityWinners.clear();

			for (Player player : players) {
				int size = player.getLocationByIndex(i).getCards().size();
				if (size > highestCount) {
					highestCount = size;
					majorityWinners.clear();
					majorityWinners.add(player);

				} else if (size == highestCount) {
					majorityWinners.add(player);
				}
			}

			if (highestCount > 0) {
				for (Player player : majorityWinners) {
					int majorityPoints = player.getFinalCalculation().getMajorityCount();
					majorityPoints += Location.getMajorityPoints(i, isSideA);
					player.getFinalCalculation().setMajorityCount(majorityPoints);
				}
			}
		}

		// note down total score
		for (Player player : players) {
			// if player left before the end of the game, deduct collected points as
			// punishment
			if (player.isPlayerLeft()) {
				player.getFinalCalculation().setMalus(true);
			}
			
			player.setPoints(player.getFinalCalculation().getTotalCount());
		}
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
		} else {
			meeples += (brewerCount * 1);
			meeples += (millerCount * 1);
			currentPlayer.setMeeples(meeples);

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
		} else {
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
		} else {
			points += (brewerCount * 2);
			points += (witchCount * 2);
			points += (guardCount * 2);
			currentPlayer.setPoints(points);

			for (Player player : players) {
				if (player.getLocationByIndex(Location.INN).getCards().size() > 0) {
					points = player.getPoints();
					points += 3;
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
		} else {
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
			currentPlayer.setPoints(points);

			for (Player player : players) {
				if (player.getLocationByIndex(Location.BREWERY).getCards().size() > 0) {
					points = player.getPoints();
					points += 3;
					player.setPoints(points);
				}
			}
		} else {
			int highestCount = 0;

			for (int i = 0; i < currentPlayer.getLocations().length; i++) {
				int cardCount = currentPlayer.getLocationByIndex(i).getCards().size();

				if (cardCount > highestCount) {
					highestCount = cardCount;
				}
			}

			points += (innkeeperCount * (highestCount * 2));
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
			currentPlayer.setMeeples(meeples);

		} else {
			// ignore first effect of side B

			points += (nobleCount * 4);
			points += (infirmaryCount * 4);

			currentPlayer.setPoints(points);
		}

	}

}
