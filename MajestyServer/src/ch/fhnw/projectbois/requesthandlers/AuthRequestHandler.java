package ch.fhnw.projectbois.requesthandlers;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import ch.fhnw.projectbois.access.DbAccess;
import ch.fhnw.projectbois.auth.LoginHandler;
import ch.fhnw.projectbois.auth.TokenFactory;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LoginDTO;
import ch.fhnw.projectbois.dto.UserDTO;
import ch.fhnw.projectbois.general.IdFactory;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;

public class AuthRequestHandler extends RequestHandler {
	
	
	
	public AuthRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	protected void handleRequest() {
		if(request.getRequestId() == RequestId.LOGIN) {
			//TO-DO: real login
			int uid;
			String DBpassword = null;
			String DBsalt = null;
			LoginDTO loginRequest = JsonUtils.Deserialize(request.getJsonDataObject(), LoginDTO.class);
			String getUserString = "SELECT `uid`, `password`, `salt` FROM `user` WHERE `nickname` = ? LIMIT 1;";
			Connection con = null;
			PreparedStatement getUser = null;
			
			try {
				con = DbAccess.getConnection();
				getUser = con.prepareStatement(getUserString);
				getUser.setString(1, "alex");
				ResultSet dbUsers = getUser.executeQuery();
				while(dbUsers.next()) {
					DBpassword = dbUsers.getString(2);
					DBsalt = dbUsers.getString(3);
				}
				getUser.close();
			}catch(SQLException e) {
				System.out.println("DB ERROR:");
				e.printStackTrace();
			}
			
			
			LoginHandler lh = new LoginHandler();
			if(lh.checkMatchingPasswords(loginRequest.getPassword(), DBpassword, DBsalt)) {
				System.out.println("Login OK");
			}else {
				System.out.println("Login Forbidden");
			}
			
			
			TokenFactory token = TokenFactory.getInstance();
			int id = IdFactory.getInstance().getNewId(UserDTO.class.getName());
			UserDTO user = new UserDTO(id, "Alex" + id, token.getNewToken());
			client.setUser(user);
			
			//response
			String json = JsonUtils.Serialize(user);
			Response response = new Response(ResponseId.AUTH_OK, request.getRequestId(), json);
			client.sendResponse(response);
		}
	}

}
