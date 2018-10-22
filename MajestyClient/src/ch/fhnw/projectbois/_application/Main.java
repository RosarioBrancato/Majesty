package ch.fhnw.projectbois._application;
	
import ch.fhnw.projectbois.login.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			MetaContainer.getInstance().setMainStage(primaryStage);
			
			LoginController.createMvc();
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
