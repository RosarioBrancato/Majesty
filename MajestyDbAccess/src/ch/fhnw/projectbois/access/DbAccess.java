package ch.fhnw.projectbois.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAccess {

	private final static String CONNECTION_STRING = "jdbc:mysql://pan.kreativmedia.ch:3306/fhnw_majesty?user=majAdmin&password=Vpiw405$&serverTimezone=UTC";
	
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
