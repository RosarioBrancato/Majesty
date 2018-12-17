/**
 * Processes login requests against the server
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.login;

import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LoginDTO;
import ch.fhnw.projectbois.dto.UserDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.session.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * 
 * @author Alexandre Miccoli
 *
 */

public class LoginModel extends Model {
	
	private SimpleObjectProperty<UserDTO> loggedInUser = null;
	private SimpleObjectProperty<String> responseMsg = null;

	/**
	 * Instantiates a new login model.
	 */
	public LoginModel() {
		this.loggedInUser = new SimpleObjectProperty<>();
		this.responseMsg = new SimpleObjectProperty<>();
		this.initResponseListener();
	}
	
	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Model#getChangeListener()
	 */
	/**
	 * Listens to the responses belonging to the authentication (auth) section.
	 * @see ch.fhnw.projectbois.communication
	 */
	@Override
	protected ChangeListener<Response> getChangeListener() {
		return new ChangeListener<Response>() {
			@Override
			public void changed(ObservableValue<? extends Response> observable, Response oldValue, Response newValue) {
				if (newValue.getResponseId() == ResponseId.AUTH_OK) {
					UserDTO user = JsonUtils.Deserialize(newValue.getJsonDataObject(), UserDTO.class);
					Session session = Session.getInstance();
					session.setCurrentUser(user);
					logger.info("Login successful for user " + user.getUsername() + " (UID: " + user.getId() + ") - Token received: " + user.getToken());
					loggedInUser.setValue(user);
				} else if (newValue.getResponseId() == ResponseId.AUTH_ERROR_SERVER) {
					logger.warning("Login failed due to a general server error. Please check server logs for further information.");
					responseMsg.set("lbl_Login_loginMsg_GeneralServerError");
				} else  if (newValue.getResponseId() == ResponseId.AUTH_ERROR_CREDENTIALS) {
					logger.warning("Login failed due to invalid credentials.");
					responseMsg.set("lbl_Login_loginMsg_CredentialsError");
				} else if (newValue.getResponseId() == ResponseId.AUTH_ERROR_ALREADYLOGGEDIN) {
					logger.warning("Login failed. Another user with the same username is already logged in.");
					responseMsg.set("lbl_Login_loginMsg_UserAlreadyLoggedIn");
				} else {
					logger.warning("Login failed. Empty response.");
					responseMsg.set("lbl_Login_loginMsg_GeneralServerError");
				}
			}
		};
	}
	
	/**
	 * Returns the logged in user.
	 * Null while no UserDTO has been returned by the server
	 *
	 * @return the logged in user (DTO)
	 */
	protected SimpleObjectProperty<UserDTO> getLoggedInUser() {
		return this.loggedInUser;
	}
	
	/**
	 * Returns the response message, which has to be shown in the status bar.
	 *
	 * @return the login status
	 */
	protected SimpleObjectProperty<String> getLoginStatus() {
		return this.responseMsg;
	}
	
	/**
	 * Prepares and sends a login request to the server.
	 *
	 * @param server the server
	 * @param port_in the server port
	 * @param username the username
	 * @param password the password
	 */
	protected void LoginProcessCredentials(String server, String port_in, String username, String password) {
		int port = Integer.parseInt(port_in);
		
		boolean success = Network.getInstance().initConnection(server, port);
		if(!success) {
			this.responseMsg.set("lbl_Login_loginMsg_ServerError");
		}
		
		String login = JsonUtils.Serialize(new LoginDTO(username, password));
		Request request = new Request("", RequestId.LOGIN, login);
		Network.getInstance().sendRequest(request);
	}
	
	/**
	 * Resets the authentication/login status.
	 * Used when a new timeout timer is started.
	 */
	public void resetStatus() {
		this.responseMsg.set(null);
	}
	
}
