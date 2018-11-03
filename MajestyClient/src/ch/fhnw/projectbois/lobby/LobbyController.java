package ch.fhnw.projectbois.lobby;

import ch.fhnw.projectbois._mvc.Controller;

public class LobbyController extends Controller<LobbyModel, LobbyView> {

	public LobbyController() {
	}
	
	public LobbyController(LobbyModel model, LobbyView view) {
		super(model, view);
	}
	
}
