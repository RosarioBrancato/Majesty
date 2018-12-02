package ch.fhnw.projectbois.dto;

import java.util.ArrayList;

public class LeaderboardDTO {

private ArrayList<LeaderboardPlayerDTO> playerranks = null;
	
	public LeaderboardDTO() {
		this.playerranks = new ArrayList<>();
	}
	
	public ArrayList<LeaderboardPlayerDTO> getLeaderboard() {
		return this.playerranks;
	}
	
	public void addToLeaderboard(LeaderboardPlayerDTO player) {
		playerranks.add(player);
	}
}
