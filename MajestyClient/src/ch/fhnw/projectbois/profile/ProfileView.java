package ch.fhnw.projectbois.profile;

import java.net.URL;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.View;

public class ProfileView extends View<ProfileModel> {

	public ProfileView(ProfileModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("ProfileView.fxml");
	}

	@Override
	public <T extends Controller<ProfileModel, ? extends View<ProfileModel>>> void loadRoot(T controller) {
		super.loadRoot(controller);
		
		String css = ClassLoader.getSystemClassLoader().getResource("stylesheets/Login_Registration_Profile.css").toExternalForm();
		this.root.getStylesheets().add(css);
	}
	
}
