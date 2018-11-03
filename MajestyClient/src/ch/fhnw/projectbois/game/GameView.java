package ch.fhnw.projectbois.game;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;

public class GameView extends View<GameModel> {

	public GameView(GameModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("GameView.fxml");
	}

}
