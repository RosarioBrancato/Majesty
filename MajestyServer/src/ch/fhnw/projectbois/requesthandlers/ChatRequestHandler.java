package ch.fhnw.projectbois.requesthandlers;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.dto.MessageDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;

/**
 * 
 * @author Leeroy Koller
 *
 */

public class ChatRequestHandler extends RequestHandler {

	public ChatRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);
	}

	@Override
	protected void handleRequest() {
		if(request.getRequestId() == RequestId.CHAT_SEND_MSG) {
			this.sendMessage();
			//Dummy code
			//MessageDTO message = new MessageDTO();
			//client.getLobby().sendMessage(message);
		}
	}
	
	private void sendMessage() {
		String json = request.getJsonDataObject();
		MessageDTO messageDTO = JsonUtils.Deserialize(json, MessageDTO.class);
		//logic: make ready to post message
		
		// send response to client
		//lobbyDTO.setId(lobby.getId());
		//lobbyDTO.addPlayer(client.getUser().getUsername());
		json = JsonUtils.Serialize(messageDTO);
		Response response = new Response(ResponseId.RECEIVE_MSG, request.getRequestId(), json);

		client.sendResponse(response);		
		
		
	}

}
