package ch.fhnw.projectbois.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
							System.out.println("Error Runnable: " + ex.getMessage());
						}
					}
				}
			};

			Thread t = new Thread(r, "ServerSocket");
			t.start();

		} catch (Exception ex) {
			System.out.println("Error on start: " + ex.getMessage());
			success = false;
		}
		
		return success;
	}

	public void stopServer() {
		System.out.println("Stop server");

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
	}

	public int getClientsCount() {
		return this.clients.size();
	}
	
	
	public void createLobby(ServerClient client) {
		Lobby lobby = new Lobby();
		lobby.addClient(client);
		
		this.lobbies.add(lobby);
	}

}
