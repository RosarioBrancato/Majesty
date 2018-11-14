package ch.fhnw.projectbois.dto;

import java.util.ArrayList;

public class LobbyDTO {

	private int id = -1;
	private boolean cardSideA = true;

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
	
	public void addPlayer(String player) {
		this.players.add(player);
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
	
	public boolean isCardSideA() {
		return cardSideA;
	}

	public void setCardSideA(boolean cardSideA) {
		this.cardSideA = cardSideA;
	}
	
}
