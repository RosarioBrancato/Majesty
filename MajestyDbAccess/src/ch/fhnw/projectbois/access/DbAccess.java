package ch.fhnw.projectbois.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAccess {

	private final static String CONNECTION_STRING = "jdbc:mysql://***REMOVED***:3306/***REMOVED***?user=***REMOVED***&password=***REMOVED***&serverTimezone=UTC";
	
	public static Connection getConnection() {
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection(CONNECTION_STRING);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}
	
}
