package ch.fhnw.projectbois.registration;

import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.RegistrationDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Network;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class RegistrationModel extends Model {
	// Registration Status
	private SimpleObjectProperty<String> regStat = null;

	public RegistrationModel() {
		this.regStat = new SimpleObjectProperty<>();
		this.initResponseListener();
	}
	
	public SimpleObjectProperty<String> getRegistrationStatus() {
		return this.regStat;
	}
	
	protected void RegistrationProcessInput(String server, int port, String username, String password, String email) {
		try {
			Network.getInstance().initConnection(server, port);
		}catch(Exception e) {
			this.regStat.set("lbl_Login_loginMsg_ServerError");
		}
		
		String regReq = JsonUtils.Serialize(new RegistrationDTO(username, email, password));
		Request request = new Request("", RequestId.REGISTER, regReq);
		Network.getInstance().sendRequest(request);
	}
	
	@Override
	protected ChangeListener<Response> getChangeListener() {
		return new ChangeListener<Response>() {
			@Override
			public void changed(ObservableValue<? extends Response> observable, Response oldValue, Response newValue) {
				if(newValue.getResponseId() == ResponseId.REGISTRATION_SUCCESS) {
					regStat.set("OK");
				} else if(newValue.getResponseId() == ResponseId.REGISTRATION_ERROR_USER_ALREADY_EXISTS) {
					logger.warning("Registration failed. User already exists.");
					regStat.set("lbl_Registration_Response_UserAlreadyExists");
				} else if(newValue.getResponseId() == ResponseId.REGISTRATION_ERROR_DATABASE) {
					logger.warning("Registration failed due to a server-side error. Please check server logs for further information.");
					regStat.set("lbl_Registration_Response_DBError");
				} else if(newValue.getResponseId() == ResponseId.REGISTRATION_ERROR_BAD_CREDENTIALS) {
					logger.warning("Registration failed due to invalid credentials submitted.");
					regStat.set("lbl_Registration_Response_DBError");
				}
			}
		};
	}

	public void resetStatus() {
		this.regStat.set(null);
	}
}
