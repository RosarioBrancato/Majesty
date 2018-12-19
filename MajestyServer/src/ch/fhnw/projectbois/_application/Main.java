package ch.fhnw.projectbois._application;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ch.fhnw.projectbois.access.DbAccess;
import ch.fhnw.projectbois.network.Server;

/**
 * The Class Main.
 *
 * @author Rosario Brancato
 */
public class Main {

	private Logger logger = null;

	/**
	 * Instantiates a new main.
	 */
	public Main() {
		this.logger = Logger.getLogger(this.getClass().getName());
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		System.out.println("Starting Majesty...");

		Locale.setDefault(new Locale("en"));

		Main main = new Main();
		main.run();
	}

	/**
	 * Runs the main routine of the server.
	 */
	public void run() {
		boolean success = false;
		Server server = null;

		try {
			Document docConfig = this.readConfig();
			if (docConfig != null) {
				// Server config
				Element eServer = (Element) docConfig.getElementsByTagName("Server").item(0);
				int serverPort = Integer.valueOf(eServer.getElementsByTagName("Port").item(0).getTextContent());

				// DB config
				Element eDb = (Element) docConfig.getElementsByTagName("Database").item(0);
				String dbHost = eDb.getElementsByTagName("Hostname").item(0).getTextContent();
				int dbPort = Integer.valueOf(eDb.getElementsByTagName("Port").item(0).getTextContent());
				String dbName = eDb.getElementsByTagName("DbName").item(0).getTextContent();
				String dbUsername = eDb.getElementsByTagName("Username").item(0).getTextContent();
				String dbPassword = eDb.getElementsByTagName("Password").item(0).getTextContent();
				String dbTimezone = eDb.getElementsByTagName("Timezone").item(0).getTextContent();

				// set up DB
				DbAccess.setUp(dbHost, dbPort, dbName, dbUsername, dbPassword, dbTimezone);
				success = DbAccess.testConnection();

				// start server
				if (success) {
					server = new Server();
					success = server.startServer(serverPort);
				}
			}

		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Main.run()", ex);
		}

		// main loop
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

					} else if (input.equals("test")) {
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

	/**
	 * Read config file of the application.
	 *
	 * @return Document of the config file
	 */
	private Document readConfig() {
		// read from config:
		// https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/

		Document document = null;

		try {
			String file;
			//https://stackoverflow.com/questions/1109019/determine-if-a-java-application-is-in-debug-mode-in-eclipse
			boolean isDebug = Boolean.getBoolean("debug");
			if(isDebug) {
				URL url = this.getClass().getResource("MajestyServer.config.xml");
				file = url.toURI().getPath();
			} else {
				//Get directory of jar file
				//https://stackoverflow.com/questions/15359702/get-location-of-jar-file/15359999
				file = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
				
				//Combine paths: https://stackoverflow.com/questions/412380/how-to-combine-paths-in-java
				file = Paths.get(file, "MajestyServer.config.xml").toString();
			}
			System.out.println("File: " + file);
			
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			document = dBuilder.parse(fXmlFile);
			document.getDocumentElement().normalize();

		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Main.readConfig()", ex);
			document = null;
		}

		return document;
	}

}
