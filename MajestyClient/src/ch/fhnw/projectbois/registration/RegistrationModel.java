package ch.fhnw.projectbois.registration;

import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LoginDTO;
import ch.fhnw.projectbois.dto.RegistrationDTO;
import ch.fhnw.projectbois.dto.ReportDTO;
import ch.fhnw.projectbois.dto.UserDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.session.Session;
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
	
	protected boolean RegistrationProcessInput(String server, int port, String username, String password, String email) {
		Network.getInstance().initConnection(server, port);
		String regReq = JsonUtils.Serialize(new RegistrationDTO(username, email, password));
		Request request = new Request("", RequestId.REGISTER, regReq);
		Network.getInstance().sendRequest(request);
		return true;
		
	}
	
	@Override
	protected ChangeListener<Response> getChangeListener() {
		return new ChangeListener<Response>() {
			@Override
			public void changed(ObservableValue<? extends Response> observable, Response oldValue, Response newValue) {
				/*if (newValue.getResponseId() == ResponseId.AUTH_OK) {
					UserDTO user = JsonUtils.Deserialize(newValue.getJsonDataObject(), UserDTO.class);
					Session session = Session.getInstance();
					session.setCurrentUser(user);
					logger.info("Login successful for user " + user.getUsername() + " (UID: " + user.getId() + ") - Token received: " + user.getToken());
					loggedInUser.setValue(user);
					
				} else if (newValue.getResponseId() == ResponseId.AUTH_ERROR_SERVER) {
					logger.warning("Login failed due to a general server error. Please check server logs for further information.");
					responseMsg.set("lbl_Login_loginMsg_GeneralServerError");
					
					String json = newValue.getJsonDataObject();
					ReportDTO report = JsonUtils.Deserialize(json, ReportDTO.class);
					getReportProperty().setValue(report);
					
				} else  if (newValue.getResponseId() == ResponseId.AUTH_ERROR_CREDENTIALS) {
					logger.warning("Login failed due to invalid credentials.");
					responseMsg.set("lbl_Login_loginMsg_CredentialsError");
				}*/
			}
		};
	}
}
