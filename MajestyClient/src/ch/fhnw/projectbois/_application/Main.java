package ch.fhnw.projectbois._application;
	
import ch.fhnw.projectbois.login.LoginController;
import ch.fhnw.projectbois.network.Network;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			MetaContainer.getInstance().setMainStage(primaryStage);
			MetaContainer.getInstance().setRoot(LoginController.initMVC().getViewRoot());
			
			primaryStage.setTitle("Majesty - For The Realm");
			primaryStage.setMaximized(false);
			primaryStage.show();
			
			Network.getInstance().initConnection("localhost", 8200);
			
		} catch(Exception e) {
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
