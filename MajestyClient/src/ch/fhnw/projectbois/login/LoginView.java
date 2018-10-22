package ch.fhnw.projectbois.login;

import java.net.URL;

import ch.fhnw.projectbois.mvc.View;

public class LoginView extends View<LoginModel> {

	protected LoginView(LoginModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("LoginView.fxml");
	}

	@Override
	protected String getCSS() {
		return null;
	}

}
