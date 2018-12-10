package ch.fhnw.projectbois.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ch.fhnw.projectbois.access.DbAccess;

public class UserHandler {
	static UserHandler uh = null;
	
	private UserHandler() {}
	
	public static UserHandler getInstance() {
		if(uh == null)
			uh = new UserHandler();
		return uh;
	}
	
	public boolean checkUserExists(String username) throws Exception{
		boolean userExists = false;
		Connection con = DbAccess.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT `uid` FROM `user` WHERE `nickname` = ?;");
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			userExists = true;
		}
		ps.close();
		return userExists;
	}
	
	/**
	 * 
	 * @param username
	 * @param email
	 * @param password
	 * @return positive int with the generated user ID if the user has been created or -1 in case user already exists
	 * @throws Exception
	 * 
	 */
	public int createUser(String username, String email, String password) throws Exception{
		if(!checkUserExists(username)) {
			PasswordHandler ph = PasswordHandler.getInstance();
			String salt = ph.getNextSalt();
			String hash = ph.getHashedPassword(salt, password);
			int uid = 0;		
			
			Connection con = DbAccess.getConnection();
			String query = "INSERT INTO `user` (`uid`, `nickname`, `email`, `password`, `salt`, `points`, `created_on`, `updated_on`) VALUES (NULL, ?, ?, ?, ?, 0, NOW(), NULL);";
			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, username);
			ps.setString(2, email);
			ps.setString(3, hash);
			ps.setString(4, salt);
			int response = ps.executeUpdate();
			if(response != 1) {
				throw new SQLException();
			}
			ResultSet uidList = ps.getGeneratedKeys();
			if(uidList.next()) {
				uid = uidList.getInt(1);
			}
			ps.close();
			return uid;
		}else {
			return -1;
		}
	}
	
	public void updateEmail(int uid, String email) throws Exception{
		Connection con = DbAccess.getConnection();
		PreparedStatement ps = con.prepareStatement("UPDATE `user` SET `email` = ? WHERE `user`.`uid` = ?;");
		ps.setString(1, email);
		ps.setInt(2, uid);
		int response = ps.executeUpdate();
		if(response != 1) {
			throw new SQLException();
		}
		ps.close();
	}
	
	public void updatePassword(int uid, String password) throws Exception{
		PasswordHandler ph = PasswordHandler.getInstance();
		String salt = ph.getNextSalt();
		String hash = ph.getHashedPassword(salt, password);
		
		Connection con = DbAccess.getConnection();
		PreparedStatement ps = con.prepareStatement("UPDATE `user` SET `password` = ?, `salt` = ? WHERE `user`.`uid` = ?;");
		ps.setString(1, hash);
		ps.setString(2, salt);
		ps.setInt(3, uid);
		int response = ps.executeUpdate();
		if(response != 1) {
			throw new SQLException();
		}
		ps.close();
	}
	
	public void updateEmailPassword(int uid, String email, String password) throws Exception{
		PasswordHandler ph = PasswordHandler.getInstance();
		String salt = ph.getNextSalt();
		String hash = ph.getHashedPassword(salt, password);
		
		Connection con = DbAccess.getConnection();
		PreparedStatement ps = con.prepareStatement("UPDATE `user` SET `email` = ?, `password` = ?, `salt` = ? WHERE `user`.`uid` = ?;");
		ps.setString(1, email);
		ps.setString(2, hash);
		ps.setString(3, salt);
		ps.setInt(4, uid);
		int response = ps.executeUpdate();
		if(response != 1) {
			throw new SQLException();
		}
		ps.close();
	}
	
	public void updatePoints(int uid, int diff) throws Exception{
		Connection con = DbAccess.getConnection();
		PreparedStatement ps = con.prepareStatement("UPDATE `user` SET `points` = `points`+? WHERE `user`.`uid` = ?;");
		ps.setInt(1, diff);
		ps.setInt(2, uid);
		int response = ps.executeUpdate();
		if(response != 1) {
			throw new SQLException();
		}
		ps.close();
	}
	
	public void deleteUser(int uid) throws Exception{
		Connection con = DbAccess.getConnection();
		PreparedStatement ps = con.prepareStatement("DELETE FROM `user` WHERE `user`.`uid` = ?;");
		ps.setInt(1, uid);
		int response = ps.executeUpdate();
		if(response != 1) {
			throw new SQLException();
		}
		ps.close();
	}
}