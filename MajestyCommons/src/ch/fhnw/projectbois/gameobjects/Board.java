package ch.fhnw.projectbois.gameobjects;

import java.util.ArrayList;

public class Board {

	public static final int DECKBACK_EMPTY = 0;
	public static final int DECKBACK_TIER1 = 1;
	public static final int DECKBACK_TIER2 = 2;

	private int round = 1;
	private int playersTurn = 1;
	private ArrayList<Player> players;
	private ArrayList<DisplayCard> display;
	private int deckBack = DECKBACK_TIER1;
	private int cardsLeft = -1;

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

	public int getDeckBack() {
		return deckBack;
	}

	public void setDeckBack(int deckBack) {
		this.deckBack = deckBack;
	}

	public int getCardsLeft() {
		return cardsLeft;
	}

	public void setCardsLeft(int cardsLeft) {
		this.cardsLeft = cardsLeft;
	}

}
