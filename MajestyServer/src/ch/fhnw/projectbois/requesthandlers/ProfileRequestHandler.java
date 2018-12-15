/**
 * Handles requests for changing profile information from clients
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.requesthandlers;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.RegistrationDTO;
import ch.fhnw.projectbois.dto.UserDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;
import ch.fhnw.projectbois.queries.UserHandler;
import ch.fhnw.projectbois.validation.CredentialsValidator;

public class ProfileRequestHandler extends RequestHandler {
	
	/**
	 * Instantiates a new profile request handler.
	 *
	 * @param request the request
	 * @param server the server
	 * @param client the client
	 */
	public ProfileRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);
	}
	
	/**
	 * Handles requests for profile modifications and initiates a response with a response type 
	 * and modified parameters for the client in case of successful change
	 *
	 */
	@Override
	protected void handleRequest() {
		if(request.getRequestId() == RequestId.PROFILE_UPDATE) {
			int uid = this.client.getUser().getId();
			RegistrationDTO req = JsonUtils.Deserialize(request.getJsonDataObject(), RegistrationDTO.class);
			CredentialsValidator cv = CredentialsValidator.getInstance();
			UserHandler uh = UserHandler.getInstance();
			Response response = null;
			
			boolean pwdChange = !(req.getPassword() == null || req.getPassword().equals(""));
			boolean pwd_ok = cv.passwordStrengthIsSufficient(req.getPassword());
			boolean emailChange = !(req.getEmail().equals(client.getUser().getEmail()));
			boolean email_ok = cv.stringIsValidEmailAddress(req.getEmail());
			
			UserDTO newUser = new UserDTO();
			
			try {
				if((pwdChange && pwd_ok) && (emailChange && email_ok)) {
					uh.updateEmailPassword(uid, req.getEmail(), req.getPassword());
					client.getUser().setEmail(req.getEmail());
					newUser.setEmail(req.getEmail());
				}else if(pwdChange && pwd_ok && !emailChange) {
					uh.updatePassword(uid, req.getPassword());
				}else if(emailChange && email_ok && !pwdChange) {
					uh.updateEmail(uid, req.getEmail());
					client.getUser().setEmail(req.getEmail());
					newUser.setEmail(req.getEmail());
				}else {
					// Cannot handle the new credentials received
					logger.warning( "Cannot update profile. Bad credentials or invalid request:"
							+ "\n REQ_PwdChange " + pwdChange 
							+ "\n REQ_PwdStrengthOk " + pwd_ok 
							+ "\n REQ_EmailChange " + emailChange 
							+ "\n REQ_EmailFormatOk " + email_ok );
					response = new Response(ResponseId.PROFILE_UPDATE_ERROR_BAD_CREDENTIALS, request.getRequestId(), "");
				}
				
				response = new Response(ResponseId.PROFILE_UPDATE_SUCCESS, request.getRequestId(), JsonUtils.Serialize(newUser));
			}catch(Exception e) {
				logger.severe("Database error occurred while updating user profile: " + e.getMessage());
				response = new Response(ResponseId.PROFILE_UPDATE_ERROR_DATABASE, request.getRequestId(), "");
			}
			
			client.sendResponse(response);
		}
	}

}
