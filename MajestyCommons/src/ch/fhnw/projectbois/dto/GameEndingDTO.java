package ch.fhnw.projectbois.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.fhnw.projectbois.gameobjects.Player;

public class GameEndingDTO {
	
	//private ArrayList<PlayerEndStateDTO> participants;
	private ArrayList<Player> participants;
	
	public GameEndingDTO() {
		this.participants = new ArrayList<>();
	}
	
	public ArrayList<Player> getParticipants() {
		return participants;
	}
	
	public void setParticipants(ArrayList<Player> participants) {
		this.participants = participants;
	}
	
	@JsonIgnore
	public void addToPlayerList(Player player) {
		participants.add(player);
	}
}
