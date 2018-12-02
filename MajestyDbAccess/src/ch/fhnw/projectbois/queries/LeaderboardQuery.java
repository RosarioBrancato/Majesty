package ch.fhnw.projectbois.queries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import ch.fhnw.projectbois.access.DbAccess;
import ch.fhnw.projectbois.dto.LeaderboardDTO;
import ch.fhnw.projectbois.dto.LeaderboardPlayerDTO;

public class LeaderboardQuery {
	
	LeaderboardDTO leaderboard = new LeaderboardDTO();
    
	public LeaderboardDTO getLeaderboard() {
	
	try {
		Connection connection = DbAccess.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs;

        rs = statement.executeQuery("SELECT * FROM ***REMOVED***.leaderboard");
        while ( rs.next() ) {
        	LeaderboardPlayerDTO player = new LeaderboardPlayerDTO();
        	player.setRank(Integer.parseInt(rs.getString("Rank")));
        	player.setUsername(rs.getString("Username"));
        	player.setPoints(Integer.parseInt(rs.getString("Points")));
        	
        	leaderboard.addToLeaderboard(player);
        	
        	
        	String rank = rs.getString("Rank");
            String nickname = rs.getString("Username");
            String points = rs.getString("Points");
            System.out.println(rank);
            System.out.println(nickname);
            System.out.println(points);
        }
        connection.close();
    } catch (Exception e) {
        System.err.println("Got an exception! ");
        System.err.println(e.getMessage());
    }
	return leaderboard;
	}
}
