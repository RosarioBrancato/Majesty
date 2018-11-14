package ch.fhnw.projectbois.gameobjects;

import java.util.ArrayList;

public class Board {

	private int round = 1;
	private int playersTurn = 1;
	private ArrayList<Player> players;
	private ArrayList<DisplayCard> display;

	public Board() {
		this.players = new ArrayList<>();
		this.display = new ArrayList<>();
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getPlayersTurn() {
		return playersTurn;
	}

	public void setPlayersTurn(int playersTurn) {
		this.playersTurn = playersTurn;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public ArrayList<DisplayCard> getDisplay() {
		return display;
	}

}
