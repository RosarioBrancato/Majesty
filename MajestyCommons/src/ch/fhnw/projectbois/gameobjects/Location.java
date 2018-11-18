package ch.fhnw.projectbois.gameobjects;

import java.util.ArrayList;

public class Location {
	
	public static final int A1 = 1;
	public static final int A2 = 2;
	public static final int A3 = 3;
	public static final int A4 = 4;
	public static final int A5 = 5;
	public static final int A6 = 6;
	public static final int A7 = 7;
	public static final int A8 = 8;
	
	public static final int B1 = 11;
	public static final int B2 = 12;
	public static final int B3 = 13;
	public static final int B4 = 14;
	public static final int B5 = 15;
	public static final int B6 = 16;
	public static final int B7 = 17;
	public static final int B8 = 18;

	private ArrayList<Card> cards;
	
	public Location() {
		this.cards = new ArrayList<>();
	}
	
	public ArrayList<Card> getCards() {
		return this.cards;
	}
	
}
