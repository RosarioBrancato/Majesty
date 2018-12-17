/*
 * Controls the Leaderboard View
 */
package ch.fhnw.projectbois.leaderboard;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.dto.LeaderboardDTO;
import ch.fhnw.projectbois.dto.LeaderboardPlayerDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * The Class LeaderboardController
 * @author Dario Stoeckli
 */
public class LeaderboardController extends Controller<LeaderboardModel, LeaderboardView> {

	@FXML
	private ListView<LeaderboardPlayerDTO> lstPlayers;

	@FXML
	private Label lblCurrentPlayer_Dynamic;
	
	/**
	 * Instantiates a new leaderboard controller.
	 *
	 * @param model the model
	 * @param view the view
	 */
	public LeaderboardController(LeaderboardModel model, LeaderboardView view) {
		super(model, view);
	}
	
	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Controller#initialize()
	 * Additionally receive the Leaderboard via a Property object and prepare a ranking statement for the 
	 * individual player
	 */
	@Override
	protected void initialize() {
		super.initialize();
		
		model.getLeaderboardProperty().addListener((observer, oldValue, newValue) -> {
			fillListView(newValue);
		});
		
		model.getLeaderboardPlayerProperty().addListener((observer, oldValue, newValue) -> {
			showPlayerInformation(newValue);
		});
		
		getLeaderboard();
		
	}
	
	/**
	 * Gets the leaderboard.
	 *
	 * @return the leaderboard
	 */
	private void getLeaderboard() {
		model.queryLeaderboard();
	}
	
	
	/**
	 * Fill list view that will be shown to the player (the actual Leaderboard as LeaderboardDTO object)
	 *
	 * @param leaderboard the leaderboard
	 */
	private void fillListView(LeaderboardDTO leaderboard) {
		Platform.runLater(() -> {
			lstPlayers.getItems().clear();
			lstPlayers.getItems().addAll(leaderboard.getLeaderboard());
		});
	}
	
	/**
	 * Show player information (this is the Text Label that informs the user at which rank with how many points he/she is at)
	 *
	 * @param currentplayer the currentplayer
	 */
	private void showPlayerInformation(LeaderboardPlayerDTO currentplayer) {
		Platform.runLater(() -> {
			lblCurrentPlayer_Dynamic.setText(translator.getTranslation("lbl_Leaderboard_CurrentPlayer") + " " + currentplayer.getUsername() 
			+ " " + translator.getTranslation("lbl_Leaderboard_CurrentPlayer_Add1") + " " + currentplayer.getPoints() + " " 
			+ translator.getTranslation("lbl_Leaderboard_CurrentPlayer_Add2") + " " + currentplayer.getRank());
		});
	}
	

}
