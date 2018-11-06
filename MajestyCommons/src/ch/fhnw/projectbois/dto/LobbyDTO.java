package ch.fhnw.projectbois.dto;

import java.util.ArrayList;

public class LobbyDTO {

	private int id = -1;
	private ArrayList<String> players = new ArrayList<>();
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public ArrayList<String> getPlayers() {
		return this.players;
	}
	
	public void setPlayers(ArrayList<String> players) {
		this.players = players;
	}
	
	public String getPlayersAsString() {
		String players = "";
		
		for(String player : this.players) {
			players += player;
			players += " ";
		}
		
		return players;
	}
	
}
