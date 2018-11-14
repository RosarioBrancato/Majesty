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
import ch.fhnw.projectbois.dto.UserDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.log.LoggerFactory;
import ch.fhnw.projectbois.requesthandlers.AuthRequestHandler;
import ch.fhnw.projectbois.requesthandlers.ChatRequestHandler;
import ch.fhnw.projectbois.requesthandlers.GameRequestHandler;
import ch.fhnw.projectbois.requesthandlers.LeaderboardRequestHandler;
import ch.fhnw.projectbois.requesthandlers.LobbyRequestHandler;

/**
 * 
 * @author Rosario Brancato
 *
 */
public class ServerClient {

	private Logger logger = null;

	private Socket socket = null;
	private UserDTO user = null;

	private Lobby lobby = null;

	public ServerClient(Server server, Socket socket) {
		this.logger = LoggerFactory.getLogger(this.getClass());

		this.socket = socket;

		ServerClient client = this;

		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String json;

					while ((json = reader.readLine()) != null && !socket.isClosed()) {

						try {
							logger.info("ServerClient Json: " + json);

							Request request = JsonUtils.Deserialize(json, Request.class);
							logger.info("Server Request: " + request.toString());

							if (request.getRequestId() > RequestId.AUTH_RANGE_START
									&& request.getRequestId() < RequestId.AUTH_RANGE_END) {

								new AuthRequestHandler(request, server, client);

							} else if (user != null) {
								// if user is logged in...
								if (request.getRequestId() > RequestId.LOBBY_RANGE_START
										&& request.getRequestId() < RequestId.LOBBY_RANGE_END) {

									new LobbyRequestHandler(request, server, client);

								} else if (request.getRequestId() > RequestId.GAME_RANGE_START
										&& request.getRequestId() < RequestId.GAME_RANGE_END) {

									new GameRequestHandler(request, server, client);

								} else if (request.getRequestId() > RequestId.LEADERBOARD_RANGE_START
										&& request.getRequestId() < RequestId.LEADERBOARD_RANGE_END) {

									new LeaderboardRequestHandler(request, server, client);

								} else if (request.getRequestId() > RequestId.CHAT_RANGE_START
										&& request.getRequestId() < RequestId.CHAT_RANGE_END) {

									new ChatRequestHandler(request, server, client);
								}
							}

						} catch (Exception e) {
							logger.log(Level.SEVERE, "ServerClient.Runnable()", e);
						}

					}
				} catch (Exception ex) {
				}

				server.removeClient(client);
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

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public UserDTO getUser() {
		return this.user;
	}

}
