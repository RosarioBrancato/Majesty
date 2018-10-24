package ch.fhnw.projectbois.components.menubar;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;

public class MenuBarView extends View<MenuBarModel> {

	protected MenuBarView(MenuBarModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("MenuBarView.fxml");
	}

}
