package ch.fhnw.projectbois.gameobjects;

public class DisplayCard extends Card {

	private int meeples = 0;

	/**
	 * For JSON Serializer
	 */
	public DisplayCard() {
		
	}
	
	public DisplayCard(CardType cardType) {
		super(cardType);
	}

	public DisplayCard(CardType cardType1, CardType cardType2) {
		super(cardType1, cardType2);
	}

	public int getMeeples() {
		return meeples;
	}

	public void setMeeples(int meeples) {
		this.meeples = meeples;
	}

}
