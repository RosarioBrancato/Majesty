package ch.fhnw.projectbois.components.menubar;

import java.net.URL;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.View;
import ch.fhnw.projectbois.playscreen.PlayScreenController;
import ch.fhnw.projectbois.playscreen.PlayScreenModel;
import ch.fhnw.projectbois.playscreen.PlayScreenView;
import javafx.scene.layout.BorderPane;

public class MenuBarView extends View<MenuBarModel> {

	public MenuBarView(MenuBarModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("MenuBarView.fxml");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <T extends Controller> void loadRoot(T controller) {
		super.loadRoot(controller);

		PlayScreenController playScreenController = Controller.initMVC(PlayScreenController.class,
				PlayScreenModel.class, PlayScreenView.class);

		BorderPane pane = (BorderPane) this.root;
		pane.setCenter(playScreenController.getViewRoot());
	}

}
