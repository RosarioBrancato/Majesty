package ch.fhnw.projectbois.leaderboard;

import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LeaderboardDTO;
import ch.fhnw.projectbois.dto.LeaderboardPlayerDTO;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.session.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * The Class LeaderboardModel.
 * @author Dario Stoeckli
 */
public class LeaderboardModel extends Model {
	
	private SimpleObjectProperty<LeaderboardDTO> leaderboardProperty = null;
	private SimpleObjectProperty<LeaderboardPlayerDTO> leaderboardPlayerProperty = null;
	
	/**
	 * Instantiates a new leaderboard model and initializes the Response Listener for network communication
	 */
	public LeaderboardModel() {
		this.leaderboardProperty = new SimpleObjectProperty<>();
		this.leaderboardPlayerProperty = new SimpleObjectProperty<>();
		this.initResponseListener();
	}
	
	/**
	 * Send request for Leaderboard to Server
	 */
	public void queryLeaderboard() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.GET_LEADERBOARD, null);
		Network.getInstance().sendRequest(request);
	}
	
	/**
	 * Gets the leaderboard property.
	 *
	 * @return the leaderboard property
	 */
	public SimpleObjectProperty<LeaderboardDTO> getLeaderboardProperty() {
		return this.leaderboardProperty;
	}
	
	/**
	 * Gets the leaderboard player property.
	 *
	 * @return the leaderboard player property
	 */
	public SimpleObjectProperty<LeaderboardPlayerDTO> getLeaderboardPlayerProperty() {
		return this.leaderboardPlayerProperty;
	}
	
	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Model#getChangeListener()
	 * Handle the response with two Property Objects that have listeners in the controller
	 */
	@Override
	protected ChangeListener<Response> getChangeListener() {
		return new ChangeListener<Response>() {

			@Override
			public void changed(ObservableValue<? extends Response> observable, Response oldValue, Response newValue) {
				
				//The leaderboard and the client associated player information is sent to the client for display
				if (newValue.getResponseId() == ResponseId.LEADERBOARD_PROVIDED) {
					String json = newValue.getJsonDataObject();
					LeaderboardDTO leaderboard = JsonUtils.Deserialize(json, LeaderboardDTO.class);
					leaderboardProperty.setValue(leaderboard);
				} else if (newValue.getResponseId() == ResponseId.LEADERBOARD_USER_INFO) {
					String json = newValue.getJsonDataObject();
					LeaderboardPlayerDTO currentplayer = JsonUtils.Deserialize(json, LeaderboardPlayerDTO.class);
					leaderboardPlayerProperty.setValue(currentplayer);
				}
			}
		};
	}

}
