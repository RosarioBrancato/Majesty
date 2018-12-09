package ch.fhnw.projectbois.dto;

public class UserDTO {

	private int id;
	private String username;
	private String email;
	private String token;
	
	public UserDTO() {
		
	}
	
	public UserDTO(int id, String username, String token) {
		this.id = id;
		this.username = username;
		this.token = token;
	}
		
	public UserDTO(int id, String username, String email, String token) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.token = token;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
