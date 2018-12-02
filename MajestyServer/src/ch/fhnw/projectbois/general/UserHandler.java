package ch.fhnw.projectbois.general;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ch.fhnw.projectbois.access.DbAccess;
import ch.fhnw.projectbois.auth.PasswordHandler;

public class UserHandler {
	
	public UserHandler() {
		
	}
	
	public void createUser(String username, String email, String password) throws Exception{
		PasswordHandler ph = new PasswordHandler();
		String salt = ph.getNextSalt();
		String hash = ph.getHashedPassword(salt, password);
		
		Connection con = DbAccess.getConnection();
		PreparedStatement ps = con.prepareStatement("INSERT INTO `user` (`uid`, `nickname`, `email`, `password`, `salt`, `points`, `created_on`, `updated_on`) VALUES (NULL, ?, ?, ?, ?, 0, NOW(), NULL);");
		ps.setString(1, username);
		ps.setString(2, email);
		ps.setString(3, hash);
		ps.setString(4, salt);
		int response = ps.executeUpdate();
		if(response != 1) {
			throw new SQLException();
		}
		ps.close();
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
		PasswordHandler ph = new PasswordHandler();
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