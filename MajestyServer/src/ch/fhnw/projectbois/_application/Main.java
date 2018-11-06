package ch.fhnw.projectbois._application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ch.fhnw.projectbois.network.Server;

public class Main {

	public static void main(String[] args) {
		System.out.println("Starting Majesty...");
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
						server.broadcastTest();
					}
				} while (!shutdown);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
