/**
 * User data transfer object (DTO) to exchange between server and client
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
	 * Instantiates a new user data transfer object.
	 * Used for the authentication
	 *
	 * @param id the unique database identifier of the user
	 * @param username the username of the user
	 * @param token the authentication token to be sent with each request
	 */
	public UserDTO(int id, String username, String token) {
		this.id = id;
		this.username = username;
		this.token = token;
	}
		
	/**
	 * Instantiates a new user data transfer object.
	 * Used when the email address is changed
	 *
	 * @param id the unique database identifier of the user
	 * @param username the username of the user
	 * @param email the new email address of a user
	 * @param token the authentication token to be sent with each request
	 * @param email the email
	 */
	public UserDTO(int id, String username, String email, String token) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.token = token;
	}

	/**
	 * Get email address out of the DTO.
	 *
	 * @return the email address set in the DTO
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Gets the id out of the DTO.
	 *
	 * @return the id set in the DTO
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Get the token out of the DTO.
	 *
	 * @return the token set in the DTO
	 */
	public String getToken() {
		return token;
	}
	
	/**
	 * Get the username out of the DTO.
	 *
	 * @return the username set in the DTO
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the email address in a DTO.
	 *
	 * @param email the new email address
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Sets the UID in a DTO.
	 *
	 * @param id the uid (unique identifier out of the database)
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the token in a DTO.
	 *
	 * @param token the new token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Sets the username in a DTO.
	 *
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}
