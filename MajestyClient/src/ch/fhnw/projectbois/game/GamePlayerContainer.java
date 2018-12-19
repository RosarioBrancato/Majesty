package ch.fhnw.projectbois.game;

import javafx.scene.control.Label;

/**
 * This class holds the most relevant references to controls in the GUI, to make
 * its changes more accessible in the code.
 */
public class GamePlayerContainer {

	private String username = null;
	private int playerRow = -1;

	private Label lblUsername = null;
	private Label lblPoints = null;
	private Label lblMeeples = null;

	/**
	 * Gets the username of the player.
	 *
	 * @return username of the player
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username of the player.
	 *
	 * @param new username of the player
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the player row int the main grid component.
	 *
	 * @return the player row
	 */
	public int getPlayerRow() {
		return playerRow;
	}

	/**
	 * Sets the player row int the main grid component.
	 *
	 * @param new player row
	 */
	public void setPlayerRow(int playerRow) {
		this.playerRow = playerRow;
	}

	/**
	 * Gets the label username.
	 *
	 * @return the label username
	 */
	public Label getLblUsername() {
		return lblUsername;
	}

	/**
	 * Sets the label username.
	 *
	 * @param lblUsername the new label username
	 */
	public void setLblUsername(Label lblUsername) {
		this.lblUsername = lblUsername;
	}

	/**
	 * Gets the label points.
	 *
	 * @return the label points
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
	 * Gets the label meeples.
	 *
	 * @return the label meeples
	 */
	public Label getLblMeeples() {
		return lblMeeples;
	}

	/**
	 * Sets the label meeples.
	 *
	 * @param lblMeeples the new label meeples
	 */
	public void setLblMeeples(Label lblMeeples) {
		this.lblMeeples = lblMeeples;
	}

}
