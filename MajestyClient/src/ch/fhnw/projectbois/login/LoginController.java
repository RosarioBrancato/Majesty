package ch.fhnw.projectbois.login;

import java.util.Locale;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.components.menubar.MenuBarController;
import ch.fhnw.projectbois.components.menubar.MenuBarModel;
import ch.fhnw.projectbois.components.menubar.MenuBarView;
import ch.fhnw.projectbois.registration.RegistrationController;
import ch.fhnw.projectbois.registration.RegistrationModel;
import ch.fhnw.projectbois.registration.RegistrationView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

/**
 * 
 * @author Alexandre Miccoli
 *
 */

public class LoginController extends Controller<LoginModel, LoginView> {
	
	//Language PickList selection
	private final String ENGLISH = "English";
	private final String GERMAN = "Deutsch";
	private final String FRENCH = "Français";
	private final String ITALIAN = "Italiano";
	
	@FXML
	private Label lbl_Login_username;
	
	@FXML
	private Label lbl_Login_password;
	
	@FXML
	private Label lbl_Login_language;
	
	@FXML
	private Label lbl_Login_loginMsg;
	
	@FXML
	private ChoiceBox<String> cmb_Login_language;

	@FXML
	private Button btn_Login_login;
	
	@FXML
	private Button btn_Login_register;
	
	@FXML
	private TextField txt_Login_username;

	@FXML
	private TextField txt_Login_serverServer;

	@FXML
	private TextField txt_Login_serverPort;

	@FXML
	private PasswordField txt_Login_password;
	
	@FXML
	private TitledPane acc_Login_serverInfo;
	
	@FXML
	private Label lbl_Login_serverInfo;

	public LoginController(LoginModel model, LoginView view) {
		super(model, view);
	}
	
	protected void LoginSetLanguage() {
		Platform.runLater(() -> {
			this.lbl_Login_username.setText(translator.getTranslation("lbl_Login_username"));
			this.lbl_Login_password.setText(translator.getTranslation("lbl_Login_password"));
			this.lbl_Login_language.setText(translator.getTranslation("lbl_Login_language"));
			this.btn_Login_login.setText(translator.getTranslation("btn_Login_login"));
			this.btn_Login_register.setText(translator.getTranslation("btn_Login_register"));
			this.acc_Login_serverInfo.setText(translator.getTranslation("acc_Login_serverInfo"));
			this.lbl_Login_serverInfo.setText(translator.getTranslation("lbl_Login_serverInfo"));
			this.lbl_Login_username.setText(translator.getTranslation("lbl_Login_username"));
		});
	}
	
	protected void LoginSetMessage(String reference) {
		Platform.runLater(() -> {
			this.lbl_Login_loginMsg.setText(translator.getTranslation(reference));
		});
	}
	
	@Override
	protected void initialize() {
		super.initialize();

		model.getLoggedInUser().addListener((observer, oldValue, newValue) -> {
			Platform.runLater(() -> {
				MenuBarController controller = initMVC(MenuBarController.class, MenuBarModel.class, MenuBarView.class);
				MetaContainer.getInstance().setRoot(controller.getViewRoot());
			});
		});
		
		model.getLoginStatus().addListener((observer, oldValue, newValue) -> {
			Platform.runLater(() -> {
				this.lbl_Login_loginMsg.setText(translator.getTranslation(newValue.toString()));
			});
		});

		this.fillChoiceBox();
		
		cmb_Login_language.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue) -> {
            Locale locale = new Locale("en");
            if (newValue.equals(GERMAN)) {
            	locale = new Locale("de");
            } else if (newValue.equals(FRENCH)) {
            	locale = new Locale("fr");
            } else if (newValue.equals(ITALIAN)) {
            	locale = new Locale("it");
            }
        	translator.setResourceBundle(locale);
        	LoginSetLanguage();
        });
		
		txt_Login_username.textProperty().addListener((observable, oldValue, newValue) -> {
			LoginCredentialsChanged();
		});
		
		txt_Login_password.textProperty().addListener((observable, oldValue, newValue) -> {
			LoginCredentialsChanged();
		});
		
	}
	
	@FXML
	private void btn_Login_loginClicked(ActionEvent event) {
		model.LoginProcessCredentials(txt_Login_serverServer.getText(), txt_Login_serverPort.getText(), txt_Login_username.getText(), txt_Login_password.getText());
	}
	
	@FXML
	private void btn_Login_registerClicked(ActionEvent event) {
		RegistrationController controller = Controller.initMVC(
				RegistrationController.class, RegistrationModel.class, RegistrationView.class);
		controller.showAndWait();
	}
	
	/* START: DELETE AFTER DEVELOPMENT PHASE */
	@FXML
	private void btn_Login_logInAlexClicked(ActionEvent event) {
		model.LoginProcessCredentials(txt_Login_serverServer.getText(), txt_Login_serverPort.getText(), "alex", "ABCDEFGH12345678");
	}
	
	@FXML
	private void btn_Login_logInDarioClicked(ActionEvent event) {
		model.LoginProcessCredentials(txt_Login_serverServer.getText(), txt_Login_serverPort.getText(), "dario", "ABCDEFGH12345678");
	}
	
	@FXML
	private void btn_Login_logInLeeClicked(ActionEvent event) {
		model.LoginProcessCredentials(txt_Login_serverServer.getText(), txt_Login_serverPort.getText(), "lee", "ABCDEFGH12345678");
	}
	
	@FXML
	private void btn_Login_logInRosarioClicked(ActionEvent event) {
		model.LoginProcessCredentials(txt_Login_serverServer.getText(), txt_Login_serverPort.getText(), "rosario", "ABCDEFGH12345678");
	}
	/* END: DELETE AFTER DEVELOPMENT PHASE */
	
	private void LoginCredentialsChanged() {
		if(txt_Login_username.getText().equals("") || txt_Login_password.getText().equals("")) {
			btn_Login_login.setDisable(true);
		}else {
			btn_Login_login.setDisable(false);
		}
	}
	
	private void fillChoiceBox() {
		this.cmb_Login_language.getItems().add(ENGLISH);
		this.cmb_Login_language.getItems().add(GERMAN);
		this.cmb_Login_language.getItems().add(FRENCH);
		this.cmb_Login_language.getItems().add(ITALIAN);

		this.cmb_Login_language.getSelectionModel().selectFirst();
	}

}
