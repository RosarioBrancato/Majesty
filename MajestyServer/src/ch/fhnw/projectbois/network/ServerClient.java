package ch.fhnw.projectbois.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.json.JsonUtils;

public class ServerClient {
	
	private Socket socket = null;
	private String token = null;

	private Lobby lobby = null;

	public ServerClient(Server server, Socket socket) {
		this.socket = socket;

		ServerClient instance = this;

		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String json;
					while ((json = reader.readLine()) != null && !socket.isClosed()) {

						System.out.println("ServerClient Json: " + json);

						Request request = JsonUtils.Deserialize(json, Request.class);

						if(request.getRequestId() == RequestId.CREATE_LOBBY) {
							server.createLobby(instance);
							
						} else if (request.getRequestId() == RequestId.DO_MOVE) {
							lobby.doMove(token, request.getJsonDataObject());
						}
					}
				} catch (Exception ex) {
					// System.out.println("Error ClientMain: " + ex.getMessage());
				}

				server.remove(instance);
			}
		};

		Thread t = new Thread(r);
		t.start();
	}

	public void close() {
		try {
			this.socket.close();
		} catch (IOException e) {
		}
	}

	public void sendGameState(String json) {
		try {
			try {
				OutputStream stream = this.socket.getOutputStream();
				PrintWriter writer = new PrintWriter(stream);

				System.out.println("ServerClient writes: " + json);

				writer.println(json);
				writer.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {

		}
	}

	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
	}

	public Lobby getLobby() {
		return this.lobby;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return this.token;
	}

}
