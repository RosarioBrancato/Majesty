package ch.fhnw.projectbois.playscreen;

import ch.fhnw.projectbois._mvc.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * 
 * @author Dario Stöckli
 *
 */

public class PlayScreenController extends Controller<PlayScreenModel, PlayScreenView> {
	
	public PlayScreenController(PlayScreenModel model, PlayScreenView view) {
		super(model, view);
	}
	
	@FXML
	public void btn_playscreen_start_test(ActionEvent e) {
		
	}
	
}
