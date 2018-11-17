package ch.fhnw.projectbois.session;

import ch.fhnw.projectbois.dto.UserDTO;

public class Session {

	private static Session instance = null;
	
	
	private UserDTO currentUser = null;
	
	
	private Session() {
		this.currentUser = new UserDTO();
		this.currentUser.setToken("PLACEHOLDER");
	}
	
	public static Session getInstance() {
		if(instance == null) {
			instance = new Session();
		}
		return instance;
	}
	
	public void setCurrentUser(UserDTO user) {
		this.currentUser = user;
	}
	
	public UserDTO getCurrentUser() {
		return this.currentUser;
	}
	
}
