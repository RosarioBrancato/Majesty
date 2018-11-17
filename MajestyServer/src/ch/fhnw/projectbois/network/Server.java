package ch.fhnw.projectbois.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.gameobjects.*;
import ch.fhnw.projectbois.general.IdFactory;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.log.LoggerFactory;

/**
 * 
 * @author Rosario Brancato
 *
 */
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

	public void removeClient(ServerClient client) {
		this.clients.remove(client);
		this.printClientSize();
	}

	public int getClientsCount() {
		return this.clients.size();
	}

	public ArrayList<Lobby> getLobbies() {
		return this.lobbies;
	}

	// TEST METHODS

	public void broadcastTest() {
		GameState gameState = new GameState();
		gameState.setId(IdFactory.getInstance().getNewId(GameState.class.getName()));
		
		DisplayCard card = new DisplayCard();
		card.setCardType(CardType.Miller);
		gameState.getBoard().getDisplay().add(card);
		
		card = new DisplayCard();
		card.setCardType(CardType.Miller);
		gameState.getBoard().getDisplay().add(card);
		
		card = new DisplayCard();
		card.setCardType(CardType.Brewer);
		gameState.getBoard().getDisplay().add(card);
		
		card = new DisplayCard();
		card.setCardType(CardType.Knight);
		gameState.getBoard().getDisplay().add(card);
		
		card = new DisplayCard();
		card.setCardType(CardType.Noble);
		gameState.getBoard().getDisplay().add(card);
		
		card = new DisplayCard();
		card.setCardType(CardType.Witch);
		gameState.getBoard().getDisplay().add(card);
		
		
		String json = JsonUtils.Serialize(gameState);
		Response response = new Response(ResponseId.UPDATE_GAMESTATE, RequestId.EMPTY, json);

		for (ServerClient c : this.clients) {
			c.sendResponse(response);
		}
	}

	private void printClientSize() {
		logger.info("Server - Clients connected: " + clients.size());
	}

}
