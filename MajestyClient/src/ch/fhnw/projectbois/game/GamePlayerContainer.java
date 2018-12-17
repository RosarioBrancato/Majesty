package ch.fhnw.projectbois.game;

import javafx.scene.control.Label;

/**
 * The Class GamePlayerContainer.
 */
public class GamePlayerContainer {

	private String username = null;
	private int playerRow = -1;
	
	private Label lblUsername = null;
	private Label lblPoints = null;
	private Label lblMeeples = null;

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the player row.
	 *
	 * @return the player row
	 */
	public int getPlayerRow() {
		return playerRow;
	}

	/**
	 * Sets the player row.
	 *
	 * @param playerRow the new player row
	 */
	public void setPlayerRow(int playerRow) {
		this.playerRow = playerRow;
	}

	/**
	 * Gets the lbl username.
	 *
	 * @return the lbl username
	 */
	public Label getLblUsername() {
		return lblUsername;
	}

	/**
	 * Sets the lbl username.
	 *
	 * @param lblUsername the new lbl username
	 */
	public void setLblUsername(Label lblUsername) {
		this.lblUsername = lblUsername;
	}

	/**
	 * Gets the lbl points.
	 *
	 * @return the lbl points
	 */
	public Label getLblPoints() {
		return lblPoints;
	}

	/**
	 * Sets the lbl points.
	 *
	 * @param lblPoints the new lbl points
	 */
	public void setLblPoints(Label lblPoints) {
		this.lblPoints = lblPoints;
	}

	/**
	 * Gets the lbl meeples.
	 *
	 * @return the lbl meeples
	 */
	public Label getLblMeeples() {
		return lblMeeples;
	}

	/**
	 * Sets the lbl meeples.
	 *
	 * @param lblMeeples the new lbl meeples
	 */
	public void setLblMeeples(Label lblMeeples) {
		this.lblMeeples = lblMeeples;
	}

}
