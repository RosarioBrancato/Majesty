package ch.fhnw.projectbois.requesthandlers;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;

public abstract class RequestHandler {

	protected Request request;
	protected Server server;
	protected ServerClient client;

	protected RequestHandler(Request request, Server server, ServerClient client) {
		this.request = request;
		this.server = server;
		this.client = client;
	}

	protected abstract void handleRequest();

}
