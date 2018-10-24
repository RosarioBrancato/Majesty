package ch.fhnw.projectbois.network;

import java.util.ArrayList;

public class Lobby {

	private ArrayList<ServerClient> clients = new ArrayList<>();

	public Lobby() {

	}

	public void addClient(ServerClient client) {
		if (this.isNotEmpty()) {
			client.setLobby(this);
			this.clients.add(client);
		}
	}

	public boolean isNotEmpty() {
		return this.clients.size() < 4;
	}

	public void doMove(String clientToken, String json) {
		System.out.println("Lobby.doMove() - Token: " + clientToken + " JSON: " + json);
	}

	public void updateGameState(String json) {
		for (ServerClient client : this.clients) {
			client.sendGameState(json);
		}
	}

}
