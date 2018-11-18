package ch.fhnw.projectbois.gameobjects;

public class Player {

	private Location[] locations;
	private int meeples;
	private int score;

	private String userToken;
	private String username;

	public Player() {
		this.locations = new Location[8];
		for (int i = 0; i < this.locations.length; i++) {
			this.locations[i] = new Location();
		}
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

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
