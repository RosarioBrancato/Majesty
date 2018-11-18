package ch.fhnw.projectbois.gameobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Card {

	private boolean splitCard = false;
	private CardType cardType1;
	private CardType cardType2;
	private int activeCardType = 1;

	/**
	 * For JSON Serializer
	 */
	public Card() {
		
	}
	
	public Card(CardType cardType) {
		this(cardType, null);
	}

	public Card(CardType cardType1, CardType cardType2) {
		this.cardType1 = cardType1;
		this.cardType2 = cardType2;

		if (this.cardType2 != null) {
			this.splitCard = true;
		}
	}

	public CardType getCardType1() {
		return cardType1;
	}

	public CardType getCardType2() {
		return cardType2;
	}

	public boolean isSplitCard() {
		return splitCard;
	}

	public int getActiveCardType() {
		return this.activeCardType;
	}


	@JsonIgnore
	public void setActiveCardType(int activeCardType) {
		if (this.splitCard) {
			this.activeCardType = activeCardType;
		}
	}

	@JsonIgnore
	public CardType getCardTypeActive() {
		switch (activeCardType) {
		case 1:
			return this.getCardType1();

		case 2:
			return this.getCardType2();

		default:
			return this.getCardType1();
		}
	}

}
