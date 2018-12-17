package ch.fhnw.projectbois.gameend;

import java.util.ArrayList;
import java.util.Collections;

import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.gameobjects.Player;

/**
 * The Class GameEndModel.
 * @author Dario Stoeckli
 */
public class GameEndModel extends Model {

	/**
	 * Determine ranking.
	 *
	 * @param players the players (from the gameState Object)
	 * @return the array list (sorted according to points)
	 */
	public ArrayList<Player> determineRanking(ArrayList<Player> players) {
		Collections.sort(players, (player1, player2) -> player1.getPoints() - player2.getPoints());
		Collections.reverse(players);
		
		return players;
	}

}
