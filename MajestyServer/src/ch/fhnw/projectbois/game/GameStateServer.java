package ch.fhnw.projectbois.game;

import java.util.ArrayList;

import ch.fhnw.projectbois.gameobjects.DisplayCard;

public class GameStateServer {

	private ArrayList<DisplayCard> deckTier1;
	private ArrayList<DisplayCard> deckTier2;

	public GameStateServer() {
		this.deckTier1 = new ArrayList<>();
		this.deckTier2 = new ArrayList<>();
	}

	public ArrayList<DisplayCard> getDeckTier1() {
		return this.deckTier1;
	}
	
	public ArrayList<DisplayCard> getDeckTier2() {
		return this.deckTier2;
	}

}
