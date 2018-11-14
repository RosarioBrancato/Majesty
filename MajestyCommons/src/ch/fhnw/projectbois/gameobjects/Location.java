package ch.fhnw.projectbois.gameobjects;

import java.util.ArrayList;

public class Location {

	private ArrayList<Card> cards;
	
	public Location() {
		this.cards = new ArrayList<>();
	}
	
	public ArrayList<Card> getCards() {
		return this.cards;
	}
	
}
