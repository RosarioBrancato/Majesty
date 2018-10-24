package ch.fhnw.projectbois.login;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;
import ch.fhnw.projectbois.components.menubar.MenuBarController;
import ch.fhnw.projectbois.fxml.FXMLUtils;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class LoginView extends View<LoginModel> {

	protected LoginView(LoginModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("LoginView.fxml");
	}
	
	@Override
	protected Parent createRoot() {
		BorderPane root = new BorderPane();
		root.setLeft(MenuBarController.initMVC().getViewRoot());
		root.setCenter(FXMLUtils.loadFXML(this.getFXML()));
		
		return root;
	}

}
