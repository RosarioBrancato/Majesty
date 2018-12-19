package ch.fhnw.projectbois.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class LobbyDTO.
 * @author Dario Stoeckli
 */
public class LobbyDTO {

	private int id = -1;
	private boolean cardSideA = true;
	private int lifetime = -1;
	private ArrayList<String> players = new ArrayList<>();

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the players.
	 *
	 * @return the players
	 */
	public ArrayList<String> getPlayers() {
		return this.players;
	}

	/**
	 * Sets the players.
	 *
	 * @param players the new players
	 */
	public void setPlayers(ArrayList<String> players) {
		this.players = players;
	}

	/**
	 * Checks if is card side A.
	 *
	 * @return true, if is card side A
	 */
	public boolean isCardSideA() {
		return cardSideA;
	}

	/**
	 * Sets the card side A.
	 *
	 * @param cardSideA the new card side A
	 */
	public void setCardSideA(boolean cardSideA) {
		this.cardSideA = cardSideA;
	}
	
	/**
	 * Gets the lifetime.
	 *
	 * @return the lifetime
	 */
	public int getLifetime() {
		return this.lifetime;
	}
	
	/**
	 * Sets the lifetime.
	 *
	 * @param lifetime the new lifetime
	 */
	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}
	
	/**
	 * Adds the player.
	 *
	 * @param player the player
	 */
	@JsonIgnore
	public void addPlayer(String player) {
		this.players.add(player);
	}
	
	/**
	 * Removes the player.
	 *
	 * @param player the player
	 */
	@JsonIgnore
	public void removePlayer(String player) {
		this.players.remove(player);
	}

	/**
	 * Gets the players as string.
	 *
	 * @return the players as string
	 */
	@JsonIgnore
	public String getPlayersAsString() {
		String players = "";

		for (String player : this.players) {
			players += player;
			players += " ";
		}

		return players;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * This is used to display the Lobby in a nice way on the clients ListView
	 */
	@Override
	public String toString() {
		String side = "";
		if(this.isCardSideA()) {
			side = "A";
		} else {
			side = "B";
		}
		
		return "Players: " + this.getPlayersAsString() + " Card Side: " + side; 
	}

}
