package ch.fhnw.projectbois.network;

import java.util.ArrayList;

import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.game.GameLogic;
import ch.fhnw.projectbois.game.GameStateServer;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.gameobjects.Player;
import ch.fhnw.projectbois.general.IdFactory;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.time.Time;
import javafx.beans.value.ChangeListener;

/**
 * 
 * @author Rosario Brancato
 *
 */
public class Lobby {
	
	public static final int LIFETIME_DEFAULT = 360;

	private int id = -1;
	private boolean cardSideA = true;

	private Time timer = null;
	private ChangeListener<Number> periodicCounterPropertyListener = null;
	private int lifetime = -1;

	private ArrayList<ServerClient> clients = null;

	private boolean gameStarted = false;
	private GameState gameState;
	private GameStateServer gameStateServer;

	public Lobby() {
		this.id = IdFactory.getInstance().getNewId(this.getClass().getName());
		this.clients = new ArrayList<>();
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

	public synchronized boolean removeClient(ServerClient client) {
		boolean success = this.clients.removeIf(f -> f.getUser().getId() == client.getUser().getId());

		if (this.gameStarted) {
			// update game state object
			Player player = gameState.getBoard().getPlayers().stream()
					.filter(f -> f.getUsername().equals(client.getUser().getUsername())).findFirst().get();

			GameLogic logic = new GameLogic(gameState, gameStateServer);
			logic.removePlayer(player);
		}

		this.sendResponseToPlayersLeft();

		return success;
	}

	public synchronized boolean isNotFull() {
		return this.clients.size() < 4;
	}

	public synchronized boolean isEmpty() {
		return this.clients.size() <= 0;
	}

	public void startCountdown(int lifetime) {
		this.lifetime = lifetime;

		this.timer = new Time();
		timer.startCountdown(lifetime);
		this.initPeriodicCounterPropertyListener();
		this.timer.getPeriodCounterProperty().addListener(this.periodicCounterPropertyListener);
	}

	public void stopCountdown() {
		if (this.timer != null) {
			this.timer.stop();
			this.timer.getPeriodCounterProperty().removeListener(this.periodicCounterPropertyListener);
			this.timer = null;
		}
	}

	public LobbyDTO toLobbyDTO() {
		LobbyDTO dto = new LobbyDTO();
		dto.setId(this.id);
		dto.setCardSideA(this.cardSideA);
		dto.setLifetime(this.lifetime);

		for (ServerClient client : this.clients) {
			dto.addPlayer(client.getUser().getUsername());
		}

		return dto;
	}

	public void destroy() {
		this.stopCountdown();
	}

	private void initPeriodicCounterPropertyListener() {
		this.periodicCounterPropertyListener = (observer, oldValue, newValue) -> {
			if (lifetime > 0) {
				lifetime--;
			}
		};
	}

	private void sendResponseToPlayersLeft() {
		Response response = null;

		if (this.gameStarted) {
			// Response for other players in game
			GameState gameState = this.getGameState();
			int clientsCount = this.getClients().size();

			String json = JsonUtils.Serialize(gameState);
			if (clientsCount > 1) {
				// message other players about a player leaving
				response = new Response(ResponseId.GAME_PLAYER_LEFT, RequestId.EMPTY, json);

			} else if (clientsCount == 1) {
				// only 1 player left in the lobby -> game ended
				response = new Response(ResponseId.GAME_ENDED, RequestId.EMPTY, json);
			}

		} else {
			// Response for other players in lobby
			LobbyDTO lobbyDTO = this.toLobbyDTO();
			String json = JsonUtils.Serialize(lobbyDTO);
			response = new Response(ResponseId.LOBBY_LEFT_MULTICAST, RequestId.EMPTY, json);
		}

		// send response
		if (response != null) {
			for (ServerClient c : this.getClients()) {
				c.sendResponse(response);
			}
		}
	}

// GETTER AND SETTER

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

	public boolean isGameStarted() {
		return this.gameStarted;
	}

	public void setGameStarted(boolean started) {
		this.gameStarted = started;
	}

	public int getLifetime() {
		return this.lifetime;
	}

	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}

}
