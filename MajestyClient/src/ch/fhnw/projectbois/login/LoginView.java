package ch.fhnw.projectbois.login;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;

public class LoginView extends View<LoginModel> {

	public LoginView(LoginModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("LoginView.fxml");
	}

}
