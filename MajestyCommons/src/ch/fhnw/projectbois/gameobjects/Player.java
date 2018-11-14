package ch.fhnw.projectbois.gameobjects;

public class Player {

	private Location[] locations;
	private int meeples;
	private int score;
	
	public Player() {
		this.locations = new Location[6];
	}

	public Location[] getLocations() {
		return locations;
	}

	public int getMeeples() {
		return meeples;
	}

	public void setMeeples(int meeples) {
		this.meeples = meeples;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
