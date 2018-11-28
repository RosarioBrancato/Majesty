package ch.fhnw.projectbois.requesthandlers;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.dto.LobbyListDTO;
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

		// send response to client
		lobbyDTO.setId(lobby.getId());
		lobbyDTO.addPlayer(client.getUser().getUsername());
		json = JsonUtils.Serialize(lobbyDTO);
		Response response = new Response(ResponseId.LOBBY_CREATED, request.getRequestId(), json);

		client.sendResponse(response);
	}

	private boolean joinLobby() {
		boolean success = false;

		LobbyDTO lobbyDTO = JsonUtils.Deserialize(request.getJsonDataObject(), LobbyDTO.class);

		// find lobby
		Lobby lobby = null;
		Optional<Lobby> first = server.getLobbies().stream().filter(f -> f.getId() == lobbyDTO.getId()).findFirst();
		if (first.isPresent()) {
			lobby = first.get();

			if (lobby != null) {
				// add player
				success = lobby.addClient(client);
			}
		}

		// send response
		Response response;
		if (success) {
			lobbyDTO.addPlayer(client.getUser().getUsername());
			String json = JsonUtils.Serialize(lobbyDTO);
			response = new Response(ResponseId.LOBBY_JOINED, request.getRequestId(), json);

		} else {
			response = new Response(ResponseId.LOBBY_ERROR, request.getRequestId(), request.getJsonDataObject());
		}

		client.sendResponse(response);
		
		//Sending updates to clients that are a member of this lobby
		
		Response responseothers;
		if (success) {
			String json = JsonUtils.Serialize(lobbyDTO);
			responseothers = new Response(ResponseId.LOBBY_JOINED, request.getRequestId(), json);

		} else {
			responseothers = new Response(ResponseId.LOBBY_ERROR, request.getRequestId(), request.getJsonDataObject());
		}
		
		for (ServerClient client : lobby.getClients()) {
			client.sendResponse(responseothers);
		}
		
		
		//To Do Send Message to chat - Dario
		/*
		ArrayList<String> players = lobbyDTO.getPlayers();
		String player = players.get(players.size()-1);
		{
			System.out.println(player + "joined!");
		}
		 */
		
		return success;
	}

	private void leaveLobby() {
		//Remove player from his lobby
		Lobby lobby = client.getLobby();
		lobby.removeClient(client);
		
		LobbyDTO lobbyDTO = JsonUtils.Deserialize(request.getJsonDataObject(), LobbyDTO.class);
		
		//Sending updates to clients that are a member of this lobby
		
		Response responseothers; 
		lobbyDTO.removePlayer(client.getUser().getUsername());
		String json = JsonUtils.Serialize(lobbyDTO);
		responseothers = new Response(ResponseId.LOBBY_LEFT, request.getRequestId(), json);
		
		for (ServerClient client : lobby.getClients()) {
			client.sendResponse(responseothers);
		}

		//If lobby is empty, remove it
		if (client.getLobby().isEmpty()) {
			server.getLobbies().remove(lobby);
		}
	}

	private void sendLobbies() {
		LobbyListDTO lobbyList = new LobbyListDTO();

		//Get lobbies, which are not full
		ArrayList<Lobby> lobbies = (ArrayList<Lobby>) server.getLobbies().stream().filter(f -> f.isNotFull() && !f.isGameStarted())
				.collect(Collectors.toList());

		//Convert lobbies to DTOs
		for (Lobby lobby : lobbies) {
			LobbyDTO lobbyDTO = new LobbyDTO();
			lobbyDTO.setId(lobby.getId());
			lobbyDTO.setCardSideA(lobby.isCardSideA());

			for (ServerClient client : lobby.getClients()) {
				lobbyDTO.addPlayer(client.getUser().getUsername());
			}

			lobbyList.getLobbies().add(lobbyDTO);
		}

		//Send DTOs to client
		String json = JsonUtils.Serialize(lobbyList);
		Response response = new Response(ResponseId.UPDATE_LOBBIES, request.getRequestId(), json);

		client.sendResponse(response);
	}

}
