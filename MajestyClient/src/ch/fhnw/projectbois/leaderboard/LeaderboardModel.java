package ch.fhnw.projectbois.leaderboard;

import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LeaderboardDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.session.Session;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class LeaderboardModel extends Model {
	
	public LeaderboardModel() {
		this.initResponseListener();
	}
	
	public void getLeaderboard() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.GET_LEADERBOARD, null);
		Network.getInstance().sendRequest(request);
	}
	
	@Override
	protected ChangeListener<Response> getChangeListener() {
		return new ChangeListener<Response>() {

			@Override
			public void changed(ObservableValue<? extends Response> observable, Response oldValue, Response newValue) {
				
				//The leaderboard is sent to the client for display
				if (newValue.getResponseId() == ResponseId.LEADERBOARD_PROVIDED) {
					String json = newValue.getJsonDataObject();
					LeaderboardDTO leaderboard = JsonUtils.Deserialize(json, LeaderboardDTO.class);
					System.out.println(leaderboard.getLeaderboard().get(0).getUsername());
				} 
			}
		};
	}

}
