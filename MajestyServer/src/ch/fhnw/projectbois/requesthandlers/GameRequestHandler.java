package ch.fhnw.projectbois.requesthandlers;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.game.GameLogic;
import ch.fhnw.projectbois.game.GameStateServer;
import ch.fhnw.projectbois.gameobjects.GameMove;
import ch.fhnw.projectbois.gameobjects.GameState;
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
		Lobby lobby = client.getLobby();
		if (lobby != null && lobby.isGameStarted()) {
			GameState gameState = lobby.getGameState();
			String json = JsonUtils.Serialize(gameState);

			Response response = new Response(ResponseId.UPDATE_GAMESTATE, request.getRequestId(), json);

			for (ServerClient c : lobby.getClients()) {
				c.sendResponse(response);
			}
		}
	}

	private void doMove() {
		Lobby lobby = client.getLobby();
		if (lobby != null && lobby.isGameStarted() && !lobby.getGameState().isGameEnded()) {
			lobby.stopTurnTimer();

			String json = request.getJsonDataObject();
			GameMove gameMove = JsonUtils.Deserialize(json, GameMove.class);

			GameState gameState = lobby.getGameState();
			GameStateServer gameStateServer = lobby.getGameStateServer();

			if (!gameStateServer.isGameEnded()) {

				GameLogic logic = new GameLogic(gameState, gameStateServer);

				logic.executeMove(client.getUser().getUsername(), gameMove);
				boolean gameOver = logic.startNextTurn();

				Response response;

				if (!gameOver) {
					lobby.startTurnTimer();
					json = JsonUtils.Serialize(gameState);
					response = new Response(ResponseId.UPDATE_GAMESTATE, request.getRequestId(), json);

				} else {
					logic.endGame();
					json = JsonUtils.Serialize(gameState);
					response = new Response(ResponseId.GAME_ENDED, request.getRequestId(), json);
				}

				for (ServerClient c : lobby.getClients()) {
					c.sendResponse(response);
				}
			}
		}
	}

	private void startGame() {
		Lobby lobby = client.getLobby();
		if (lobby != null && !lobby.isGameStarted()) {

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
			lobby.startTurnTimer();

			Response response = new Response(ResponseId.GAME_STARTED, request.getRequestId(),
					request.getJsonDataObject());

			for (ServerClient c : lobby.getClients()) {
				c.sendResponse(response);
			}

		}
	}

	private void leaveGame() {
		server.removeClientFromLobby(client);
	}

}
