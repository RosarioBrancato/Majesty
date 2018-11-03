package ch.fhnw.projectbois.registration;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;

public class RegistrationView extends View<RegistrationModel> {

	public RegistrationView(RegistrationModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("RegistrationView.fxml");
	}

}
