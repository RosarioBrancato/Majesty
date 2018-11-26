package ch.fhnw.projectbois._application;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.fhnw.projectbois._mvc.Controller;
/*
import ch.fhnw.projectbois.components.menubar.MenuBarController;
import ch.fhnw.projectbois.components.menubar.MenuBarModel;
import ch.fhnw.projectbois.components.menubar.MenuBarView;
*/
import ch.fhnw.projectbois.log.LoggerFactory;
import ch.fhnw.projectbois.login.LoginController;
import ch.fhnw.projectbois.login.LoginModel;
import ch.fhnw.projectbois.login.LoginView;
import ch.fhnw.projectbois.network.Network;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 
 * @author Rosario Brancato
 *
 */
public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		Logger logger = LoggerFactory.getLogger(Main.class);
		
		try {
			//Network.getInstance().initConnection("localhost", 8200);

			primaryStage.setTitle("Majesty - For The Realm");
			primaryStage.setMaximized(false);
			//primaryStage.setFullScreen(true);
			primaryStage.setWidth(1200);
			primaryStage.setHeight(800);

			LoginController login = Controller.initMVC(LoginController.class, LoginModel.class, LoginView.class);
			//MenuBarController menu = Controller.initMVC(MenuBarController.class, MenuBarModel.class, MenuBarView.class);
			Scene scene = new Scene(login.getViewRoot());
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/majesty.png")));
			primaryStage.setScene(scene);
			
			MetaContainer.getInstance().setMainStage(primaryStage);
			
			primaryStage.show();

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Main (Client)", e);
		}
	}

	public static void main(String[] args) {
		Locale.setDefault(new Locale("en"));
		
		launch(args);
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
		
		//Release current MVC
		MetaContainer.getInstance().setRoot(new AnchorPane());
		
		//End conneciton
		Network.getInstance().stopConnection();
	}
}
