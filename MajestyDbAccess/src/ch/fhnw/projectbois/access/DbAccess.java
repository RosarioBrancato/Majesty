package ch.fhnw.projectbois.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import ch.fhnw.projectbois.preferences.UserPrefs;

public class DbAccess {
	private final static String db_server = UserPrefs.getInstance().get("DB_SERVER", "");
	private final static String db_port = UserPrefs.getInstance().get("DB_PORT", "");
	private final static String db_name = UserPrefs.getInstance().get("DB_NAME", "");
	private final static String db_user = UserPrefs.getInstance().get("DB_USER", "");
	private final static String db_pass = UserPrefs.getInstance().get("DB_PASS", "");
	private final static String db_param = UserPrefs.getInstance().get("DB_PARAM", "");
	private final static String CONNECTION_STRING = "jdbc:mysql://" + db_server + ":" + db_port + "/" + db_name + "?user=" + db_user + "&password=" + db_pass + "&" + db_param;
	
	public static Connection getConnection() {
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection(CONNECTION_STRING);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}
	
	public static Connection getConnectionWithExceptions() throws SQLException{
		Connection connection = null;
		connection = DriverManager.getConnection(CONNECTION_STRING);
		return connection;
	}
	
}
