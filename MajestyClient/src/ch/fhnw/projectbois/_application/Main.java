package ch.fhnw.projectbois._application;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.log.LoggerFactory;
import ch.fhnw.projectbois.login.LoginController;
import ch.fhnw.projectbois.login.LoginModel;
import ch.fhnw.projectbois.login.LoginView;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.preferences.UserPrefs;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * The Class Main.
 *
 * @author Rosario Brancato
 */
public class Main extends Application {

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		Logger logger = LoggerFactory.getLogger(Main.class);
		
		try {
			primaryStage.setTitle("Majesty - For The Realm");
			primaryStage.setMaximized(false);
			primaryStage.setWidth(1250);
			primaryStage.setHeight(850);
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/majesty.png")));
			
			Scene scene = new Scene(new Label("Loading..."));
			String bootstrap = this.getClass().getResource("bootstrap2.css").toExternalForm();
			String mainCss = this.getClass().getResource("Main.css").toExternalForm();
			scene.getStylesheets().add(bootstrap);
			scene.getStylesheets().add(mainCss);
			primaryStage.setScene(scene);
			
			MetaContainer.getInstance().setMainStage(primaryStage);
			Controller.initMVCAsRoot(LoginController.class, LoginModel.class, LoginView.class);
			
			primaryStage.show();

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Main (Client)", e);
		}
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Locale.setDefault(new Locale(UserPrefs.getInstance().get("LANG", "en")));
		launch(args);
	}
	
	/* (non-Javadoc)
	 * @see javafx.application.Application#stop()
	 */
	@Override
	public void stop() throws Exception {
		super.stop();
		
		//Release current MVC
		MetaContainer.getInstance().setRoot(new Label("Shutting down..."));
		MetaContainer.getInstance().destroyControllers();
		
		//End conneciton
		Network.getInstance().stopConnection();
	}
}
