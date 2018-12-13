package ch.fhnw.projectbois.gameend;

import java.util.ArrayList;
import java.util.Collections;

import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.gameobjects.Player;

public class GameEndModel extends Model {

	public ArrayList<Player> determineRanking(ArrayList<Player> players) {
		Collections.sort(players, (player1, player2) -> player1.getPoints() - player2.getPoints());
		Collections.reverse(players);

		for (Player player : players) {
			System.out.println(player.getUsername() + player.getPoints());
		}
		
		return players;
	}

}
