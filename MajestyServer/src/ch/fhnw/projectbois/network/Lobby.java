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
 * The Class Lobby.
 *
 * @author Rosario Brancato
 */
public class Lobby {

	public static final int LIFETIME_DEFAULT = 360;

	private int id = -1;
	private boolean cardSideA = true;

	private Time lifetimer = null;
	private ChangeListener<Number> lifetimerPropertyListener = null;
	private int lifetime = -1;

	private Time turnTimer = null;
	private ChangeListener<Number> turnTimerPropertyListener = null;

	private ArrayList<ServerClient> clients = null;

	private boolean gameStarted = false;
	private GameState gameState;
	private GameStateServer gameStateServer;

	/**
	 * Instantiates a new lobby.
	 */
	public Lobby() {
		this.id = IdFactory.getInstance().getNewId(this.getClass().getName());
		this.clients = new ArrayList<>();
	}

	/**
	 * Adds the client.
	 *
	 * @param client the client
	 * @return true, if successful
	 */
	public synchronized boolean addClient(ServerClient client) {
		boolean success = false;

		if (this.isNotFull()) {
			client.setLobby(this);
			this.clients.add(client);
			success = true;
		}

		return success;
	}

	/**
	 * Removes the client.
	 *
	 * @param client the client
	 * @return true, if successful
	 */
	public synchronized boolean removeClient(ServerClient client) {
		boolean success = this.clients.removeIf(f -> f.getUser().getId() == client.getUser().getId());

		if (this.gameStarted) {
			this.stopTurnTimer();

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

	/**
	 * Start lifetimer.
	 *
	 * @param lifetime the lifetime
	 */
	public void startLifetimer(int lifetime) {
		this.lifetime = lifetime;

		this.lifetimer = new Time();
		lifetimer.startCountdown(lifetime);
		this.initPeriodicCounterPropertyListener();
		this.lifetimer.getPeriodCounterProperty().addListener(this.lifetimerPropertyListener);
	}

	/**
	 * Stop lifetimer.
	 */
	public void stopLifetimer() {
		if (this.lifetimer != null) {
			this.lifetimer.stop();
			this.lifetimer.getPeriodCounterProperty().removeListener(this.lifetimerPropertyListener);
			this.lifetimer = null;
		}
	}

	/**
	 * Start turn timer.
	 */
	public void startTurnTimer() {
		if (this.gameStarted && !this.gameState.isGameEnded()) {
			this.gameState.setTurntimer(GameStateServer.TURN_TIMER);

			if (this.turnTimer == null) {
				this.turnTimer = new Time();
				this.initPeriodCounterPropertyListener();
				this.turnTimer.getPeriodCounterProperty().addListener(this.turnTimerPropertyListener);
				this.turnTimer.startCountdown(GameStateServer.TURN_TIMER);
			} else {
				this.turnTimer.setCounter(GameStateServer.TURN_TIMER);
			}
		}
	}

	/**
	 * Stop turn timer.
	 */
	public void stopTurnTimer() {
		if (this.turnTimer != null) {
			this.turnTimer.stop();
			this.turnTimer.getPeriodCounterProperty().removeListener(this.turnTimerPropertyListener);
			this.turnTimer = null;
		}
	}

	/**
	 * To lobby DTO.
	 *
	 * @return the lobby DTO
	 */
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

	/**
	 * Destroy.
	 */
	public void destroy() {
		this.stopLifetimer();
		this.stopTurnTimer();
	}

	/**
	 * Inits the periodic counter property listener.
	 */
	private void initPeriodicCounterPropertyListener() {
		this.lifetimerPropertyListener = (observer, oldValue, newValue) -> {
			if (lifetime > 0) {
				lifetime--;
			}
		};
	}

	/**
	 * Inits the period counter property listener.
	 */
	private void initPeriodCounterPropertyListener() {
		this.turnTimerPropertyListener = (observer, oldValue, newValue) -> {
			if (gameState.getTurntimer() > 0) {
				gameState.setTurntimer(gameState.getTurntimer() - 1);
			}

			if (newValue.intValue() == 0) {
				this.sendResponseTurnTimeOver();
			}
		};
	}

	/**
	 * Send response to players left.
	 */
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

			} else if (clientsCount == 1 || gameState.isGameEnded()) {
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
		for (ServerClient c : this.getClients()) {
			c.sendResponse(response);
		}
	}

	/**
	 * Send response turn time over.
	 */
	private void sendResponseTurnTimeOver() {
		if (this.gameStarted && !this.gameState.isGameEnded()) {
			this.stopTurnTimer();

			// find current player's client
			Player currentPlayer = this.gameState.getBoard().getPlayers().get(this.gameState.getPlayersTurn());

			// start new turn
			GameLogic logic = new GameLogic(this.gameState, this.gameStateServer);
			logic.startNextTurn();
			this.startTurnTimer();

			// tell player his turn time is over
			String json = JsonUtils.Serialize(this.gameState);

			for (ServerClient client : this.clients) {
				Response response;
				if (client.getUser().getUsername().equals(currentPlayer.getUsername())) {
					response = new Response(ResponseId.GAME_TURN_TIME_OVER, RequestId.EMPTY, json);
				} else {
					response = new Response(ResponseId.UPDATE_GAMESTATE, RequestId.EMPTY, json);
				}
				client.sendResponse(response);
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
