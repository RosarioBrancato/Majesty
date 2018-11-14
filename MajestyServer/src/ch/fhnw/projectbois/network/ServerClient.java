package ch.fhnw.projectbois.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.log.LoggerFactory;

public class ServerClient {
	
	private Logger logger = null;

	private Socket socket = null;
	private String token = null;

	private Lobby lobby = null;

	public ServerClient(Server server, Socket socket) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		
		this.socket = socket;

		ServerClient instance = this;

		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String json;
					
					while ((json = reader.readLine()) != null && !socket.isClosed()) {
						
						try {
							System.out.println("ServerClient Json: " + json);

							Request request = JsonUtils.Deserialize(json, Request.class);
							System.out.println("Server Request: " + request.toString());

							if (request.getRequestId() == RequestId.CREATE_LOBBY) {
								server.createLobby(instance);
								
							} else if(request.getRequestId() == RequestId.JOIN_LOBBY) {
								LobbyDTO lobbyDTO = JsonUtils.Deserialize(request.getJsonDataObject(), LobbyDTO.class);
								server.joinLobby(instance, lobbyDTO);
								
							} else if(request.getRequestId() == RequestId.LEAVE_LOBBY) {
								lobby.removeClient(instance);
								
							} else if(request.getRequestId() == RequestId.GET_LOBBIES) {
								//TO-DO: Send lobbies arraylist to client

							} else if (request.getRequestId() == RequestId.DO_MOVE) {
								lobby.doMove(token, request.getJsonDataObject());
							}
							
						} catch (Exception e) {
							logger.log(Level.SEVERE, "ServerClient.Runnable()", e);
						}
						
					}
				} catch (Exception ex) {
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
	
	public void sendResponse(Response response) {
		try {
			String json = JsonUtils.Serialize(response);
			
			OutputStream stream = this.socket.getOutputStream();
			PrintWriter writer = new PrintWriter(stream);

			this.logger.info("S to C: " + json);

			writer.println(json);
			writer.flush();
			
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
