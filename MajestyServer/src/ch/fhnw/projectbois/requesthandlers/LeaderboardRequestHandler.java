package ch.fhnw.projectbois.requesthandlers;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LeaderboardDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;
import ch.fhnw.projectbois.queries.LeaderboardQuery;

public class LeaderboardRequestHandler extends RequestHandler {

	public LeaderboardRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);
	}

	@Override
	protected void handleRequest() {
		if(request.getRequestId() == RequestId.GET_LEADERBOARD) {
			this.getLeaderboard();
		}
	}
	
	private void getLeaderboard() {
		LeaderboardQuery leaderboardquery = new LeaderboardQuery();
		LeaderboardDTO leaderboardDTO = leaderboardquery.getLeaderboard();
		String json = JsonUtils.Serialize(leaderboardDTO);
		Response response = new Response(ResponseId.LEADERBOARD_PROVIDED, request.getRequestId(), json);
		
		client.sendResponse(response);
		
	}

}
