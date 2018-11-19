package ch.fhnw.projectbois.game;

import java.util.ArrayList;
import java.util.Collections;

import ch.fhnw.projectbois.gameobjects.Board;
import ch.fhnw.projectbois.gameobjects.CardType;
import ch.fhnw.projectbois.gameobjects.DisplayCard;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.gameobjects.Player;
import ch.fhnw.projectbois.network.Lobby;
import ch.fhnw.projectbois.network.ServerClient;

public class GameLogic {

	public void definePlayers(Lobby lobby, GameState gameState) {
		ArrayList<ServerClient> clients = lobby.getClients();

		for (ServerClient c : clients) {
			Player player = new Player();
			player.setUserToken(c.getUser().getToken());
			player.setUsername(c.getUser().getToken());

			player.setMeeples(5);

			gameState.getBoard().getPlayers().add(player);
		}
	}

	public void fillDecks(GameStateServer gameStateServer) {
		// TIER 2
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Miller));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Brewer));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Witch));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Guard));
		}
		gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Knight));
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Innkeeper));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Noble));
		}
		gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Miller, CardType.Brewer));
		gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Miller, CardType.Knight));
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Brewer, CardType.Witch));
		}
		gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Brewer, CardType.Knight));
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Witch, CardType.Guard));
		}
		gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Witch, CardType.Innkeeper));
		gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Witch, CardType.Noble));
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Guard, CardType.Knight));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Knight, CardType.Innkeeper));
		}
		gameStateServer.getDeckTier2().add(new DisplayCard(CardType.Innkeeper, CardType.Noble));

		// TIER 1
		for (int i = 0; i < 7; i++) {
			gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Miller));
		}
		for (int i = 0; i < 4; i++) {
			gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Brewer));
		}
		for (int i = 0; i < 3; i++) {
			gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Witch));
		}
		for (int i = 0; i < 3; i++) {
			gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Guard));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Knight));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Innkeeper));
		}
		for (int i = 0; i < 3; i++) {
			gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Noble));
		}
		for (int i = 0; i < 2; i++) {
			gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Miller, CardType.Brewer));
		}
		gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Miller, CardType.Knight));
		gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Brewer, CardType.Witch));
		gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Witch, CardType.Guard));
		gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Guard, CardType.Innkeeper));
		gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Guard, CardType.Noble));
		gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Knight, CardType.Innkeeper));
		gameStateServer.getDeckTier1().add(new DisplayCard(CardType.Innkeeper, CardType.Noble));

		Collections.shuffle(gameStateServer.getDeckTier2());
		Collections.shuffle(gameStateServer.getDeckTier1());
	}

	public void setCardsAside(GameState gameState, GameStateServer gameStateServer) {
		int cardsToRemove = 0;

		switch (gameState.getBoard().getPlayers().size()) {
		case 2:
			cardsToRemove = 6;
			break;
		case 3:
			cardsToRemove = 14;
			break;
		case 4:
			cardsToRemove = 26;
			break;
		}

		for (int i = 0; i < cardsToRemove; i++) {
			gameStateServer.getDeckTier1().remove(0);
		}
	}
	
	public void fillDisplay(GameState gameState, GameStateServer gameStateServer) {
		for(int i = 0; i < 6; i++) {
			DisplayCard card = this.drawACard(gameState, gameStateServer);
			gameState.getBoard().getDisplay().add(card);
		}
	}
	
	
	private DisplayCard drawACard(GameState gameState, GameStateServer gameStateServer) {
		DisplayCard card = null;
		
		if(gameStateServer.getDeckTier1().size() > 0) {
			card = gameStateServer.getDeckTier1().get(0);
			gameStateServer.getDeckTier1().remove(0);
			
		} else if(gameStateServer.getDeckTier2().size() > 0) {
			card = gameStateServer.getDeckTier2().get(0);
			gameStateServer.getDeckTier2().remove(0);
		}
		
		//Deck back for display
		if(gameStateServer.getDeckTier1().size() > 0) {
			gameState.getBoard().setDeckBack(Board.DECKBACK_TIER1);
		} else if(gameStateServer.getDeckTier2().size() > 0) {
			gameState.getBoard().setDeckBack(Board.DECKBACK_TIER2);
		} else {
			gameState.getBoard().setDeckBack(Board.DECKBACK_EMPTY);
		}
		
		return card;
	}

}
