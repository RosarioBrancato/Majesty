package ch.fhnw.projectbois.gameobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GameMove {

	private int displayCardIndexSelected = -1;
	private int decision1 = -1;
	private int decision2 = -1;
	
	private int nextDecision = 1;

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
	
	@JsonIgnore
	public int getNextDecision() {
		int decision = -1;
		
		if(this.nextDecision == 1) {
			decision = this.getDecision1();
		} else if(this.nextDecision == 2) {
			decision = this.getDecision2();
		}
		
		this.nextDecision++;
		
		return decision;
	}

}
