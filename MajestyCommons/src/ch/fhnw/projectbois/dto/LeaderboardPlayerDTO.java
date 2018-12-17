/*
 * Leaderboard Player data transfer object (DTO) to exchange between server and client
 */
package ch.fhnw.projectbois.dto;


/**
 * @author Dario Stoeckli
 * 
 * Instantiates a new LeaderboardPlayer DTO
 * Every Player represents one object, the information is being queried from the database
 * and held in this DTO that will be sent to the Client in an ArrayList of LeaderboardPlayer DTO's
 */
public class LeaderboardPlayerDTO {

	private int rank;
	private String username;
	private int points;
	
	/**
	 * Gets the rank.
	 *
	 * @return the rank
	 */
	public int getRank() {
		return this.rank;
	}
	
	/**
	 * Sets the rank.
	 *
	 * @param rank the new rank
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Gets the points.
	 *
	 * @return the points
	 */
	public int getPoints() {
		return this.points;
	}
	
	/**
	 * Sets the points.
	 *
	 * @param points the new points
	 */
	public void setPoints(int points) {
		this.points = points;
	}
	
	/* 
	 * @see java.lang.Object#toString()
	 * Method overrides the toString method to nicely display the ranking in the GUI
	 */
	@Override
	public String toString() {
		String ranking = getRank() + ") " + getUsername() + " - Points: "  + getPoints();
		return ranking;
	}
	
}
