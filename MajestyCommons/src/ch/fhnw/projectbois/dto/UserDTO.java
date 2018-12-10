/*
 * 
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.dto;

public class UserDTO {

	private int id;
	private String username;
	private String email;
	private String token;
	
	/**
	 * Instantiates a new user DTO.
	 */
	public UserDTO() {
		
	}
	
	/**
	 * Instantiates a new user DTO.
	 *
	 * @param id the id
	 * @param username the username
	 * @param token the token
	 */
	public UserDTO(int id, String username, String token) {
		this.id = id;
		this.username = username;
		this.token = token;
	}
		
	/**
	 * Instantiates a new user DTO.
	 *
	 * @param id the id
	 * @param username the username
	 * @param email the email
	 * @param token the token
	 */
	public UserDTO(int id, String username, String email, String token) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.token = token;
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
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the token.
	 *
	 * @return the token
	 */
	public String getToken() {
		return token;
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
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the token.
	 *
	 * @param token the new token
	 */
	public void setToken(String token) {
		this.token = token;
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
