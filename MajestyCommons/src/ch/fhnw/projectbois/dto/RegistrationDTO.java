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
	
	public RegistrationDTO() {
		
	}
	
	public RegistrationDTO(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
