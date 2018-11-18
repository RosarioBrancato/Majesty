package ch.fhnw.projectbois.network;

import java.util.ArrayList;
import java.util.logging.Logger;

import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.MessageDTO;
import ch.fhnw.projectbois.dto.UserDTO;
import ch.fhnw.projectbois.game.GameStateServer;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.general.IdFactory;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.log.LoggerFactory;

/**
 * 
 * @author Rosario Brancato
 *
 */
public class Lobby {

	private Logger logger = null;

	private int id = -1;
	private boolean cardSideA = true;

	private ArrayList<ServerClient> clients = new ArrayList<>();

	private GameState gameState;
	private GameStateServer gameStateServer;

	public Lobby() {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.id = IdFactory.getInstance().getNewId(this.getClass().getName());
	}

	public synchronized boolean addClient(ServerClient client) {
		boolean success = false;

		if (this.isNotFull()) {
			client.setLobby(this);
			this.clients.add(client);
			success = true;
		}

		return success;
	}

	public boolean removeClient(ServerClient client) {
		return this.clients.removeIf(f -> f.getUser().getId() == client.getUser().getId());
	}

	public boolean isNotFull() {
		return this.clients.size() < 4;
	}

	public boolean isEmpty() {
		return this.clients.size() <= 0;
	}

	public void startGame() {
		this.gameState = new GameState();
		this.gameStateServer = new GameStateServer();
	}

	public void doMove(UserDTO user, String json) {
		this.logger.info("Lobby.doMove() - Token: " + user.getToken() + " JSON: " + json);
	}

	public void updateGameState(GameState gameState) {
		for (ServerClient client : this.clients) {
			String json = JsonUtils.Serialize(gameState);
			Response response = new Response(ResponseId.UPDATE_GAMESTATE, RequestId.DO_MOVE, json);

			client.sendResponse(response);
		}
	}

	public void sendMessage(MessageDTO message) {
		for (ServerClient client : this.clients) {
			String json = JsonUtils.Serialize(message);
			Response response = new Response(ResponseId.RECEIVE_MSG, RequestId.CHAT_SEND_MSG, json);

			client.sendResponse(response);
		}
	}

	public int getId() {
		return this.id;
	}

	public ArrayList<ServerClient> getClients() {
		return this.clients;
	}

	public boolean isCardSideA() {
		return cardSideA;
	}

	public void setCardSideA(boolean cardSideA) {
		this.cardSideA = cardSideA;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public GameStateServer getGameStateServer() {
		return gameStateServer;
	}

	public void setGameStateServer(GameStateServer gameStateServer) {
		this.gameStateServer = gameStateServer;
	}

}
