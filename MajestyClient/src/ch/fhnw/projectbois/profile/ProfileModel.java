/*
 * 
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.profile;

import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.RegistrationDTO;
import ch.fhnw.projectbois.dto.UserDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.session.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ProfileModel extends Model {

	private SimpleObjectProperty<String> profUpdateStat = null;

	/**
	 * Instantiates a new profile model.
	 */
	public ProfileModel() {
		this.profUpdateStat = new SimpleObjectProperty<>();
		this.initResponseListener();
	}
	
	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Model#getChangeListener()
	 */
	@Override
	protected ChangeListener<Response> getChangeListener() {
		return new ChangeListener<Response>() {
			@Override
			public void changed(ObservableValue<? extends Response> observable, Response oldValue, Response newValue) {
				if(newValue.getResponseId() == ResponseId.PROFILE_UPDATE_SUCCESS) {
					profUpdateStat.set("OK");
					String newEmail = JsonUtils.Deserialize(newValue.getJsonDataObject(), UserDTO.class).getEmail();
					if(newEmail != null && !newEmail.equals(Session.getCurrentEmail()))
						Session.getInstance().getCurrentUser().setEmail(newEmail);
				} else if(newValue.getResponseId() == ResponseId.PROFILE_UPDATE_ERROR) {
					logger.warning("Profile update failed due to an unspecified or database error on the server side. Please check server logs for further information.");
					profUpdateStat.set("lbl_Profile_Response_DBError");
				} else if(newValue.getResponseId() == ResponseId.PROFILE_UPDATE_ERROR_BAD_CREDENTIALS) {
					logger.warning("Registration failed due to invalid credentials submitted.");
					profUpdateStat.set("lbl_Profile_Response_Success");
				}
			}
		};
	}
	
	/**
	 * Gets the prof update status.
	 *
	 * @return the prof update status
	 */
	public SimpleObjectProperty<String> getProfUpdateStatus() {
		return this.profUpdateStat;
	}
	
	/**
	 * Reset status.
	 */
	public void resetStatus() {
		this.profUpdateStat.set(null);
	}
	
	/**
	 * Update profile process input.
	 *
	 * @param email the email
	 * @param password the password
	 */
	protected void UpdateProfileProcessInput(String email, String password) {
		String update = JsonUtils.Serialize(new RegistrationDTO(email, password));
		Request request = new Request(Session.getCurrentUserToken(), RequestId.PROFILE_UPDATE, update);
		Network.getInstance().sendRequest(request);
	}

}
