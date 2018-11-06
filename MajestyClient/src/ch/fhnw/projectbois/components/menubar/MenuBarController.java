package ch.fhnw.projectbois.components.menubar;

import ch.fhnw.projectbois._mvc.Controller;
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

	@FXML
	private void btnText_Click(ActionEvent event) {
		Network.getInstance().sendTest();
	}

	@FXML
	private void btn_menubarview_play_view(ActionEvent event) {
		PlayScreenController controller = Controller.initMVC(PlayScreenController.class, PlayScreenModel.class,
				PlayScreenView.class);

		Parent root = this.view.getRoot();
		BorderPane pane = (BorderPane) root;
		pane.setCenter(controller.getViewRoot());
	}

}
