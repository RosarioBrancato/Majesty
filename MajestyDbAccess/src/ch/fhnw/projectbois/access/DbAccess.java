
package ch.fhnw.projectbois.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.fhnw.projectbois.log.LoggerFactory;

/**
 * The Class DbAccess.
 * 
 * @author Rosario Brancato, Alexandre Miccoli
 */
public class DbAccess {

	private static String CONNECTION_STRING = "";

	/**
	 * Sets up the connection string.
	 *
	 * @param server   servername
	 * @param port     port
	 * @param dbName   database name
	 * @param username username
	 * @param password password
	 * @param timezone timezone
	 */
	public static void setUp(String server, int port, String dbName, String username, String password,
			String timezone) {

		CONNECTION_STRING = "jdbc:mysql://" + server + ":" + port + "/" + dbName + "?user=" + username + "&password="
				+ password + "&serverTimezone=" + timezone;
	}

	/**
	 * Gets a new instance of the database connection.
	 *
	 * @return the connection
	 */
	public static Connection getConnection() {
		Connection connection = null;
		Logger logger = LoggerFactory.getLogger(DbAccess.class);

		try {
			connection = DriverManager.getConnection(CONNECTION_STRING);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "DBAccess.getConnection()", e);
		}

		return connection;
	}

	/**
	 * Tests connection.
	 *
	 * @return true, if successful
	 */
	public static boolean testConnection() {
		boolean success = false;

		Connection connection = getConnection();
		if (connection != null) {
			try {
				connection.close();
				success = true;
			} catch (SQLException e) {
			}
		}

		return success;
	}

}
