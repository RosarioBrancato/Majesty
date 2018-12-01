package ch.fhnw.projectbois.gameobjects;

import java.util.ArrayList;

public class Board {

	public static final int DECKBACK_EMPTY = 0;
	public static final int DECKBACK_TIER1 = 1;
	public static final int DECKBACK_TIER2 = 2;
	
	private ArrayList<Player> players;
	private ArrayList<Card> display;
	private int deckBack = DECKBACK_TIER1;
	private int cardsLeft = -1;

	public Board() {
		this.players = new ArrayList<>();
		this.display = new ArrayList<>();
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public ArrayList<Card> getDisplay() {
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
