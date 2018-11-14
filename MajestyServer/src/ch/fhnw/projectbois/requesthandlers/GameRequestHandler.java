package ch.fhnw.projectbois.requesthandlers;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.network.Lobby;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;

public class GameRequestHandler extends RequestHandler {
	
	private Lobby lobby;

	public GameRequestHandler(Request request, Server server, ServerClient client, Lobby lobby) {
		super(request, server, client);
		
		this.lobby = lobby;
		
		this.handleRequest();
	}

	@Override
	protected void handleRequest() {
		if (request.getRequestId() == RequestId.DO_MOVE) {
			lobby.doMove(client.getToken(), request.getJsonDataObject());
		}
	}

}
