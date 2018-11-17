package ch.fhnw.projectbois.dto;

import java.util.ArrayList;

public class LobbyListDTO {

	private ArrayList<LobbyDTO> lobbies = null;
	
	public LobbyListDTO() {
		this.lobbies = new ArrayList<>();
	}
	
	public ArrayList<LobbyDTO> getLobbies() {
		return this.lobbies;
	}
	
}
