/**
 * Various methods to manage user accounts (CRUD)
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ch.fhnw.projectbois.access.DbAccess;

public class UserHandler {
	static UserHandler uh = null;
	
	/**
	 * Gets the single instance of UserHandler.
	 *
	 * @return single instance of UserHandler
	 */
	public static UserHandler getInstance() {
		if(uh == null)
			uh = new UserHandler();
		return uh;
	}
	
	/**
	 * Instantiates a new user handler.
	 */
	private UserHandler() {}
	
	/**
	 * Check if the given user exists in the database
	 *
	 * @param username the username to check
	 * @return true, if if the user already exists
	 * @throws Exception in case something goes wrong in the database connection
	 */
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
	 * Creates the user in the database.
	 *
	 * @param username the username for the newely created user
	 * @param email the email for the newely created user
	 * @param password the password for the newely created user
	 * @return positive int with the generated user ID if the user has been created or -1 in case user was not created
	 * @throws Exception in case something goes wrong in the database connection
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
	
	/**
	 * Delete a user from the database.
	 *
	 * @param uid the unique db identifier of the user
	 * @throws Exception in case something goes wrong in the database connection
	 */
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
	
	/**
	 * Update email address of the user.
	 *
	 * @param uid the unique db identifier of the user
	 * @param email the new email
	 * @throws Exception in case something goes wrong in the database connection
	 */
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
	
	/**
	 * Update email AND password of the user in one command.
	 *
	 * @param uid the unique db identifier of the user
	 * @param email the new email
	 * @param password the new password
	 * @throws Exception in case something goes wrong in the database connection
	 */
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
	
	/**
	 * Update password of the user.
	 *
	 * @param uid the unique db identifier of the user
	 * @param password the new password
	 * @throws Exception in case something goes wrong in the database connection
	 */
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
	
	/**
	 * Update points of a user.
	 *
	 * @param uid the unique db identifier of the user
	 * @param diff the points to add (positive int) or to subtract (negative int)
	 * @throws Exception in case something goes wrong in the database connection
	 */
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
}