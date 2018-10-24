package ch.fhnw.projectbois.components.menubar;

import ch.fhnw.projectbois._mvc.Controller;

public class MenuBarController extends Controller<MenuBarModel, MenuBarView> {

	public MenuBarController(MenuBarModel model, MenuBarView view) {
		super(model, view);
	}

	public static MenuBarController initMVC() {
		MenuBarModel model = new MenuBarModel();
		MenuBarView view = new MenuBarView(model);

		return new MenuBarController(model, view);
	}

}
