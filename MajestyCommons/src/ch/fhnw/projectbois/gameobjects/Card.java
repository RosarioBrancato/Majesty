package ch.fhnw.projectbois.gameobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Card {

	public static final int BACK_NONE = 0;
	public static final int BACK_TIER1 = 1;
	public static final int BACK_TIER2 = 2;

	private boolean splitCard = false;
	private CardType cardType1;
	private CardType cardType2;
	private int activeCardType = 1;
	private int cardBack = -1;
	private int meeples = 0;

	/**
	 * For JSON Serializer
	 */
	public Card() {

	}

	public Card(CardType cardType1, CardType cardType2, int cardBack) {
		this.cardType1 = cardType1;
		this.cardType2 = cardType2;
		this.cardBack = cardBack;

		if (this.cardType2 != null) {
			this.splitCard = true;
		}
	}

	public Card(CardType cardType, int cardBack) {
		this(cardType, null, cardBack);
	}

	public int getActiveCardType() {
		return this.activeCardType;
	}

	public int getCardBack() {
		return cardBack;
	}

	public CardType getCardType1() {
		return cardType1;
	}

	public CardType getCardType2() {
		return cardType2;
	}

	public int getMeeples() {
		return meeples;
	}

	public boolean isSplitCard() {
		return splitCard;
	}

	public void setActiveCardType(int activeCardType) {
		this.activeCardType = activeCardType;
	}

	public void setCardBack(int cardBack) {
		this.cardBack = cardBack;
	}

	public void setCardType1(CardType cardType1) {
		this.cardType1 = cardType1;
	}

	public void setCardType2(CardType cardType2) {
		this.cardType2 = cardType2;
	}

	public void setMeeples(int meeples) {
		this.meeples = meeples;
	}

	public void setSplitCard(boolean splitCard) {
		this.splitCard = splitCard;
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
