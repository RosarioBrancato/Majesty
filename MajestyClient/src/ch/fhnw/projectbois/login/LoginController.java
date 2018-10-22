package ch.fhnw.projectbois.login;

import ch.fhnw.projectbois.mvc.Controller;

public class LoginController extends Controller<LoginModel, LoginView> {

	public LoginController() {
	}
	
	public LoginController(LoginModel model, LoginView view) {
		super(model, view);
	}
	
	public static LoginController createMvc() {
		LoginModel m = new LoginModel();
		LoginView v = new LoginView(m);
		
		return new LoginController(m, v);
	}
	
}
