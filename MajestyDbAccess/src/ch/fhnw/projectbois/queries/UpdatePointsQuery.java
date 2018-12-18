package ch.fhnw.projectbois.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ch.fhnw.projectbois.access.DbAccess;
import ch.fhnw.projectbois.gameobjects.Player;
import ch.fhnw.projectbois.log.LoggerFactory;

/**
 * The Class UpdatePointsQuery.
 * @author Dario Stoeckli
 */
public class UpdatePointsQuery {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Writes the points calculated in the final calculations of the game to the MySQL DB
	 *
	 * @param players the players from the finished game associated to a specific lobby
	 * @return true, if successful
	 */
	public boolean setPoints(ArrayList<Player> players) {
		boolean success = false;
		
		// create prepared statement to write points to DB
		try {
			Connection connection = DbAccess.getConnection();
			PreparedStatement preparedstatement = connection
					.prepareStatement("UPDATE user SET points=points+? WHERE nickname=?");
			
			int rs = 0;

			for (Player player : players) {
				preparedstatement.setInt(1, player.getFinalCalculation().getTotalCount());
				preparedstatement.setString(2, player.getUsername());
				rs += preparedstatement.executeUpdate();
				logger.info("Wrote score to db: " + player.getUsername() + " made " + player.getPoints()
						+ " - rows affected: " + rs);
			}
			
			if (rs >= 4) {
				success = true;
			}
			
			connection.close();

		} catch (Exception e) {
			this.logger.log(Level.SEVERE, "UpdatePointsQuery.setPoints()", e);
		}

		return success;
	}

}
