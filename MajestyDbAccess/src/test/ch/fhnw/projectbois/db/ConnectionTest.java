package test.ch.fhnw.projectbois.db;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import ch.fhnw.projectbois.access.DbAccess;

class ConnectionTest {

	@Test
	void testConnection() {
		Connection conn = DbAccess.getConnection();
		assertNotNull(conn);
	}

	@Test
	void testSelect() {
		Connection conn = DbAccess.getConnection();
		
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user");
			ResultSet results = stmt.executeQuery();
			
//			String username = null;
//			if(results.next()) {
//				username = results.getString("nickname");
//			}
			
			assertNotNull(results);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
