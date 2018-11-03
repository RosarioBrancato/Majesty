package ch.fhnw.projectbois.profile;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;

public class ProfileView extends View<ProfileModel> {

	public ProfileView(ProfileModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("ProfileView.fxml");
	}

}
