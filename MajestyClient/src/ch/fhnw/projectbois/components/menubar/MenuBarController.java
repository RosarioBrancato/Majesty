package ch.fhnw.projectbois.components.menubar;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.network.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MenuBarController extends Controller<MenuBarModel, MenuBarView> {

	public MenuBarController() {

	}

	public MenuBarController(MenuBarModel model, MenuBarView view) {
		super(model, view);
	}

	public static MenuBarController initMVC() {
		MenuBarModel model = new MenuBarModel();
		MenuBarView view = new MenuBarView(model);

		return new MenuBarController(model, view);
	}

	@FXML
	private void btnText_Click(ActionEvent event) {
		Network.getInstance().sendTest();
	}

}
