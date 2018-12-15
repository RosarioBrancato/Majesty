/**
 * Login/Credentials data transfer object (DTO) to exchange between server and client
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.dto;

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
	 * @param user the username entered
	 * @param pass the password entered
	 */
	public LoginDTO(String user, String pass) {
		this.username = user;
		this.password = pass;
	}
	
	/**
	 * Gets the password out of the DTO.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Gets the username out of the DTO.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sets the password in the DTO.
	 *
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Sets the username in the DTO.
	 *
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
}
