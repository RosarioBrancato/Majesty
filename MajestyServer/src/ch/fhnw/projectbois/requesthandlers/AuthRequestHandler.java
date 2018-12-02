package ch.fhnw.projectbois.requesthandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ch.fhnw.projectbois.access.DbAccess;
import ch.fhnw.projectbois.auth.LoginHandler;
import ch.fhnw.projectbois.auth.TokenFactory;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LoginDTO;
import ch.fhnw.projectbois.dto.UserDTO;
// import ch.fhnw.projectbois.general.IdFactory;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;

public class AuthRequestHandler extends RequestHandler {
	
	
	
	public AuthRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);
	}

	@Override
	protected void handleRequest() {
		if(request.getRequestId() == RequestId.LOGIN) {
			int DBuid = 0;
			String DBpassword = null;
			String DBsalt = null;
			TokenFactory token = null;
			UserDTO user = null;
			Response response = null;
			String json = "";
			LoginDTO loginRequest = JsonUtils.Deserialize(request.getJsonDataObject(), LoginDTO.class);
			String getUserString = "SELECT `uid`, `password`, `salt` FROM `user` WHERE `nickname` = ? LIMIT 1;";
			Connection con = null;
			PreparedStatement getUser = null;
			
			try {
				con = DbAccess.getConnection();
				getUser = con.prepareStatement(getUserString);
				getUser.setString(1, loginRequest.getUsername());
				ResultSet dbUsers = getUser.executeQuery();
				while(dbUsers.next()) {
					DBuid = dbUsers.getInt(1);
					DBpassword = dbUsers.getString(2);
					DBsalt = dbUsers.getString(3);
				}
				getUser.close();
			}catch(SQLException e) {
				logger.severe("Login failed due to database error: " + e.toString());
				response = new Response(ResponseId.AUTH_ERROR_SERVER, request.getRequestId(), json);
			}
			
			if(DBuid != 0 && response == null) {
				LoginHandler lh = new LoginHandler();
				if(lh.checkMatchingPasswords(loginRequest.getPassword(), DBpassword, DBsalt)) {
					token = TokenFactory.getInstance();
					//uid = IdFactory.getInstance().getNewId(UserDTO.class.getName());
					user = new UserDTO(DBuid, loginRequest.getUsername(), token.getNewToken());
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
	}

}
