package ch.fhnw.projectbois.requesthandlers;

import ch.fhnw.projectbois.auth.TokenFactory;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.UserDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;

public class AuthRequestHandler extends RequestHandler {

	public AuthRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);
	}

	@Override
	protected void handleRequest() {
		if(request.getRequestId() == RequestId.LOGIN) {
			//TO-DO: real login
			TokenFactory token = TokenFactory.getInstance();
			UserDTO user = new UserDTO(1, "Alex", token.getNewToken());
			String json = JsonUtils.Serialize(user);
			Response response = new Response(ResponseId.AUTH_OK, request.getRequestId(), json);
			client.sendResponse(response);
		}
	}

}
