package ch.fhnw.projectbois._application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.fhnw.projectbois.log.LoggerFactory;
import ch.fhnw.projectbois.network.Server;

/**
 * The Class Main.
 *
 * @author Rosario Brancato
 */
public class Main {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		System.out.println("Starting Majesty...");
		
		Locale.setDefault(new Locale("en"));
		Logger logger = LoggerFactory.getLogger(Main.class);
		
		Server server = new Server();
		boolean success = server.startServer(8200);

		if (success) {
			System.out.println("Server started!");

			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				boolean shutdown = false;

				do {
					System.out.println("Enter SHUTDOWN to stop the server.");
					String input = br.readLine();

					if (input.equals("SHUTDOWN")) {
						server.stopServer();
						shutdown = true;
						
					} else if(input.equals("test")) {
						server.broadcastGameMsg();
						server.broadcastLobbyMsg();
						server.broadcastPlayScreenMsg();
						server.broadcastLoginMsg();
						server.broadcastLoginLeaderboard();
						server.broadcastLoginChat();
					}
				} while (!shutdown);

			} catch (IOException e) {
				logger.log(Level.SEVERE, "Main (Server)", e);
			}
			
		} else {
			logger.severe("Server could not be started.");
		}
		
		logger.info("Majesty (Server) ended.");
	}

}
