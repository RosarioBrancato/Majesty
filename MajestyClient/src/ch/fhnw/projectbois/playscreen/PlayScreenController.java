package ch.fhnw.projectbois.playscreen;

import ch.fhnw.projectbois._mvc.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

/**
 * 
 * @author Dario Stöckli
 *
 */

public class PlayScreenController extends Controller<PlayScreenModel, PlayScreenView> {
	
	public PlayScreenController(PlayScreenModel model, PlayScreenView view) {
		super(model, view);
	}
	
	
	//Preparing List for GameMode ChoiceBox
	ObservableList<String> gamemode_list=FXCollections.observableArrayList();
	
	private void loadData() {
		//Loading Dropdown List for Hosting game
		gamemode_list.removeAll(gamemode_list);
		String a = "A";
		String b = "B";
		gamemode_list.addAll(a,b);
		choicebx_playscreen_cardside.getItems().addAll(gamemode_list);
		choicebx_playscreen_cardside.getSelectionModel().selectFirst();
		//Loading Games to join
		model.getLobbies();
		// TO DO
	}

    @FXML
    public void initialize() {
        loadData();
    }
	
	@FXML
	private ChoiceBox<String> choicebx_playscreen_cardside;

	@FXML
	public void btn_playscreen_start(ActionEvent e) {
	System.out.println(choicebx_playscreen_cardside.getValue());
	String gamemode = choicebx_playscreen_cardside.getValue();
	model.createLobby(gamemode);
	
		
	}
	
}
