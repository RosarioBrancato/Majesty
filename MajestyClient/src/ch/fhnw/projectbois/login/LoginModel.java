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

	public LoginModel() {
		this.loggedInUser = new SimpleObjectProperty<>();
		this.initResponseListener();
	}
	
	protected SimpleObjectProperty<UserDTO> getLoggedInUser() {
		return this.loggedInUser;
	}
	
	protected boolean LoginProcessCredentials(String server, String port_in, String username, String password) {
		int port = 0;
				
		try {
			port = Integer.parseInt(port_in);
		}catch(Exception e) {
			return false;
		}
		
		Network.getInstance().initConnection(server, port);
		String login = JsonUtils.Serialize(new LoginDTO(username, password));
		Request request = new Request("", RequestId.LOGIN, login);
		Network.getInstance().sendRequest(request);
		return true;
		
	}
	
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
				}
				if (newValue.getResponseId() == ResponseId.AUTH_ERROR) {
					logger.warning("Login unsuccessful");
				}
			}
		};
	}
	
}
