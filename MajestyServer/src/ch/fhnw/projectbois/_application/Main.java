package ch.fhnw.projectbois._application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import ch.fhnw.projectbois.access.DbAccess;
import ch.fhnw.projectbois.log.LoggerFactory;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.preferences.UserPrefs;

/**
 * The Class Main.
 *
 * @author Rosario Brancato, Alexandre Miccoli
 */
public class Main {
	
	
	/**
	 * Asks for DB parameters and writes them into preferences for later use.
	 * 
	 * @param param the preferences key string
	 */
	@SuppressWarnings("resource")
	private static void dbSetParam(String param) {
		String res = null;
		
		Preferences up = UserPrefs.getInstance();
		Scanner in = new Scanner(System.in);
		
		String param_name = null;
		String param_regex = null;
		String param_default = up.get(param, "");
		
		switch(param){
			case "DB_SERVER":
				param_name = "server address";
				param_regex = "^[a-zA-Z0-9.]+$";
				break;
			case "DB_PORT":
				param_name = "server port";
				param_regex = "^[0-9]+$";
				break;
			case "DB_NAME":
				param_name = "name";
				param_regex = null;
				break;
			case "DB_USER":
				param_name = "username";
				param_regex = null;
				break;
			case "DB_PASS":
				param_name = "password";
				param_regex = null;
				break;
			case "DB_PARAM":
				param_name = "additional parameters";
				param_regex = null;
				break;
		}
		
		do {
			System.out.println("Please enter the DB " + param_name + " or press enter to reuse the latest setting [" + param_default + "]:");
			String tmp = in.nextLine();
			if(tmp.equals(""))
				tmp = param_default;
			if(param_regex == null || tmp.matches(param_regex))
				res = tmp;
		}while(res == null);
		
		System.out.println("Using \"" + res + "\" as DB " + param_name);
		
		if(!res.equals(param_default))
			up.put(param, res);
	}
	
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
		
		if(success) {
			ArrayList<String> dbParam = new ArrayList<>();
			dbParam.add("DB_SERVER");
			dbParam.add("DB_PORT");
			dbParam.add("DB_NAME");
			dbParam.add("DB_USER");
			dbParam.add("DB_PASS");
			dbParam.add("DB_PARAM");
			int length = dbParam.size();
			
			boolean resetDbParam = false;
			
			if(UserPrefs.getInstance().getBoolean("DBParamWorking", false)) {
				@SuppressWarnings("resource")
				Scanner in = new Scanner(System.in);
				boolean ans = false;
				System.out.println("You already set working DB parameters. Would you like to change them? [yN]");
				while(!ans) {
					String tmp = in.nextLine();
					if(tmp.equals("") || tmp.equals("n") || tmp.equals("N")) {
						ans = true;
					}else if(tmp.equals("y") || tmp.equals("Y")) {
						resetDbParam = true;
						ans = true;
					}
				}
			}else {
				resetDbParam = true;
			}
			
			if(resetDbParam) {
				System.out.println("You will now be prompted to type in the database parameters. \nTo reuse previous parameters (in brackets), simply leave the line empty and press ENTER.");
				for(int i=0; i<length; i++) {
					dbSetParam(dbParam.get(i));
				}
			}			
			
			try {
				Connection conn = DbAccess.getConnectionWithExceptions();
				conn.close();
				UserPrefs.getInstance().putBoolean("DBParamWorking", true);
			}catch(SQLException e) {
				UserPrefs.getInstance().putBoolean("DBParamWorking", false);
				success = false;
				logger.severe("Connection to the database failed: " + e.getMessage());
			}
		}
		
		

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
