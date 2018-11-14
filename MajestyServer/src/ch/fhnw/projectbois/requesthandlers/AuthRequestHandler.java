package ch.fhnw.projectbois.requesthandlers;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;

public class AuthRequestHandler extends RequestHandler {

	public AuthRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);

		this.handleRequest();
	}

	@Override
	protected void handleRequest() {
		if(request.getRequestId() == RequestId.LOGIN) {
			//TO-DO: login
		}
	}

}
