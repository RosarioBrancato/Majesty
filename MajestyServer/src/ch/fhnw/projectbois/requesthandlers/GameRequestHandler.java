package ch.fhnw.projectbois.requesthandlers;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.game.GameLogic;
import ch.fhnw.projectbois.game.GameStateServer;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.general.IdFactory;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Lobby;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;

public class GameRequestHandler extends RequestHandler {

	public GameRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);
	}

	@Override
	protected void handleRequest() {
		if (request.getRequestId() == RequestId.GET_GAMESTATE) {
			this.getGameState();

		} else if (request.getRequestId() == RequestId.DO_MOVE) {
			this.doMove();

		} else if (request.getRequestId() == RequestId.START_GAME) {
			this.startGame();

		} else if (request.getRequestId() == RequestId.QUIT_GAME) {
			this.quitGame();
		}
	}

	private void getGameState() {
		GameState gameState = client.getLobby().getGameState();
		String json = JsonUtils.Serialize(gameState);

		Response response = new Response(ResponseId.UPDATE_GAMESTATE, request.getRequestId(), json);

		client.sendResponse(response);
	}

	private void doMove() {
		// TO-DO
		// client.getLobby().doMove(client.getUser(), request.getJsonDataObject());
	}

	private void startGame() {
		Lobby lobby = client.getLobby();

		GameState gameState = new GameState();
		gameState.setId(IdFactory.getInstance().getNewId(GameState.class.getName()));
		gameState.setCardSideA(lobby.isCardSideA());

		GameStateServer gameStateServer = new GameStateServer();

		GameLogic logic = new GameLogic(gameState, gameStateServer);
		logic.fillDecks();
		logic.definePlayers(lobby);
		logic.setCardsAside();
		logic.fillDisplay();

		lobby.setGameState(gameState);
		lobby.setGameStateServer(gameStateServer);
		lobby.setGameStarted(true);

		Response response = new Response(ResponseId.GAME_STARTED, request.getRequestId(), request.getJsonDataObject());
		for (ServerClient c : lobby.getClients()) {
			c.sendResponse(response);
		}
	}

	private void quitGame() {

	}

}
