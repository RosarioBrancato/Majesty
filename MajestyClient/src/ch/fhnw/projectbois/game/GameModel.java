package ch.fhnw.projectbois.game;

import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.ReportDTO;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.gameobjects.GameMove;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.session.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class GameModel extends Model {

	private SimpleObjectProperty<GameState> gameStateProperty = null;

	public GameModel() {
		this.gameStateProperty = new SimpleObjectProperty<>();
		this.initResponseListener();
	}

	public SimpleObjectProperty<GameState> getGameStateProperty() {
		return this.gameStateProperty;
	}

	public void getGameState() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.GET_GAMESTATE, null);
		Network.getInstance().sendRequest(request);
	}

	public void sendMove(GameMove move) {
		String json = JsonUtils.Serialize(move);
		Request request = new Request(Session.getCurrentUserToken(), RequestId.DO_MOVE, json);

		Network.getInstance().sendRequest(request);
	}

	@Override
	protected ChangeListener<Response> getChangeListener() {
		return new ChangeListener<Response>() {
			@Override
			public void changed(ObservableValue<? extends Response> observable, Response oldValue, Response newValue) {

				if (newValue.getResponseId() == ResponseId.UPDATE_GAMESTATE) {
					String json = newValue.getJsonDataObject();
					GameState gameState = JsonUtils.Deserialize(json, GameState.class);

					gameStateProperty.setValue(gameState);
				
				} else if(newValue.getResponseId() == ResponseId.GAME_ERROR) {
					String json = newValue.getJsonDataObject();
					ReportDTO report = JsonUtils.Deserialize(json, ReportDTO.class);
					
					getReportProperty().setValue(report);
				}
			}
		};
	}

}
