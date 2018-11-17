package ch.fhnw.projectbois.requesthandlers;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;

public class GameRequestHandler extends RequestHandler {
	

	public GameRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);
	}

	@Override
	protected void handleRequest() {
		if (request.getRequestId() == RequestId.DO_MOVE) {
			this.doMove();
		}
	}

	private void doMove() {
		//TO-DO
		//client.getLobby().doMove(client.getUser(), request.getJsonDataObject());
	}
	
	
}
