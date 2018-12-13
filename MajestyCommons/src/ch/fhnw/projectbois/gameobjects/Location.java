package ch.fhnw.projectbois.gameobjects;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Location {

	public static final int MILL = 0;
	public static final int BREWERY = 1;
	public static final int COTTAGE = 2;
	public static final int GUARDHOUSE = 3;
	public static final int BARACKS = 4;
	public static final int INN = 5;
	public static final int CASTLE = 6;
	public static final int INFIRMARY = 7;
	public static final int MAX_COUNT = 8;

	public static final int OFFSET_B = 10;

	public static final int A1 = 0;
	public static final int A2 = 1;
	public static final int A3 = 2;
	public static final int A4 = 3;
	public static final int A5 = 4;
	public static final int A6 = 5;
	public static final int A7 = 6;
	public static final int A8 = 7;

	public static final int B1 = 10;
	public static final int B2 = 11;
	public static final int B3 = 12;
	public static final int B4 = 13;
	public static final int B5 = 14;
	public static final int B6 = 15;
	public static final int B7 = 16;
	public static final int B8 = 17;

	private boolean majorityWinner;

	private ArrayList<Card> cards;

	public Location() {
		this.cards = new ArrayList<>();
		this.majorityWinner = false;
	}

	public ArrayList<Card> getCards() {
		return this.cards;
	}

	public boolean getMajorityWinner() {
		return majorityWinner;
	}

	public void setMajorityWinner() {
		this.majorityWinner = true;
	}

	public void unsetMajorityWinner() {
		this.majorityWinner = false;
	}

	@JsonIgnore
	public static int getMajorityPoints(int locationIndex, boolean isSideA) {
		int points = 0;

		switch (locationIndex) {
		case MILL:
			if (isSideA) {
				points = 10;
			} else {
				points = 14;
			}
			break;
		case BREWERY:
			if (isSideA) {
				points = 11;
			} else {
				points = 12;
			}
			break;
		case COTTAGE:
			// A and B
			points = 12;
			break;
		case GUARDHOUSE:
			if (isSideA) {
				points = 13;
			} else {
				points = 8;
			}
			break;
		case BARACKS:
			if (isSideA) {
				points = 14;
			} else {
				points = 8;
			}
			break;
		case INN:
			if (isSideA) {
				points = 15;
			} else {
				points = 12;
			}
			break;
		case CASTLE:
			//A and B
			points = 16;
			break;
		case INFIRMARY:
			if (!isSideA) {
				points = -10;
			}
			break;

		default:
			break;
		}

		return points;
	}

}
