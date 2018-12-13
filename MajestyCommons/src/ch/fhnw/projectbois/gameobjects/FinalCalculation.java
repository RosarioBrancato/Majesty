package ch.fhnw.projectbois.gameobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FinalCalculation {

	private boolean malus;
	private int gameCount;
	private int infirmaryCount;
	private int locationCount;
	private int majorityCount;

	public boolean isMalus() {
		return malus;
	}

	public void setMalus(boolean malus) {
		this.malus = malus;
	}

	public int getGameCount() {
		return gameCount;
	}

	public void setGameCount(int gameCount) {
		this.gameCount = gameCount;
	}

	public int getInfirmaryCount() {
		return infirmaryCount;
	}

	public void setInfirmaryCount(int infirmaryCount) {
		this.infirmaryCount = infirmaryCount;
	}

	public int getLocationCount() {
		return locationCount;
	}

	public void setLocationCount(int locationCount) {
		this.locationCount = locationCount;
	}

	public int getMajorityCount() {
		return majorityCount;
	}

	public void setMajorityCount(int majorityCount) {
		this.majorityCount = majorityCount;
	}

	@JsonIgnore
	public int getTotalCount() {
		int total = gameCount + infirmaryCount + locationCount + majorityCount;
		if (malus) {
			total = Math.abs(total) * -1;
		}

		return total;
	}

}
