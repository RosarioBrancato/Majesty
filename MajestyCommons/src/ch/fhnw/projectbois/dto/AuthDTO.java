/**
 * Authentication request data transfer object (DTO) to use when getting a user from DB for authentication purposes
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.dto;

/**
 * 
 * @author Alexandre Miccoli
 *
 */

public class AuthDTO {
	private int uid = 0;
	private String email = null;
	private String password = null;
	private String salt = null;
	
	/**
	 * Instantiates a new authentication DTO.
	 */
	public AuthDTO() {
		this.uid = 0;
		this.email = null;
		this.password = null;
		this.salt = null;
	}
	
	/**
	 * Instantiates a new authentication DTO.
	 *
	 * @param uid the DB identifier for the user
	 * @param password the hashed password of the user
	 * @param salt the salt used in the hashed password
	 */
	public AuthDTO(int uid, String email, String password, String salt) {
		this.uid = uid;
		this.email = email;
		this.password = password;
		this.salt = salt;
	}
	
	/**
	 * Returns the DB identifier for the user out of the DTO.
	 *
	 * @return the UID out of the DTO
	 */
	public int getUid() {
		return uid;
	}
	
	/**
	 * Returns the email address of the user out of the DTO.
	 *
	 * @return the email address out of the DTO
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Returns the hashed password of the user out of the DTO.
	 *
	 * @return the hashed password out of the DTO
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Returns the salt used in the hashed password out of the DTO.
	 *
	 * @return the salt out of the DTO
	 */
	public String getSalt() {
		return salt;
	}
	
	/**
	 * Sets the DB identifier for the user into the DTO.
	 *
	 * @return the UID to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}
	
	/**
	 * Sets the email address of the user into the DTO.
	 *
	 * @return the email address to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Sets the hashed password of the user into the DTO.
	 *
	 * @return the hashed password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Sets the salt used in the hashed password into the DTO.
	 *
	 * @return the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}
}
