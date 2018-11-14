package ch.fhnw.projectbois.game;

import java.util.ArrayList;

import ch.fhnw.projectbois.gameobjects.Card;

public class GameStateServer {

	private ArrayList<Card> deck;

	public GameStateServer() {
		this.deck = new ArrayList<>();
	}

	public ArrayList<Card> getDeck() {
		return this.deck;
	}

}
