/*
 * 
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.dto;

/**
 * 
 * @author Alexandre Miccoli
 * 
 */

public class LoginDTO {
	
	private String username;
	private String password;
	
	/**
	 * Instantiates a new login DTO.
	 */
	public LoginDTO() {
		
	}
	
	/**
	 * Instantiates a new login DTO.
	 *
	 * @param user the user
	 * @param pass the pass
	 */
	public LoginDTO(String user, String pass) {
		this.username = user;
		this.password = pass;
	}
	
	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
}
