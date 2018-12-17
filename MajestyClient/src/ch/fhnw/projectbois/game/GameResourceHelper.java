package ch.fhnw.projectbois.game;

import ch.fhnw.projectbois.gameobjects.Card;
import ch.fhnw.projectbois.gameobjects.CardType;

/**
 * The Class GameResourceHelper.
 * 
 * @author Rosario Brancato
 */
public class GameResourceHelper {

	private final String PATH_TO_CARD = "game/cards/character%20cards/";
	private final String PATH_TO_SPLIT_CARD = "game/cards/split%20cards/";
	private final String PATH_TO_CARDBACKS = "game/cards/backs/";

	/**
	 * Gets the url by card.
	 *
	 * @param card the card
	 * @return the url by card
	 */
	public String getUrlByCard(Card card) {
		String url = "";

		if (card != null) {
			if (card.isSplitCard()) {
				if (card.getCardType1() == CardType.Guard && card.getCardType2() == CardType.Knight) {
					url = PATH_TO_SPLIT_CARD + "Blue%20Red.jpg";

				} else if (card.getCardType1() == CardType.Guard && card.getCardType2() == CardType.Noble) {
					url = PATH_TO_SPLIT_CARD + "Blue%20Violet.jpg";

				} else if (card.getCardType1() == CardType.Guard && card.getCardType2() == CardType.Innkeeper) {
					url = PATH_TO_SPLIT_CARD + "Blue%20Yellow.jpg";

				} else if (card.getCardType1() == CardType.Brewer && card.getCardType2() == CardType.Witch) {
					url = PATH_TO_SPLIT_CARD + "Brown%20Green.jpg";

				} else if (card.getCardType1() == CardType.Brewer && card.getCardType2() == CardType.Knight) {
					url = PATH_TO_SPLIT_CARD + "Brown%20Red.jpg";

				} else if (card.getCardType1() == CardType.Witch && card.getCardType2() == CardType.Guard) {
					url = PATH_TO_SPLIT_CARD + "Green%20Blue.jpg";

				} else if (card.getCardType1() == CardType.Witch && card.getCardType2() == CardType.Noble) {
					url = PATH_TO_SPLIT_CARD + "Green%20Violet.jpg";

				} else if (card.getCardType1() == CardType.Witch && card.getCardType2() == CardType.Innkeeper) {
					url = PATH_TO_SPLIT_CARD + "Green%20Yellow.jpg";

				} else if (card.getCardType1() == CardType.Miller && card.getCardType2() == CardType.Brewer) {
					url = PATH_TO_SPLIT_CARD + "Orange%20Brown.jpg";

				} else if (card.getCardType1() == CardType.Miller && card.getCardType2() == CardType.Knight) {
					url = PATH_TO_SPLIT_CARD + "Orange%20Red.jpg";

				} else if (card.getCardType1() == CardType.Knight && card.getCardType2() == CardType.Innkeeper) {
					url = PATH_TO_SPLIT_CARD + "Red%20Yellow.jpg";

				} else if (card.getCardType1() == CardType.Innkeeper && card.getCardType2() == CardType.Noble) {
					url = PATH_TO_SPLIT_CARD + "Yellow%20Violet.jpg";
				}

			} else {
				switch (card.getCardTypeActive()) {
				case Miller:
					url = PATH_TO_CARD + "Orange.jpg";
					break;
				case Brewer:
					url = PATH_TO_CARD + "Brown.jpg";
					break;
				case Guard:
					url = PATH_TO_CARD + "Blue.jpg";
					break;
				case Innkeeper:
					url = PATH_TO_CARD + "Yellow.jpg";
					break;
				case Knight:
					url = PATH_TO_CARD + "Red.jpg";
					break;
				case Noble:
					url = PATH_TO_CARD + "Violet.jpg";
					break;
				case Witch:
					url = PATH_TO_CARD + "Green.jpg";
					break;
				}
			}
		}

		return url;
	}

	/**
	 * Gets the url by card back.
	 *
	 * @param deckBack the deck back
	 * @return the url by card back
	 */
	public String getUrlByCardBack(int deckBack) {
		String url = "";

		switch (deckBack) {
		case Card.BACK_TIER1:
			url = PATH_TO_CARDBACKS + "Back%201.jpg";
			break;
		case Card.BACK_TIER2:
			url = PATH_TO_CARDBACKS + "Back%202.jpg";
			break;
		case Card.BACK_NONE:
			break;
		}

		return url;
	}
	
}
