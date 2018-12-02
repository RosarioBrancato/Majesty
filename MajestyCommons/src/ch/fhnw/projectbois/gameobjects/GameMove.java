package ch.fhnw.projectbois.gameobjects;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GameMove {
	
	public static final int DECISION_NONE = -999;

	private int displayCardIndexSelected = -1;

	private ArrayList<Integer> decisions = new ArrayList<>();
	private int nextDecision = 0;

	public int getDisplayCardIndexSelected() {
		return displayCardIndexSelected;
	}

	public void setDisplayCardIndexSelected(int cardIndexSelected) {
		this.displayCardIndexSelected = cardIndexSelected;
	}

	public ArrayList<Integer> getDecisions() {
		return decisions;
	}

	public void setDecisions(ArrayList<Integer> decisions) {
		this.decisions = decisions;
	}

	@JsonIgnore
	public int getNextDecision() {
		int decision = DECISION_NONE;

		if (this.nextDecision < this.decisions.size()) {
			decision = this.decisions.get(this.nextDecision);
		}

		this.nextDecision++;

		return decision;
	}

}
