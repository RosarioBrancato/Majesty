/**
 * Registration request data transfer object (DTO) to exchange between server and client
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
	 * Used for profile changes
	 *
	 * @param email the requested new email
	 * @param password the requested new password
	 */
	public RegistrationDTO(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	/**
	 * Instantiates a new registration DTO.
	 * Used for new profile creation.
	 *
	 * @param username the wished username
	 * @param email the requested email
	 * @param password the requested password
	 */
	public RegistrationDTO(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
	/**
	 * Returns the email address out of the DTO.
	 *
	 * @return the email out of the DTO
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Returns the password out of the DTO.
	 *
	 * @return the password out of the DTO
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Returns the username out of the DTO.
	 *
	 * @return the username out of the DTO
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sets the email address in the DTO.
	 *
	 * @param email the email address to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
