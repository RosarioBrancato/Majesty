package ch.fhnw.projectbois.game;

import java.util.ArrayList;

import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.ReportDTO;
import ch.fhnw.projectbois.enumerations.ReportSeverity;
import ch.fhnw.projectbois.gameobjects.GameMove;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.gameobjects.Player;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.session.Session;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * The Class GameModel.
 * 
 * @author Rosario Brancato
 */
public class GameModel extends Model {

	private int playerIndex = -1;
	private SimpleObjectProperty<GameState> gameStateProperty = null;
	private SimpleBooleanProperty gameEndProperty;

	/**
	 * Instantiates a new game model.
	 */
	public GameModel() {
		this.gameStateProperty = new SimpleObjectProperty<>();
		this.gameEndProperty = new SimpleBooleanProperty();
		this.initResponseListener();
	}

	/**
	 * Define players index.
	 */
	public void definePlayersIndex() {
		ArrayList<Player> players = gameStateProperty.getValue().getBoard().getPlayers();

		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getUsername().equals(Session.getCurrentUsername())) {
				this.playerIndex = i;
				break;
			}
		}
	}

	/**
	 * Request game state.
	 */
	public void requestGameState() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.GET_GAMESTATE, null);
		Network.getInstance().sendRequest(request);
	}

	/**
	 * Send move.
	 *
	 * @param move the move
	 */
	public void sendMove(GameMove move) {
		String json = JsonUtils.Serialize(move);
		Request request = new Request(Session.getCurrentUserToken(), RequestId.DO_MOVE, json);

		Network.getInstance().sendRequest(request);
	}

	/**
	 * Leave game.
	 */
	public void leaveGame() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.lEAVE_GAME, null);
		Network.getInstance().sendRequest(request);
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Model#getChangeListener()
	 */
	@Override
	protected ChangeListener<Response> getChangeListener() {
		return new ChangeListener<Response>() {
			@Override
			public void changed(ObservableValue<? extends Response> observable, Response oldValue, Response newValue) {

				if (newValue.getResponseId() == ResponseId.UPDATE_GAMESTATE) {
					String json = newValue.getJsonDataObject();
					GameState gameState = JsonUtils.Deserialize(json, GameState.class);

					if (gameStateProperty.getValue() == null || (gameStateProperty.getValue() != null
							&& gameState.getId() >= gameStateProperty.getValue().getId())) {
						
						gameStateProperty.setValue(gameState);
					}

				} else if (newValue.getResponseId() == ResponseId.GAME_ENDED) {
					String json = newValue.getJsonDataObject();
					GameState gameState = JsonUtils.Deserialize(json, GameState.class);
					gameStateProperty.setValue(gameState);
					gameEndProperty.setValue(true);

				} else if (newValue.getResponseId() == ResponseId.GAME_PLAYER_LEFT) {
					String json = newValue.getJsonDataObject();
					GameState gameState = JsonUtils.Deserialize(json, GameState.class);
					gameStateProperty.setValue(gameState);

					ReportDTO report = new ReportDTO(ReportSeverity.INFO, "inf_Player_Left_Game");
					getReportProperty().setValue(report);

				} else if (newValue.getResponseId() == ResponseId.GAME_TURN_TIME_OVER) {
					String json = newValue.getJsonDataObject();
					GameState gameState = JsonUtils.Deserialize(json, GameState.class);
					gameStateProperty.setValue(gameState);

					ReportDTO report = new ReportDTO(ReportSeverity.WARNING, "wrn_Game_Turn_Time_Over");
					getReportProperty().setValue(report);

				} else if (newValue.getResponseId() == ResponseId.GAME_ERROR) {
					String json = newValue.getJsonDataObject();
					ReportDTO report = JsonUtils.Deserialize(json, ReportDTO.class);

					getReportProperty().setValue(report);
				}
			}
		};
	}

	// GETTER AND SETTER

	/**
	 * Gets the player index.
	 *
	 * @return the player index
	 */
	public int getPlayerIndex() {
		return playerIndex;
	}

	/**
	 * Gets the game state.
	 *
	 * @return the game state
	 */
	public GameState getGameState() {
		return this.gameStateProperty.getValue();
	}

	/**
	 * Gets the game state property.
	 *
	 * @return the game state property
	 */
	public SimpleObjectProperty<GameState> getGameStateProperty() {
		return this.gameStateProperty;
	}

	/**
	 * Gets the game end property.
	 *
	 * @return the game end property
	 */
	public SimpleBooleanProperty getGameEndProperty() {
		return this.gameEndProperty;
	}

}
