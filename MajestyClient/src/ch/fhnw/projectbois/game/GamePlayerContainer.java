package ch.fhnw.projectbois.game;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GamePlayerContainer {

	private String username = null;

	private GridPane pnlCards = null;
	private Label lblInfo = null;
	private Label[] locationCardCount = null;
	
	public GamePlayerContainer() {
		this.locationCardCount = new Label[8];
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public GridPane getPnlCards() {
		return pnlCards;
	}

	public void setPnlCards(GridPane pnlCards) {
		this.pnlCards = pnlCards;
	}

	public Label getLblInfo() {
		return lblInfo;
	}

	public void setLblInfo(Label lblInfo) {
		this.lblInfo = lblInfo;
	}

	public Label getLabelCardCountByIndex(int index) {
		return this.locationCardCount[index];
	}
	
	public void setLabelCardCountByIndex(int index, Label label) {
		this.locationCardCount[index] = label;
	}

}
