/**
 * Processes authentication/login and registration requests from the clients
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.requesthandlers;

import ch.fhnw.projectbois.auth.TokenFactory;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.AuthDTO;
import ch.fhnw.projectbois.dto.LoginDTO;
import ch.fhnw.projectbois.dto.RegistrationDTO;
import ch.fhnw.projectbois.dto.UserDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;
import ch.fhnw.projectbois.queries.PasswordHandler;
import ch.fhnw.projectbois.queries.UserHandler;
import ch.fhnw.projectbois.validation.CredentialsValidator;

public class AuthRequestHandler extends RequestHandler {
	
	/**
	 * Instantiates a new auth request handler.
	 *
	 * @param request the request
	 * @param server the server
	 * @param client the client
	 */
	public AuthRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);
	}

	/**
	 * 
	 * Handles registration and login requests and prepares a response with response ID.
	 * In successful login cases, the response includes a UserDTO object with an authentication token.
	 * 
	 * Conditions for Login request: User exists, credentials are OK, user is not already logged in.
	 * Conditions for Registration request: User does not exist, credentials are validated
	 * 
	 */
	@Override
	protected void handleRequest() {
		
		// Case: Login
		
		if(request.getRequestId() == RequestId.LOGIN) {
			Response response = null;
			String json = "";
			LoginDTO loginRequest = JsonUtils.Deserialize(request.getJsonDataObject(), LoginDTO.class);
			UserHandler uh = UserHandler.getInstance();
			AuthDTO authUser = new AuthDTO();
			
			try {
				authUser = uh.authGetUserFromDB(loginRequest.getUsername());
			}catch(Exception e) {
				logger.severe("Login failed due to database error: " + e.toString());
				response = new Response(ResponseId.AUTH_ERROR_SERVER, request.getRequestId(), json);
			}
			
			if(authUser.getUid() == 0) {
				logger.warning("Login failed. The requested user does not exist.");
				response = new Response(ResponseId.AUTH_ERROR_CREDENTIALS, request.getRequestId(), json);
			}
			
			int DBuid = authUser.getUid();
			String DBpassword = authUser.getPassword();
			String DBsalt = authUser.getSalt();
			String DBemail = authUser.getEmail();
			TokenFactory token = null;
			UserDTO user = null;
			
			if(response == null) {
				boolean isLoggedIn = server.getClients().stream()
						.filter(f -> f.getUser() != null && f.getUser().getUsername().equals(loginRequest.getUsername())).findAny().isPresent();
				
				
				if(isLoggedIn) {
					logger.severe("Login failed. User is already logged in: " + loginRequest.getUsername());
					response = new Response(ResponseId.AUTH_ERROR_ALREADYLOGGEDIN, request.getRequestId(), json);
				}
			}
			
			if(DBuid != 0 && response == null) {
				PasswordHandler ph = PasswordHandler.getInstance();
				if(ph.checkMatchingPasswords(loginRequest.getPassword(), DBpassword, DBsalt)) {
					token = TokenFactory.getInstance();
					user = new UserDTO(DBuid, loginRequest.getUsername(), DBemail, token.getNewToken());
					client.setUser(user);
					json = JsonUtils.Serialize(user);
					response = new Response(ResponseId.AUTH_OK, request.getRequestId(), json);
				}else {
					response = new Response(ResponseId.AUTH_ERROR_CREDENTIALS, request.getRequestId(), json);
					logger.severe("Login failed due to invalid user input. Requested User: " + loginRequest.getUsername());
				}
			}

			client.sendResponse(response);
		}
		
		// Case: Registration
		
		if(request.getRequestId() == RequestId.REGISTER) {
			CredentialsValidator cv = CredentialsValidator.getInstance();
			Response response = null;
			RegistrationDTO regReq = JsonUtils.Deserialize(request.getJsonDataObject(),RegistrationDTO.class);
			UserHandler uh = UserHandler.getInstance();
			int uid = 0;
			
			if(!cv.stringIsAlphanumeric(regReq.getUsername()) || !cv.stringIsValidEmailAddress(regReq.getEmail()) || !cv.passwordStrengthIsSufficient(regReq.getPassword())) {
				uid = -99;
			}
			
			try {
				if(uid == 0) {
					uid = uh.createUser(regReq.getUsername(), regReq.getEmail(), regReq.getPassword());
				}
				
				if(uid > 0) {
					response = new Response(ResponseId.REGISTRATION_SUCCESS, request.getRequestId(), "");
				}else if (uid == -1) {
					logger.severe("Registration failed because the user already exists.");
					response = new Response(ResponseId.REGISTRATION_ERROR_USER_ALREADY_EXISTS, request.getRequestId(), "");
				}else if(uid == -2) {
					logger.severe("Registration failed because the user already exists.");
					response = new Response(ResponseId.REGISTRATION_ERROR_USER_ALREADY_EXISTS, request.getRequestId(), "");
				}else {
					logger.severe("Registration failed due to invalid credentials submitted.");
					response = new Response(ResponseId.REGISTRATION_ERROR_BAD_CREDENTIALS, request.getRequestId(), "");
				}
			} catch (Exception e) {
				logger.severe("Registration failed due to database error: " + e.toString());
				response = new Response(ResponseId.REGISTRATION_ERROR_DATABASE, request.getRequestId(), "");
			}
			
			client.sendResponse(response);
		}
	}

}
