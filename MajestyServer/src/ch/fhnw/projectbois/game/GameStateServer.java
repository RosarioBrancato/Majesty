package ch.fhnw.projectbois.game;

import java.util.ArrayList;

import ch.fhnw.projectbois.gameobjects.Card;

public class GameStateServer {

	private ArrayList<Card> deckTier1;
	private ArrayList<Card> deckTier2;
	private boolean gameEnded = false;

	public GameStateServer() {
		this.deckTier1 = new ArrayList<>();
		this.deckTier2 = new ArrayList<>();
	}

	public ArrayList<Card> getDeckTier1() {
		return this.deckTier1;
	}
	
	public ArrayList<Card> getDeckTier2() {
		return this.deckTier2;
	}

	public boolean isGameEnded() {
		return gameEnded;
	}

	public void setGameEnded(boolean gameEnded) {
		this.gameEnded = gameEnded;
	}

}
