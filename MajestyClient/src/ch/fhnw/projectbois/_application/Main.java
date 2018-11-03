package ch.fhnw.projectbois._application;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.login.LoginController;
import ch.fhnw.projectbois.login.LoginModel;
import ch.fhnw.projectbois.login.LoginView;
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

			LoginController login = Controller.initMVC(LoginController.class, LoginModel.class, LoginView.class);
			MetaContainer.getInstance().setRoot(login.getViewRoot());

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
