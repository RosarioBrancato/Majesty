/*
 * Leaderboard data transfer object (DTO) to exchange between server and client
 */
package ch.fhnw.projectbois.dto;

import java.util.ArrayList;

/**
 * @author Dario Stoeckli
 * 
 * Instantiates a new Leaderboard DTO 
 */
public class LeaderboardDTO {

/** ArrayList that holds LeaderboardPlayerDTO - this represents the ranking */
private ArrayList<LeaderboardPlayerDTO> playerranks = null;
	
	/**
	 * Instantiates a new Leaderboard DTO.
	 */
	public LeaderboardDTO() {
		this.playerranks = new ArrayList<>();
	}
	
	/**
	 * Gets the leaderboard.
	 *
	 * @return the leaderboard
	 */
	public ArrayList<LeaderboardPlayerDTO> getLeaderboard() {
		return this.playerranks;
	}
	
	/**
	 * Adds the to leaderboard.
	 *
	 * @param player the player
	 */
	public void addToLeaderboard(LeaderboardPlayerDTO player) {
		playerranks.add(player);
	}
}
