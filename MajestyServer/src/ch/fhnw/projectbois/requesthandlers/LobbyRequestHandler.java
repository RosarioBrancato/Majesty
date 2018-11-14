package ch.fhnw.projectbois.requesthandlers;

import java.util.ArrayList;
import java.util.stream.Collectors;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Lobby;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;

/**
 * 
 * @author Rosario Brancato
 *
 */
public class LobbyRequestHandler extends RequestHandler {

	public LobbyRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);
	}

	protected void handleRequest() {
		if (request.getRequestId() == RequestId.CREATE_LOBBY) {
			this.createLobby();

		} else if (request.getRequestId() == RequestId.JOIN_LOBBY) {
			this.joinLobby();

		} else if (request.getRequestId() == RequestId.LEAVE_LOBBY) {
			this.leaveLobby();

		} else if (request.getRequestId() == RequestId.GET_LOBBIES) {
			this.sendLobbies();
		}
	}

	private void createLobby() {
		String json = request.getJsonDataObject();
		LobbyDTO lobbyDTO = JsonUtils.Deserialize(json, LobbyDTO.class);

		// create lobby
		Lobby lobby = new Lobby();
		lobby.setCardSideA(lobbyDTO.isCardSideA());

		// add player
		lobby.addClient(client);

		// add lobby to server
		server.getLobbies().add(lobby);
		logger.info("Server.createLobby() - Lobby created!");

		// send response to client
		lobbyDTO.setId(lobby.getId());
		json = JsonUtils.Serialize(lobbyDTO);
		Response response = new Response(ResponseId.LOBBY_CREATED, request.getRequestId(), json);

		client.sendResponse(response);

		logger.info("Server.createLobby() - Response sent!");
	}

	private boolean joinLobby() {
		boolean success = false;

		LobbyDTO lobbyDTO = JsonUtils.Deserialize(request.getJsonDataObject(), LobbyDTO.class);

		// find lobby
		Lobby lobby = server.getLobbies().stream().filter(f -> f.getId() == lobbyDTO.getId()).findFirst().get();
		if (lobby != null) {
			// add player
			success = lobby.addClient(client);
		}

		// send response
		Response response;
		if (success) {
			response = new Response(ResponseId.LOBBY_JOINED, request.getRequestId(), request.getJsonDataObject());
		} else {
			response = new Response(ResponseId.LOBBY_ERROR, request.getRequestId(), request.getJsonDataObject());
		}
		client.sendResponse(response);

		return success;
	}

	private void leaveLobby() {
		// remove player from his lobby
		Lobby lobby = client.getLobby();
		lobby.removeClient(client);

		// if lobby is empty, remove it
		if (client.getLobby().isEmpty()) {
			server.getLobbies().remove(lobby);
		}
	}

	private void sendLobbies() {
		ArrayList<LobbyDTO> lobbyDTOs = new ArrayList<>();

		// get lobbies, which are not full
		ArrayList<Lobby> lobbies = (ArrayList<Lobby>) server.getLobbies().stream().filter(f -> f.isNotFull())
				.collect(Collectors.toList());

		// convert lobbies to DTOs
		for (Lobby lobby : lobbies) {
			LobbyDTO lobbyDTO = new LobbyDTO();
			lobbyDTO.setId(lobby.getId());

			for (ServerClient client : lobby.getClients()) {
				lobbyDTO.addPlayer(client.getUser().getUsername());
			}

			lobbyDTOs.add(lobbyDTO);
		}

		// send DTOs to client
		String json = JsonUtils.Serialize(lobbyDTOs);
		Response response = new Response(ResponseId.UPDATE_LOBBIES, request.getRequestId(), json);

		client.sendResponse(response);
	}

}
