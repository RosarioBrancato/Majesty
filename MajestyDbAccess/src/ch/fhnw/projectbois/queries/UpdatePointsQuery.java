package ch.fhnw.projectbois.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.logging.Logger;
import ch.fhnw.projectbois.access.DbAccess;
import ch.fhnw.projectbois.gameobjects.Player;
import ch.fhnw.projectbois.log.LoggerFactory;

public class UpdatePointsQuery {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public boolean setPoints(ArrayList<Player> players) {
		 boolean success = false;
		 try {
			 Connection connection = DbAccess.getConnection();
			 PreparedStatement preparedstatement = connection.prepareStatement("UPDATE user SET points=points+? WHERE nickname=?");
			 int rs;
			 
			 for (Player player : players) {
				 preparedstatement.setInt(1, player.getPoints());
				 preparedstatement.setString(2, player.getUsername());
				 rs = preparedstatement.executeUpdate();
				logger.info("Wrote score to db: " + player.getUsername() + " made " + player.getPoints() + " - rows affected: " + rs); 
			 }
			 success=true;
        connection.close();
		 } catch (Exception e) {
			 System.err.println("Got an exception while writing points to Database!");
			 System.err.println(e.getMessage());
		 }
	return success;
	}

}
