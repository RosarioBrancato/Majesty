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
import ch.fhnw.projectbois.dto.MessageDTO;
import ch.fhnw.projectbois.dto.ReportDTO;
import ch.fhnw.projectbois.enumerations.ChatMember;
import ch.fhnw.projectbois.enumerations.ReportSeverity;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Lobby;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;
import ch.fhnw.projectbois.translate.Translator;

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

		} else if (request.getRequestId() == RequestId.GET_LOBBY_OF_CLIENT) {
			this.getLobbyOfClient();
			
		} else if (request.getRequestId() == RequestId.EXTEND_LIFETIME_LOBBY) {
			this.setLifetime();
			
		} else if (request.getRequestId() == RequestId.DESTROY_LOBBY) {
			this.destroyLobby();
		}
	}

	private void createLobby() {
		String json = request.getJsonDataObject();
		LobbyDTO lobbyDTO = JsonUtils.Deserialize(json, LobbyDTO.class);

		// create lobby
		Lobby lobby = new Lobby();
		lobby.setCardSideA(lobbyDTO.isCardSideA());
		lobby.startCountdown(Lobby.LIFETIME_DEFAULT);

		// add player
		lobby.addClient(client);

		// add lobby to server
		server.getLobbies().add(lobby);

		// send response about lobby to client
		lobbyDTO = lobby.toLobbyDTO();
		json = JsonUtils.Serialize(lobbyDTO);
		Response response = new Response(ResponseId.LOBBY_CREATED, request.getRequestId(), json);

		client.sendResponse(response);
	}

	private void joinLobby() {
		boolean success = false;

		String json = request.getJsonDataObject();
		LobbyDTO lobbyDTO = JsonUtils.Deserialize(json, LobbyDTO.class);

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
			LobbyDTO responseLobbyDTO = lobby.toLobbyDTO();
			json = JsonUtils.Serialize(responseLobbyDTO);
			response = new Response(ResponseId.LOBBY_JOINED, request.getRequestId(), json);
			// notify users about join event
			MessageDTO message = new MessageDTO();
			message.setReceiver(ChatMember.All);
			message.setAuthor(ChatMember.System);
			message.setMessage(client.getUser().getUsername() + " has joined the game...");			
			String json1 = JsonUtils.Serialize(message);
			request = new Request(client.getUser().getToken(), RequestId.CHAT_SEND_MSG, json1);
			new ChatRequestHandler(request, server, client);
		} else {
			ReportDTO report = new ReportDTO();
			report.setSeverity(ReportSeverity.WARNING);
			report.setMessage("Lobby is already full.");
			json = JsonUtils.Serialize(report);
			response = new Response(ResponseId.LOBBY_ERROR, request.getRequestId(), json);
		}
		client.sendResponse(response);

		// Sending updates to clients that are a member of this lobby
		if (lobby != null && lobby.getClients().size() > 1 && success) {
			LobbyDTO responseLobbyDTO = lobby.toLobbyDTO();
			json = JsonUtils.Serialize(responseLobbyDTO);
			Response responseothers = new Response(ResponseId.LOBBY_JOINED_MULTICAST, request.getRequestId(), json);
			
			for (int i = 0; i < lobby.getClients().size() - 1; i++) {
				lobby.getClients().get(i).sendResponse(responseothers);
			}
		}
	}

	private void leaveLobby() {
		server.removeClientFromLobby(client);
	}

	private void sendLobbies() {
		LobbyListDTO lobbyList = new LobbyListDTO();

		// Get lobbies, which are not full
		ArrayList<Lobby> lobbies = (ArrayList<Lobby>) server.getLobbies().stream()
				.filter(f -> f.isNotFull() && !f.isGameStarted()).collect(Collectors.toList());
		
		// Convert lobbies to DTOs
		for (Lobby lobby : lobbies) {
			LobbyDTO lobbyDTO = lobby.toLobbyDTO();
			System.out.println(lobby.getLifetime());
			System.out.println(lobbyDTO.getLifetime());
			lobbyList.getLobbies().add(lobbyDTO);
		}

		// Send DTOs to client
		String json = JsonUtils.Serialize(lobbyList);
		Response response = new Response(ResponseId.UPDATE_LOBBIES, request.getRequestId(), json);
		client.sendResponse(response);
	}

	private void getLobbyOfClient() {
		Lobby lobby = client.getLobby();
		if (lobby != null) {
			LobbyDTO lobbyDTO = lobby.toLobbyDTO();

			String json = JsonUtils.Serialize(lobbyDTO);
			Response response = new Response(ResponseId.LOBBY_INFO, request.getRequestId(), json);
			client.sendResponse(response);
		}
	}
	
	private void setLifetime() {
		Lobby lobby = client.getLobby();
		lobby.setLifetime(Lobby.LIFETIME_DEFAULT);
		
		// Answer all clients
		LobbyDTO lobbyDTO = lobby.toLobbyDTO();
		String json = JsonUtils.Serialize(lobbyDTO);
		Response responseall = new Response(ResponseId.LOBBY_LIFETIME_EXTENDED, request.getRequestId(), json);

		for (ServerClient client : lobby.getClients()) {
			client.sendResponse(responseall);
		}
	}
	
	private void destroyLobby() {
		Lobby lobby = client.getLobby();
		lobby.destroy();
		server.getLobbies().remove(lobby);
		// Answer all clients	
		ReportDTO reportDTO = new ReportDTO(ReportSeverity.INFO, "Owner let lobby die", "msg_LobbyView_LobbyDied");
		String json = JsonUtils.Serialize(reportDTO);
		Response responseall = new Response(ResponseId.LOBBY_DIED, request.getRequestId(), json);

		for (ServerClient client : lobby.getClients()) {
			client.sendResponse(responseall);
		}
	}

}
