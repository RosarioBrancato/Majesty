package ch.fhnw.projectbois.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.log.LoggerFactory;

public class Server {

	private Logger logger = null;
	
	private ServerSocket server = null;
	private boolean stop = false;
	
	private ArrayList<ServerClient> clients = new ArrayList<>();
	private ArrayList<Lobby> lobbies = new ArrayList<>();

	public Server() {
		this.logger = LoggerFactory.getLogger(this.getClass());
	}
	
	public boolean startServer(int port) {
		this.logger.info("Starting server...");
		boolean success = true;

		try {
			Server instance = this;

			this.server = new ServerSocket(port);

			Runnable r = new Runnable() {
				@Override
				public void run() {
					while (!stop) {
						try {
							Socket socket = server.accept();
							clients.add(new ServerClient(instance, socket));
							
							printClientSize();
							
						} catch (Exception ex) {
						}
					}
				}
			};

			Thread t = new Thread(r, "ServerSocket");
			t.start();

		} catch (Exception ex) {
			this.logger.log(Level.SEVERE, "Server.startServer()", ex);
			success = false;
		}
		
		return success;
	}

	public void stopServer() {
		this.logger.info("Server.stopServer()");

		for (ServerClient s : this.clients) {
			try {
				s.close();
			} catch (Exception ex) {
			}
		}

		this.stop = true;
		if (this.server != null) {
			try {
				this.server.close();
			} catch (Exception ex) {
			}
		}
	}

	public void remove(ServerClient client) {
		this.clients.remove(client);
		this.printClientSize();
	}

	public int getClientsCount() {
		return this.clients.size();
	}
	
	
	public void createLobby(ServerClient client) {
		Lobby lobby = new Lobby();
		lobby.addClient(client);
		
		this.lobbies.add(lobby);
		this.logger.info("Server.createLobby() - Lobby created!");
		
		LobbyDTO lobbyDTO = new LobbyDTO();
		lobbyDTO.setId(lobby.getId());
		
		String json = JsonUtils.Serialize(lobbyDTO);
		Response response = new Response(ResponseId.PLAYERS_LOBBY, RequestId.CREATE_LOBBY, json);
		
		client.sendResponse(response);
		this.logger.info("Server.createLobby() - Response sent!");
	}
	
	public boolean joinLobby(ServerClient client, LobbyDTO lobbyDTO) {
		boolean success = false;
		
		Lobby lobby = this.lobbies.stream().filter(f -> f.getId() == lobbyDTO.getId()).findFirst().get();
		if(lobby != null) {
			success = lobby.addClient(client);
		}
		
		return success;
	}
	
	public ArrayList<LobbyDTO> getLobbies() {
		ArrayList<LobbyDTO> lobbies = new ArrayList<>();
		
		for(Lobby l : this.lobbies) {
			LobbyDTO lobbyDTO =  new LobbyDTO();
			lobbyDTO.setId(l.getId());
			
			//lobbyDTO.setPlayers(); TO-DO
			
			lobbies.add(lobbyDTO);
		}
		
		return lobbies;
	}
	
	
	public void broadcastTest() {
		GameState gameState = new GameState();
		String json = JsonUtils.Serialize(gameState);
		Response r = new Response(ResponseId.UPDATE_GAMESTATE, RequestId.EMPTY, json);
		
		for(ServerClient c : this.clients) {
			c.sendResponse(r);
		}
	}
	
	
	private void printClientSize() {
		logger.info("Server - Clients connected: " + clients.size());
	}

}
