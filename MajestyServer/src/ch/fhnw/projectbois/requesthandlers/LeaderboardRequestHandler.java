package ch.fhnw.projectbois.requesthandlers;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;

public class LeaderboardRequestHandler extends RequestHandler {

	public LeaderboardRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);

		this.handleRequest();
	}

	@Override
	protected void handleRequest() {
		if(request.getRequestId() == RequestId.GET_LEADERBOARD) {
			//TO-DO: get leaderboard
		}
	}

}
