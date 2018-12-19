package ch.fhnw.projectbois.requesthandlers;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LeaderboardDTO;
import ch.fhnw.projectbois.dto.LeaderboardPlayerDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;
import ch.fhnw.projectbois.queries.LeaderboardQuery;

/**
 * The Class LeaderboardRequestHandler.
 * @author Dario Stoeckli
 */
public class LeaderboardRequestHandler extends RequestHandler {

	/**
	 * Instantiates a new leaderboard request handler.
	 *
	 * @param request the request
	 * @param server the server
	 * @param client the client
	 */
	public LeaderboardRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois.requesthandlers.RequestHandler#handleRequest()
	 * Listens to request from client
	 */
	@Override
	protected void handleRequest() {
		if(request.getRequestId() == RequestId.GET_LEADERBOARD) {
			this.getLeaderboard();
		}
	}
	
	/**
	 * Gets the leaderboard from the DB and sends player specific information and a ranking list
	 * back to the client
	 *
	 * @return the leaderboard
	 */
	private void getLeaderboard() {
		LeaderboardQuery leaderboardquery = new LeaderboardQuery();
		LeaderboardDTO leaderboardDTO = leaderboardquery.getLeaderboard();
		LeaderboardPlayerDTO leaderboardPlayerDTO = leaderboardquery.getPlayerInfo(client.getUser().getUsername());
		
		String json = JsonUtils.Serialize(leaderboardDTO);
		Response response = new Response(ResponseId.LEADERBOARD_PROVIDED, request.getRequestId(), json);
		
		String json1 = JsonUtils.Serialize(leaderboardPlayerDTO);
		Response response1 = new Response (ResponseId.LEADERBOARD_USER_INFO, request.getRequestId(), json1);
		
		client.sendResponse(response);
		client.sendResponse(response1);
		
	}

}
