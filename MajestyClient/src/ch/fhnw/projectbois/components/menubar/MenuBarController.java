package ch.fhnw.projectbois.components.menubar;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois._mvc.View;
import ch.fhnw.projectbois.leaderboard.LeaderboardController;
import ch.fhnw.projectbois.leaderboard.LeaderboardModel;
import ch.fhnw.projectbois.leaderboard.LeaderboardView;
import ch.fhnw.projectbois.login.LoginController;
import ch.fhnw.projectbois.login.LoginModel;
import ch.fhnw.projectbois.login.LoginView;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.playscreen.PlayScreenController;
import ch.fhnw.projectbois.playscreen.PlayScreenModel;
import ch.fhnw.projectbois.playscreen.PlayScreenView;
import ch.fhnw.projectbois.profile.ProfileController;
import ch.fhnw.projectbois.profile.ProfileModel;
import ch.fhnw.projectbois.profile.ProfileView;
import ch.fhnw.projectbois.session.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MenuBarController extends Controller<MenuBarModel, MenuBarView> {

	private Controller<? extends Model, ? extends View<? extends Model>> controller;

	@FXML
	BorderPane pnlMenu;

	@FXML
	private Label lblGreetUser;

	public MenuBarController(MenuBarModel model, MenuBarView view) {
		super(model, view);
	}

	@Override
	protected void initialize() {
		super.initialize();

		PlayScreenController playScreenController = Controller.initMVC(PlayScreenController.class,
				PlayScreenModel.class, PlayScreenView.class);

		switchCenter(playScreenController);
		
		lblGreetUser.setText(lblGreetUser.getText() + " " + Session.getCurrentUsername() + "!");
	}

	private void switchCenter(Controller<? extends Model, ? extends View<? extends Model>> controller) {
		// destroy old MVC
		if (this.controller != null) {
			MetaContainer.getInstance().destroyController(this.controller);
		}
		this.controller = controller;

		pnlMenu.setCenter(controller.getViewRoot());
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

		this.switchCenter(controller);
	}

	@FXML
	private void btnProfile_Click(ActionEvent event) {
		ProfileController controller = Controller.initMVC(ProfileController.class, ProfileModel.class,
				ProfileView.class);

		this.switchCenter(controller);
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

		this.switchCenter(controller);
	}

	@FXML
	private void btnLogout_Click(ActionEvent event) {
		Network.getInstance().stopConnection();

		Controller.initMVCAsRoot(LoginController.class, LoginModel.class, LoginView.class);
	}

	// TEMP DELTE AFTERWARDS
	@FXML
	private void btnLogin_Click(ActionEvent event) {
		LoginController controller = Controller.initMVC(LoginController.class, LoginModel.class, LoginView.class);

		this.switchCenter(controller);
	}

}
