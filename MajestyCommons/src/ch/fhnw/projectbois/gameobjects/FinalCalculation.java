package ch.fhnw.projectbois.gameobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * The Class FinalCalculation.
 * @author Dario Stoeckli
 */
public class FinalCalculation {

	private boolean malus;
	private int gameCount;
	private int infirmaryCount;
	private int locationCount;
	private int majorityCount;

	/**
	 * Checks if is malus.
	 *
	 * @return true, if is malus
	 */
	public boolean isMalus() {
		return malus;
	}

	/**
	 * Sets the malus.
	 *
	 * @param malus the new malus
	 */
	public void setMalus(boolean malus) {
		this.malus = malus;
	}

	/**
	 * Gets the game count.
	 *
	 * @return the game count
	 */
	public int getGameCount() {
		return gameCount;
	}

	/**
	 * Sets the game count.
	 *
	 * @param gameCount the new game count
	 */
	public void setGameCount(int gameCount) {
		this.gameCount = gameCount;
	}

	/**
	 * Gets the infirmary count.
	 *
	 * @return the infirmary count
	 */
	public int getInfirmaryCount() {
		return infirmaryCount;
	}

	/**
	 * Sets the infirmary count.
	 *
	 * @param infirmaryCount the new infirmary count
	 */
	public void setInfirmaryCount(int infirmaryCount) {
		this.infirmaryCount = infirmaryCount;
	}

	/**
	 * Gets the location count.
	 *
	 * @return the location count
	 */
	public int getLocationCount() {
		return locationCount;
	}

	/**
	 * Sets the location count.
	 *
	 * @param locationCount the new location count
	 */
	public void setLocationCount(int locationCount) {
		this.locationCount = locationCount;
	}

	/**
	 * Gets the majority count.
	 *
	 * @return the majority count
	 */
	public int getMajorityCount() {
		return majorityCount;
	}

	/**
	 * Sets the majority count.
	 *
	 * @param majorityCount the new majority count
	 */
	public void setMajorityCount(int majorityCount) {
		this.majorityCount = majorityCount;
	}

	/**
	 * Gets the total count.
	 *
	 * @return the total count
	 */
	@JsonIgnore
	public int getTotalCount() {
		int total = gameCount + infirmaryCount + locationCount + majorityCount;
		if (malus) {
			total = Math.abs(total) * -1;
		}

		return total;
	}

}
