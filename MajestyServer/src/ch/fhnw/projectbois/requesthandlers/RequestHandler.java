package ch.fhnw.projectbois.requesthandlers;

import java.util.logging.Logger;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.log.LoggerFactory;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;

/**
 * 
 * @author Rosario Brancato
 *
 */
public abstract class RequestHandler {

	protected Logger logger;
	
	protected Request request;
	protected Server server;
	protected ServerClient client;

	public RequestHandler(Request request, Server server, ServerClient client) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		
		this.request = request;
		this.server = server;
		this.client = client;
		
		this.handleRequest();
	}

	protected abstract void handleRequest();

}
