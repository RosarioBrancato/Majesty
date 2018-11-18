package ch.fhnw.projectbois.gameobjects;

public class GameMove {

	private int displayCardIndexSelected = -1;
	private int decision1 = -1;
	private int decision2 = -1;

	public int getDisplayCardIndexSelected() {
		return displayCardIndexSelected;
	}

	public void setDisplayCardIndexSelected(int cardIndexSelected) {
		this.displayCardIndexSelected = cardIndexSelected;
	}

	public int getDecision1() {
		return decision1;
	}

	public void setDecision1(int decision1) {
		this.decision1 = decision1;
	}

	public int getDecision2() {
		return decision2;
	}

	public void setDecision2(int decision2) {
		this.decision2 = decision2;
	}

}
