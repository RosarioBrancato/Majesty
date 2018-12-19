package ch.fhnw.projectbois.dto;

import java.util.ArrayList;

/**
 * The Class LobbyListDTO.
 * @author Dario Stöckli
 */
public class LobbyListDTO {

	private ArrayList<LobbyDTO> lobbies = null;
	
	/**
	 * Instantiates a new lobby list DTO.
	 * Adds an ArrayList that hold each LobbyDTO in it, this will be the object used
	 * to display all the available Lobbies on the Client
	 */
	public LobbyListDTO() {
		this.lobbies = new ArrayList<>();
	}
	
	/**
	 * Gets the lobbies.
	 *
	 * @return the lobbies
	 */
	public ArrayList<LobbyDTO> getLobbies() {
		return this.lobbies;
	}
	
}
