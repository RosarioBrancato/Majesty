package ch.fhnw.projectbois.lobby;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;

public class LobbyView extends View<LobbyModel> {

	public LobbyView(LobbyModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("LobbyView.fxml");
	}

}
