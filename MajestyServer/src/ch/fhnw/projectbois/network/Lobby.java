package ch.fhnw.projectbois.network;

import java.util.ArrayList;

public class Lobby {

	private static int NEXT_ID = 1;

	private int id = -1;
	private ArrayList<ServerClient> clients = new ArrayList<>();

	public Lobby() {
		this.id = getNewId();
	}

	public boolean addClient(ServerClient client) {
		boolean success = false;

		if (this.isNotFull()) {
			client.setLobby(this);
			this.clients.add(client);
			success = true;
		}

		return success;
	}

	public boolean removeClient(ServerClient client) {
		return this.clients.removeIf(f -> f.getToken().equals(client.getToken()));
	}

	public boolean isNotFull() {
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

	private static int getNewId() {
		return NEXT_ID++;
	}

	public int getId() {
		return this.id;
	}

}
