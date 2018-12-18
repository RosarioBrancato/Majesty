package ch.fhnw.projectbois.queries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.fhnw.projectbois.access.DbAccess;
import ch.fhnw.projectbois.dto.LeaderboardDTO;
import ch.fhnw.projectbois.dto.LeaderboardPlayerDTO;
import ch.fhnw.projectbois.log.LoggerFactory;

/**
 * The Class LeaderboardQuery.
 * @author Dario Stoeckli
 */
public class LeaderboardQuery {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private LeaderboardDTO leaderboard = new LeaderboardDTO();
	private LeaderboardPlayerDTO currentuser = new LeaderboardPlayerDTO();
	private int rank = 1;

	/**
	 * Gets the leaderboard from the MySQL DB instance defined in the DbAccess class
	 *
	 * @return the leaderboard  as LeaderboardDTO object
	 */
	public LeaderboardDTO getLeaderboard() {

		try {
			Connection connection = DbAccess.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs;
			
	        // calls the predefined view in the MySQL DB and writes each tuple into a separate LeaderboardPlayerDBO
	        // which is added to a LeaderboardDTO object that holds an ArrayList of LeaderboardPlayerDTO objects
	        // limit the query to the first 100 players
			rs = statement.executeQuery("SELECT * FROM ***REMOVED***.leaderboard");
			while (rs.next()) {
				LeaderboardPlayerDTO player = new LeaderboardPlayerDTO();
				player.setRank(rank++);
				player.setUsername(rs.getString("Username"));
				player.setPoints(Integer.parseInt(rs.getString("Points")));

				leaderboard.addToLeaderboard(player);

			}
			connection.close();

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Got an exception while querying Database!", e);
		}

		return leaderboard;
	}
	
	/**
	 * Gets the player info from the MySQL DB instance
	 *
	 * @param username the username of the currently associated ServerClient user
	 * @return the player info
	 */
	public LeaderboardPlayerDTO getPlayerInfo(String username) {

		try {
			Connection connection = DbAccess.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs;

			rs = statement.executeQuery("SELECT nickname,points FROM user WHERE nickname='" + username + "'");
			while (rs.next()) {
				for (LeaderboardPlayerDTO player : leaderboard.getLeaderboard()) {
					if (player.getUsername().equals(username)) {
						currentuser.setRank(player.getRank());
					}
				}
				currentuser.setUsername(rs.getString("nickname"));
				currentuser.setPoints(Integer.parseInt(rs.getString("points")));
			}
			connection.close();

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Got an exception while querying Database!", e);
		}

		return currentuser;
	}
}
