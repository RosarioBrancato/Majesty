package ch.fhnw.projectbois._application;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.components.menubar.MenuBarController;
import ch.fhnw.projectbois.components.menubar.MenuBarModel;
import ch.fhnw.projectbois.components.menubar.MenuBarView;
import ch.fhnw.projectbois.network.Network;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * 
 * @author Rosario Brancato
 *
 */
public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			MetaContainer.getInstance().setMainStage(primaryStage);

			//LoginController login = Controller.initMVC(LoginController.class, LoginModel.class, LoginView.class);
			MenuBarController menu = Controller.initMVC(MenuBarController.class, MenuBarModel.class, MenuBarView.class);
			MetaContainer.getInstance().setRoot(menu.getViewRoot());

			primaryStage.setTitle("Majesty - For The Realm");
			primaryStage.setMaximized(false);
			primaryStage.show();

			Network.getInstance().initConnection("localhost", 8200);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void stop() throws Exception {
		super.stop();

		Network.getInstance().stopConnection();
	}
}
