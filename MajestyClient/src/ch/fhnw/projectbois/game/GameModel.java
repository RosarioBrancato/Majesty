package ch.fhnw.projectbois.game;

import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.json.JsonUtils;
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

	@Override
	protected ChangeListener<Response> getChangeListener() {
		return new ChangeListener<Response>() {
			@Override
			public void changed(ObservableValue<? extends Response> observable, Response oldValue, Response newValue) {

				if (newValue.getResponse() == ResponseId.UPDATE_GAMESTATE) {
					String json = newValue.getJsonDataObject();
					GameState gameState = JsonUtils.Deserialize(json, GameState.class);

					gameStateProperty.setValue(gameState);
					logger.info("GameModel - GameStateProperty updated!");
				}
			}
		};
	}

}
