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

public class RegistrationDTO {
	private String username = null;
	private String email = null;
	private String password = null;
	
	/**
	 * Instantiates a new registration DTO.
	 */
	public RegistrationDTO() {
		
	}
	
	/**
	 * Instantiates a new registration DTO.
	 *
	 * @param email the email
	 * @param password the password
	 */
	public RegistrationDTO(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	/**
	 * Instantiates a new registration DTO.
	 *
	 * @param username the username
	 * @param email the email
	 * @param password the password
	 */
	public RegistrationDTO(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
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
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
