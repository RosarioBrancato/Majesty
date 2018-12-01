package ch.fhnw.projectbois.requesthandlers;

import java.util.ArrayList;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.MessageDTO;
import ch.fhnw.projectbois.enumerations.ChatMember;
import ch.fhnw.projectbois.general.IdFactory;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Lobby;
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
			
		} else {
			
		}
	}
	
	private void sendMessage() {
		String json = request.getJsonDataObject();
		MessageDTO messageDTO = JsonUtils.Deserialize(json, MessageDTO.class);
		
		// send response to client
		int id = IdFactory.getInstance().getNewId(MessageDTO.class.getName());
		messageDTO.setId(id);
		
		//messageDTO.setAuthor(ChatMember.System); //for now just system. how to check who is the author??
		json = JsonUtils.Serialize(messageDTO);
		Response response = new Response(ResponseId.RECEIVE_MSG, request.getRequestId(), json);

		Lobby lobby = client.getLobby();
		ArrayList<ServerClient> clients = lobby.getClients();
		
		if (messageDTO.getReceiver() == ChatMember.All) {
			for(ServerClient c : clients) {
				c.sendResponse(response);
			}
		}
		
		// does not work yet
		//		||
		//	   \||/
		//		\/
		if (messageDTO.getAuthor() == ChatMember.Player1) {
			//send response to client player1
			//send notification to all others as "system"
		} else if (messageDTO.getAuthor() == ChatMember.Player2) {
			//send response to client player2
			//send notification to all others as "system"
		} else if (messageDTO.getAuthor() == ChatMember.Player3) {
			//send response to client player3
			//send notification to all others as "system"
		} else if (messageDTO.getAuthor() == ChatMember.Player4) {
			//send response to client player4
			//send notification to all others as "system"
		} 
		
	}

}
