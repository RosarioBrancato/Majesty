package ch.fhnw.projectbois.requesthandlers;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Lobby;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;

public class LobbyRequestHandler extends RequestHandler {

	private Lobby lobby;
	
	public LobbyRequestHandler(Request request, Server server, ServerClient client, Lobby lobby) {
		super(request, server, client);
		
		this.lobby = lobby;

		this.handleRequest();
	}

	protected void handleRequest() {
		if (request.getRequestId() == RequestId.CREATE_LOBBY) {
			server.createLobby(client);
			
		} else if(request.getRequestId() == RequestId.JOIN_LOBBY) {
			LobbyDTO lobbyDTO = JsonUtils.Deserialize(request.getJsonDataObject(), LobbyDTO.class);
			server.joinLobby(client, lobbyDTO);
			
		} else if(request.getRequestId() == RequestId.LEAVE_LOBBY) {
			lobby.removeClient(client);
			
		} else if(request.getRequestId() == RequestId.GET_LOBBIES) {
			//TO-DO: Send lobbies arraylist to client
		}
	}

}
