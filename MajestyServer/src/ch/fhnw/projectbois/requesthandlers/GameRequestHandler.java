package ch.fhnw.projectbois.requesthandlers;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.game.GameLogic;
import ch.fhnw.projectbois.game.GameStateServer;
import ch.fhnw.projectbois.gameobjects.GameMove;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.gameobjects.Player;
import ch.fhnw.projectbois.general.IdFactory;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Lobby;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;

/**
 * 
 * @author Rosario
 *
 */
public class GameRequestHandler extends RequestHandler {

	public GameRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);
	}

	@Override
	protected void handleRequest() {
		if (request.getRequestId() == RequestId.GET_GAMESTATE) {
			this.updateGameState();

		} else if (request.getRequestId() == RequestId.DO_MOVE) {
			this.doMove();

		} else if (request.getRequestId() == RequestId.START_GAME) {
			this.startGame();

		} else if (request.getRequestId() == RequestId.lEAVE_GAME) {
			this.leaveGame();
		}
	}

	private void updateGameState() {
		GameState gameState = client.getLobby().getGameState();
		String json = JsonUtils.Serialize(gameState);

		Response response = new Response(ResponseId.UPDATE_GAMESTATE, request.getRequestId(), json);

		Lobby lobby = client.getLobby();
		for (ServerClient c : lobby.getClients()) {
			c.sendResponse(response);
		}
	}

	private void doMove() {
		String json = request.getJsonDataObject();
		GameMove gameMove = JsonUtils.Deserialize(json, GameMove.class);

		Lobby lobby = client.getLobby();
		GameState gameState = lobby.getGameState();
		GameStateServer gameStateServer = lobby.getGameStateServer();

		if (!gameStateServer.isGameEnded()) {

			GameLogic logic = new GameLogic(gameState, gameStateServer);

			logic.executeMove(client.getUser().getUsername(), gameMove);
			boolean gameOver = logic.startNextTurn();

			Response response;
			json = JsonUtils.Serialize(gameState);

			if (!gameOver) {
				response = new Response(ResponseId.UPDATE_GAMESTATE, request.getRequestId(), json);

			} else {
				logic.endGame();

				response = new Response(ResponseId.GAME_ENDED, request.getRequestId(), json);
			}

			for (ServerClient c : lobby.getClients()) {
				c.sendResponse(response);
			}
		}
	}

	private void startGame() {
		Lobby lobby = client.getLobby();

		if (!lobby.isGameStarted()) {
			GameState gameState = new GameState();
			gameState.setId(IdFactory.getInstance().getNewId(GameState.class.getName()));
			gameState.setCardSideA(lobby.isCardSideA());

			GameStateServer gameStateServer = new GameStateServer();

			GameLogic logic = new GameLogic(gameState, gameStateServer);
			logic.fillDecks();
			logic.definePlayers(lobby);
			logic.setCardsAside();
			logic.fillDisplay();
			logic.startNextTurn();

			lobby.setGameState(gameState);
			lobby.setGameStateServer(gameStateServer);
			lobby.setGameStarted(true);

			Response response = new Response(ResponseId.GAME_STARTED, request.getRequestId(),
					request.getJsonDataObject());

			for (ServerClient c : lobby.getClients()) {
				c.sendResponse(response);
			}
		}
	}

	private void leaveGame() {
		Lobby lobby = client.getLobby();
		GameState gameState = lobby.getGameState();

		// update game state object
		Player player = gameState.getBoard().getPlayers().stream()
				.filter(f -> f.getUsername().equals(client.getUser().getUsername())).findFirst().get();

		GameLogic logic = new GameLogic(lobby.getGameState(), lobby.getGameStateServer());
		logic.removePlayer(player);

		lobby.getClients().remove(client);
		int clientsCount = lobby.getClients().size();

		Response response = null;
		String json = JsonUtils.Serialize(gameState);
		if (clientsCount > 1) {
			// message other players about a player leaving
			response = new Response(ResponseId.GAME_PLAYER_LEFT, request.getRequestId(), json);

		} else if (clientsCount == 1) {
			// only 1 player left in the lobby -> game ended
			response = new Response(ResponseId.GAME_ENDED, request.getRequestId(), json);
		}

		if (response != null) {
			for (ServerClient c : lobby.getClients()) {
				c.sendResponse(response);
			}
		}

		// last player left the lobby -> remove lobby
		if (lobby.getClients().size() >= 0) {
			server.getLobbies().remove(lobby);
		}
	}

}
