package ch.fhnw.projectbois.components.menubar;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.game.GameController;
import ch.fhnw.projectbois.game.GameModel;
import ch.fhnw.projectbois.game.GameView;
import ch.fhnw.projectbois.leaderboard.LeaderboardController;
import ch.fhnw.projectbois.leaderboard.LeaderboardModel;
import ch.fhnw.projectbois.leaderboard.LeaderboardView;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.playscreen.PlayScreenController;
import ch.fhnw.projectbois.playscreen.PlayScreenModel;
import ch.fhnw.projectbois.playscreen.PlayScreenView;
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

		System.gc();
	}

	@FXML
	private void btnText_Click(ActionEvent event) {
		Network.getInstance().sendTest();
	}

	// Get to Lobby Screen where the player can either create a lobby or join one.
	@FXML
	private void btn_menubarview_play_view(ActionEvent event) {
		PlayScreenController controller = Controller.initMVC(PlayScreenController.class, PlayScreenModel.class,
				PlayScreenView.class);

		this.switchCenter(controller.getViewRoot());
	}
	
	// Get to the Leaderboard Screen 
	@FXML
	private void btn_menubarview_leaderboard_view(ActionEvent event) {
		LeaderboardController controller = Controller.initMVC(LeaderboardController.class, LeaderboardModel.class, LeaderboardView.class);
		this.switchCenter(controller.getViewRoot());
	}

	@FXML
	private void btnGame_Click(ActionEvent event) {
		GameController controller = Controller.initMVC(GameController.class, GameModel.class, GameView.class);
		MetaContainer.getInstance().setRoot(controller.getViewRoot());
	}

}
