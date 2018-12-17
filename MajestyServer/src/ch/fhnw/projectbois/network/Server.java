package ch.fhnw.projectbois.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.ReportDTO;
import ch.fhnw.projectbois.enumerations.ReportSeverity;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.log.LoggerFactory;

/**
 * The Class Server.
 *
 * @author Rosario Brancato
 */
public class Server {

	private Logger logger = null;

	private ServerSocket server = null;
	private boolean stop = false;

	private ArrayList<ServerClient> clients = new ArrayList<>();
	private ArrayList<Lobby> lobbies = new ArrayList<>();

	/**
	 * Instantiates a new server.
	 */
	public Server() {
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

	/**
	 * Start server.
	 *
	 * @param port the port
	 * @return true, if successful
	 */
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

							printInfos();

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

	/**
	 * Stop server.
	 */
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

	/**
	 * Removes the client.
	 *
	 * @param client the client
	 */
	public synchronized void removeClient(ServerClient client) {
		// remove client
		this.removeClientFromLobby(client);

		// remove client
		this.clients.remove(client);
		this.printInfos();
	}

	/**
	 * Removes the client from lobby.
	 *
	 * @param client the client
	 */
	public synchronized void removeClientFromLobby(ServerClient client) {
		// remove client from his lobby
		Lobby lobby = client.getLobby();
		if (lobby != null) {
			lobby.removeClient(client);
			client.setLobby(null);

			// if lobby is empty, remove it
			if (lobby.isEmpty()) {
				lobby.destroy();
				lobbies.remove(lobby);
			}
		}
	}

	// TEST METHODS

	/**
	 * Broadcast game msg.
	 */
	public void broadcastGameMsg() {
		// info
		ReportDTO report = new ReportDTO(ReportSeverity.INFO, "inf_Broadcast_Game_Msg");

		String json = JsonUtils.Serialize(report);
		Response response = new Response(ResponseId.GAME_ERROR, RequestId.TEST, json);

		for (ServerClient c : this.clients) {
			c.sendResponse(response);
		}
	}

	/**
	 * Broadcast lobby msg.
	 */
	public void broadcastLobbyMsg() {
		// info
		ReportDTO report = new ReportDTO(ReportSeverity.INFO, "inf_Broadcast_Lobby_Msg");

		String json = JsonUtils.Serialize(report);
		Response response = new Response(ResponseId.LOBBY_ERROR, RequestId.TEST, json);

		for (ServerClient c : this.clients) {
			c.sendResponse(response);
		}
	}

	/**
	 * Broadcast play screen msg.
	 */
	public void broadcastPlayScreenMsg() {
		// info
		ReportDTO report = new ReportDTO(ReportSeverity.INFO, "inf_Broadcast_Play_Screen_Msg");

		String json = JsonUtils.Serialize(report);
		Response response = new Response(ResponseId.PLAY_SCREEN_ERROR, RequestId.TEST, json);

		for (ServerClient c : this.clients) {
			c.sendResponse(response);
		}
	}

	/**
	 * Broadcast login msg.
	 */
	public void broadcastLoginMsg() {
		// info
		ReportDTO report = new ReportDTO(ReportSeverity.INFO, "inf_Broadcast_Login_Msg");

		String json = JsonUtils.Serialize(report);
		Response response = new Response(ResponseId.AUTH_ERROR_SERVER, RequestId.TEST, json);

		for (ServerClient c : this.clients) {
			c.sendResponse(response);
		}
	}

	/**
	 * Broadcast profile.
	 */
	public void broadcastProfile() {
		// info
		ReportDTO report = new ReportDTO(ReportSeverity.INFO, "inf_Broadcast_Profile_Msg");

		String json = JsonUtils.Serialize(report);
		Response response = new Response(ResponseId.PROFILE_ERROR, RequestId.TEST, json);

		for (ServerClient c : this.clients) {
			c.sendResponse(response);
		}
	}

	/**
	 * Broadcast login leaderboard.
	 */
	public void broadcastLoginLeaderboard() {
		// info
		ReportDTO report = new ReportDTO(ReportSeverity.INFO, "inf_Broadcast_Leaderboard_Msg");

		String json = JsonUtils.Serialize(report);
		Response response = new Response(ResponseId.LEADERBOARD_ERROR, RequestId.TEST, json);

		for (ServerClient c : this.clients) {
			c.sendResponse(response);
		}
	}

	/**
	 * Broadcast login chat.
	 */
	public void broadcastLoginChat() {
		// info
		ReportDTO report = new ReportDTO(ReportSeverity.INFO, "inf_Broadcast_Chat_Msg");

		String json = JsonUtils.Serialize(report);
		Response response = new Response(ResponseId.CHAT_ERROR, RequestId.TEST, json);

		for (ServerClient c : this.clients) {
			c.sendResponse(response);
		}
	}

	/**
	 * Prints the infos.
	 */
	private void printInfos() {
		logger.info("Server - Clients: " + clients.size() + " Lobbies: " + this.lobbies.size());
	}

	// GETTERS AND SETTERS

	public int getClientsCount() {
		return this.clients.size();
	}

	public ArrayList<ServerClient> getClients() {
		return this.clients;
	}

	public ArrayList<Lobby> getLobbies() {
		return this.lobbies;
	}
}
