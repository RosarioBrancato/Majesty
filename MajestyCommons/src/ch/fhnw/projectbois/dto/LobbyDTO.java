package ch.fhnw.projectbois.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LobbyDTO {

	private int id = -1;
	private boolean cardSideA = true;
	private int lifetime = -1;

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

	public boolean isCardSideA() {
		return cardSideA;
	}

	public void setCardSideA(boolean cardSideA) {
		this.cardSideA = cardSideA;
	}
	
	public int getLifetime() {
		return this.lifetime;
	}
	
	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}
	
	@JsonIgnore
	public void addPlayer(String player) {
		this.players.add(player);
	}
	
	@JsonIgnore
	public void removePlayer(String player) {
		this.players.remove(player);
	}

	@JsonIgnore
	public String getPlayersAsString() {
		String players = "";

		for (String player : this.players) {
			players += player;
			players += " ";
		}

		return players;
	}
	
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
