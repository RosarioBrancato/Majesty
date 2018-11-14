package ch.fhnw.projectbois.network;

import java.util.ArrayList;
import java.util.logging.Logger;

import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.MessageDTO;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.log.LoggerFactory;

public class Lobby {

	private static int NEXT_ID = 1;
	
	private Logger logger = null;

	private int id = -1;
	private ArrayList<ServerClient> clients = new ArrayList<>();

	public Lobby() {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.id = getNewId();
	}

	public boolean addClient(ServerClient client) {
		boolean success = false;

		if (this.isNotFull()) {
			client.setLobby(this);
			this.clients.add(client);
			success = true;
		}

		return success;
	}

	public boolean removeClient(ServerClient client) {
		return this.clients.removeIf(f -> f.getToken().equals(client.getToken()));
	}

	public boolean isNotFull() {
		return this.clients.size() < 4;
	}

	public void doMove(String clientToken, String json) {
		this.logger.info("Lobby.doMove() - Token: " + clientToken + " JSON: " + json);
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

	private static int getNewId() {
		return NEXT_ID++;
	}

	public int getId() {
		return this.id;
	}

}
