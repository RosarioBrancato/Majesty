package ch.fhnw.projectbois.queries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import ch.fhnw.projectbois.access.DbAccess;
import ch.fhnw.projectbois.dto.UserDTO;

public class UpdatePointsQuery {
	
	public boolean setPoints(UserDTO user) {
		 boolean success = false;
		 try {
			 Connection connection = DbAccess.getConnection();
			 Statement statement = connection.createStatement();
			 ResultSet rs;
      

			// rs = statement.executeQuery("UPDATE user SET points = points+10 WHERE nickname="dario"");
			 //while ( rs.next() ) {
				 
			 //}
			 
			 success=true;
        connection.close();
		 } catch (Exception e) {
			 System.err.println("Got an exception while querying Database!");
			 System.err.println(e.getMessage());
		 }
	return success;
	}

}
