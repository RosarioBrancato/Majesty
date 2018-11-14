package ch.fhnw.projectbois.requesthandlers;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.dto.MessageDTO;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;

public class ChatRequestHandler extends RequestHandler {

	public ChatRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);
	}

	@Override
	protected void handleRequest() {
		if(request.getRequestId() == RequestId.CHAT_SEND_MSG) {
			//TO-DO: send msg;
			//Dummy code
			MessageDTO message = new MessageDTO();
			client.getLobby().sendMessage(message);
		}
	}

}
