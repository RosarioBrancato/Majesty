package ch.fhnw.projectbois.components.menubar;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.leaderboard.LeaderboardController;
import ch.fhnw.projectbois.leaderboard.LeaderboardModel;
import ch.fhnw.projectbois.leaderboard.LeaderboardView;
import ch.fhnw.projectbois.playscreen.PlayScreenController;
import ch.fhnw.projectbois.playscreen.PlayScreenModel;
import ch.fhnw.projectbois.playscreen.PlayScreenView;
import ch.fhnw.projectbois.profile.ProfileController;
import ch.fhnw.projectbois.profile.ProfileModel;
import ch.fhnw.projectbois.profile.ProfileView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class MenuBarController extends Controller<MenuBarModel, MenuBarView> {

	public MenuBarController(MenuBarModel model, MenuBarView view) {
		super(model, view);
	}

	private void switchCenter(Parent pane) {
		Parent root = this.getViewRoot();
		BorderPane borderPane = (BorderPane) root;
		borderPane.setCenter(pane);
	}

	/**
	 * Get to Lobby Screen where the player can either create a lobby or join one.
	 * 
	 * @param event
	 */
	@FXML
	private void btnPlay_Click(ActionEvent event) {
		PlayScreenController controller = Controller.initMVC(PlayScreenController.class, PlayScreenModel.class,
				PlayScreenView.class);

		this.switchCenter(controller.getViewRoot());
	}

	@FXML
	private void btnProfile_Click(ActionEvent event) {
		ProfileController controller = Controller.initMVC(ProfileController.class, ProfileModel.class,
				ProfileView.class);

		this.switchCenter(controller.getViewRoot());
	}

	/**
	 * Get to the Leaderboard Screen
	 * 
	 * @param event
	 */
	@FXML
	private void btnLeaderboard_Click(ActionEvent event) {
		LeaderboardController controller = Controller.initMVC(LeaderboardController.class, LeaderboardModel.class,
				LeaderboardView.class);
		
		this.switchCenter(controller.getViewRoot());
	}
	
	@FXML
	private void btnLogout_Click(ActionEvent event) {
		
	}

}
