package ch.fhnw.projectbois.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAccess {

	private final static String CONNECTION_STRING = "jdbc:mysql://localhost/test?user=minty&password=greatsqldb";
	
	public static Connection GetConnection() {
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection(CONNECTION_STRING);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}
	
}
