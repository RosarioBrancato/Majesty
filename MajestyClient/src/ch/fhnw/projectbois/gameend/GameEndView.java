package ch.fhnw.projectbois.gameend;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;

public class GameEndView extends View<GameEndModel> {

	public GameEndView(GameEndModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("GameEndView.fxml");
	}

}
