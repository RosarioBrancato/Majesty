package ch.fhnw.projectbois.playscreen;

import ch.fhnw.projectbois._mvc.Controller;

/**
 * 
 * @author Dario Stöckli
 *
 */

public class PlayScreenController extends Controller<PlayScreenModel, PlayScreenView> {

	public PlayScreenController() {
		
	}
	
	public PlayScreenController(PlayScreenModel model, PlayScreenView view) {
		super(model, view);
	}
	
}
