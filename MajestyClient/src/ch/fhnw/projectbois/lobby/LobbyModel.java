package ch.fhnw.projectbois.lobby;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.game.GameController;
import ch.fhnw.projectbois.game.GameModel;
import ch.fhnw.projectbois.game.GameView;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.session.Session;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class LobbyModel extends Model {

	public LobbyModel() {
		this.initResponseListener();
	}

	public void startGame() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.START_GAME, null);
		Network.getInstance().sendRequest(request);
	}

	@Override
	protected ChangeListener<Response> getChangeListener() {
		return new ChangeListener<Response>() {

			@Override
			public void changed(ObservableValue<? extends Response> observable, Response oldValue, Response newValue) {

				if (newValue.getResponseId() == ResponseId.GAME_STARTED) {
					showGameBoard();
				}
			}
		};
	}

	private void showGameBoard() {
		Platform.runLater(() -> {
			GameController controller = Controller.initMVC(GameController.class, GameModel.class, GameView.class);
			MetaContainer.getInstance().setRoot(controller.getViewRoot());
		});
	}

}
