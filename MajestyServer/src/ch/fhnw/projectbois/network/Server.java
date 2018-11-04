package ch.fhnw.projectbois.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import ch.fhnw.projectbois.lobby.LobbyDTO;

public class Server {

	private ServerSocket server = null;
	private boolean stop = false;
	
	private ArrayList<ServerClient> clients = new ArrayList<>();
	private ArrayList<Lobby> lobbies = new ArrayList<>();

	public boolean startServer(int port) {
		System.out.println("Starting server...");
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
							
						} catch (Exception ex) {
							System.out.println("Server ex: " + ex.getMessage());
						}
						
						printClientSize();
					}
				}
			};

			Thread t = new Thread(r, "ServerSocket");
			t.start();

		} catch (Exception ex) {
			System.out.println("Server ex on start: " + ex.getMessage());
			success = false;
		}
		
		return success;
	}

	public void stopServer() {
		System.out.println("Server.stopServer()");

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
	
	
	private void printClientSize() {
		System.out.println("Server - Clients connected: " + clients.size());
	}

}
