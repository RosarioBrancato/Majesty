package ch.fhnw.projectbois.game;

import ch.fhnw.projectbois._mvc.Controller;

public class GameController extends Controller<GameModel, GameView> {

	public GameController() {
	}
	
	public GameController(GameModel model, GameView view) {
		super(model, view);
	}
	
}
