package ch.fhnw.projectbois.login;

import java.net.URL;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.View;

/**
 * 
 * @author Alexandre Miccoli
 *
 */

public class LoginView extends View<LoginModel> {

	public LoginView(LoginModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("LoginView.fxml");
	}
	
	@Override
		public <T extends Controller<LoginModel, ? extends View<LoginModel>>> void loadRoot(T controller) {
			super.loadRoot(controller);
			
			String css = ClassLoader.getSystemClassLoader().getResource("stylesheets/Login_Registration_Profile.css").toExternalForm();
			this.root.getStylesheets().add(css);
		}

}
