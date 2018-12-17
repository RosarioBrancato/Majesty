/**
 * Controls the login view
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.login;

import java.awt.Desktop;
import java.io.File;
import java.util.Locale;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.components.menubar.MenuBarController;
import ch.fhnw.projectbois.components.menubar.MenuBarModel;
import ch.fhnw.projectbois.components.menubar.MenuBarView;
import ch.fhnw.projectbois.dto.UserDTO;
import ch.fhnw.projectbois.preferences.UserPrefs;
import ch.fhnw.projectbois.registration.RegistrationController;
import ch.fhnw.projectbois.registration.RegistrationModel;
import ch.fhnw.projectbois.registration.RegistrationView;
import ch.fhnw.projectbois.time.Time;
import ch.fhnw.projectbois.validation.CredentialsValidator;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class LoginController extends Controller<LoginModel, LoginView> {
	private final String pref_user = UserPrefs.getInstance().get("USERNAME", "");
	private final String pref_server = UserPrefs.getInstance().get("SERVER", "localhost");
	private final String pref_port = UserPrefs.getInstance().get("SERVER_PORT", "8200");
	
	// Language PickList selection
	private final String ENGLISH = "English";
	private final String GERMAN = "Deutsch";
	private final String FRENCH = "Français";
	private final String ITALIAN = "Italiano";

	private Time timer = null;
	private ChangeListener<Number> timerPropertyListener = null;

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
	private Hyperlink link_Login_OpenSourceResources;

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

	@FXML
	private VBox vbox_Login_form;

	@FXML
	private VBox vbox_Login_loading;

	private ChangeListener<UserDTO> userPropertyListener = null;
	private ChangeListener<String> loginStatusPropertyListener = null;

	/**
	 * Instantiates a new login controller.
	 *
	 * @param model the model
	 * @param view the view
	 */
	public LoginController(LoginModel model, LoginView view) {
		super(model, view);
	}

	/**
	 * When the "Login" button is clicked:
	 * If inputs are plausible/valid, the credentials are to be processed by the model. Else the error message is shown.
	 *
	 * @param event the click event
	 */
	@FXML
	private void btn_Login_loginClicked(ActionEvent event) {
		if (checkServerPortValidity()) {
			processCredentials();
		} else {
			Platform.runLater(() -> {
				
				this.lbl_Login_loginMsg.setText(translator.getTranslation("lbl_Login_loginMsg_BadInputBoth"));
			});
		}
	}
	
	/* START: DELETE AFTER DEVELOPMENT PHASE */
	@FXML
	private void btn_Login_logInAlexClicked(ActionEvent event) {
		model.LoginProcessCredentials(txt_Login_serverServer.getText(), txt_Login_serverPort.getText(), "alex",
				"ABCDEFGH12345678");
	}

	/**
	 * Btn login log in dario clicked.
	 *
	 * @param event the event
	 */
	@FXML
	private void btn_Login_logInDarioClicked(ActionEvent event) {
		model.LoginProcessCredentials(txt_Login_serverServer.getText(), txt_Login_serverPort.getText(), "dario",
				"ABCDEFGH12345678");
	}

	/**
	 * Btn login log in lee clicked.
	 *
	 * @param event the event
	 */
	@FXML
	private void btn_Login_logInLeeClicked(ActionEvent event) {
		model.LoginProcessCredentials(txt_Login_serverServer.getText(), txt_Login_serverPort.getText(), "lee",
				"ABCDEFGH12345678");
	}

	/**
	 * Btn login log in rosario clicked.
	 *
	 * @param event the event
	 */
	@FXML
	private void btn_Login_logInRosarioClicked(ActionEvent event) {
		model.LoginProcessCredentials(txt_Login_serverServer.getText(), txt_Login_serverPort.getText(), "rosario",
				"ABCDEFGH12345678");
	}
	/* END: DELETE AFTER DEVELOPMENT PHASE */

	/**
	 * Asks the model to handle the login request once the login button is clicked.
	 *
	 * @param event the click event
	 */
	@FXML
	private void btn_Login_registerClicked(ActionEvent event) {
		if (checkServerPortValidity()) {
			int port = Integer.parseInt(txt_Login_serverPort.getText());

			RegistrationController controller = Controller.initMVC(RegistrationController.class,
					RegistrationModel.class, RegistrationView.class);
			controller.setServerParam(txt_Login_serverServer.getText(), port);
			controller.showAndWait();
				this.txt_Login_username.setText(controller.getUsername());
				this.txt_Login_password.setText(controller.getPassword());
				this.txt_Login_username.requestFocus();
			MetaContainer.getInstance().destroyController(controller);
			if (!this.txt_Login_username.getText().equals("") && !this.txt_Login_password.getText().equals("")) {
				processCredentials();
			}
		} else {
			Platform.runLater(() -> {

				this.lbl_Login_loginMsg.setText(translator.getTranslation("lbl_Login_loginMsg_BadInputBoth"));
			});
		}

	}
	
	/**
	 * Checks whether the entered port is valid or not.
	 *
	 * @return true, if the port entered is an int
	 */
	private boolean checkServerPortValidity() {
		boolean response = CredentialsValidator.getInstance().stringIsValidServerAddress(this.txt_Login_serverServer.getText());
		try {
			Integer.parseInt(this.txt_Login_serverPort.getText());
		} catch (Exception e) {
			response = false;
		}
		return response;
	}
	
	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Controller#destroy()
	 */
	/**
	 * Removes property listeners before closing the MVC.
	 */
	@Override
	public void destroy() {
		try {
			this.timer.getPeriodCounterProperty().removeListener(this.timerPropertyListener);
			this.timer.stop();
		} catch (Exception e) {}

		super.destroy();

		model.getLoggedInUser().removeListener(userPropertyListener);
		model.getLoginStatus().removeListener(loginStatusPropertyListener);
	}

	/**
	 * Fills the language options in the dropdown menu.
	 */
	private void fillChoiceBox() {
		this.cmb_Login_language.getItems().add(ENGLISH);
		this.cmb_Login_language.getItems().add(GERMAN);
		this.cmb_Login_language.getItems().add(FRENCH);
		this.cmb_Login_language.getItems().add(ITALIAN);
		
		String lang = UserPrefs.getInstance().get("LANG", "en");
		switch(lang) {
			case("de"):
				this.cmb_Login_language.getSelectionModel().select(GERMAN);
				break;
			case("fr"):
				this.cmb_Login_language.getSelectionModel().select(FRENCH);
				break;
			case("it"):
				this.cmb_Login_language.getSelectionModel().select(ITALIAN);
				break;
			default:
				this.cmb_Login_language.getSelectionModel().select(ENGLISH);
		}		
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Controller#initialize()
	 */
	/**
	 * Creates change listeners for fields in the form and the login status coming from model,
	 * initializes the user properties, asks to fill out the menu and pre-fills fields according
	 * to the properties set or the defaults.
	 */
	@Override
	protected void initialize() {
		super.initialize();

		Platform.runLater(() -> {
			if(!(this.pref_user.equals("") || this.pref_user == null))
				this.txt_Login_username.setText(pref_user);
				this.txt_Login_password.requestFocus();
			this.txt_Login_serverServer.setText(pref_server);
			this.txt_Login_serverPort.setText(pref_port);
		});
		
		this.initUserPropertyListener();
		model.getLoggedInUser().addListener(userPropertyListener);

		this.initLoginStatusPropertyListener();
		model.getLoginStatus().addListener(loginStatusPropertyListener);

		model.getLoginStatus().addListener((observer, oldValue, newValue) -> {
			if (newValue != null) {
				Platform.runLater(() -> {
					lbl_Login_loginMsg.setText(translator.getTranslation(newValue.toString()));
				});
			}
		});

		this.fillChoiceBox();
		
		cmb_Login_language.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue, oldValue, newValue) -> {
					Locale locale = null;
					
					if (newValue.equals(GERMAN)) {
						locale = new Locale("de");
						UserPrefs.getInstance().put("LANG", "de");
					} else if (newValue.equals(FRENCH)) {
						locale = new Locale("fr");
						UserPrefs.getInstance().put("LANG", "fr");
					} else if (newValue.equals(ITALIAN)) {
						locale = new Locale("it");
						UserPrefs.getInstance().put("LANG", "it");
					} else {
						locale = new Locale("de");
						UserPrefs.getInstance().put("LANG", "en");
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

	/**
	 * Initializes the login status property listener and allows to react on new login status messages.
	 */
	private void initLoginStatusPropertyListener() {
		this.loginStatusPropertyListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try{
					timer.getPeriodCounterProperty().removeListener(timerPropertyListener);
					timer.stop();
				}catch(NullPointerException e) {}
				
				if (newValue != null) {
					Platform.runLater(() -> {
						lbl_Login_loginMsg.setText(translator.getTranslation(newValue.toString()));
					});
				}
				switchLoaderDisplay(false);
			}
		};
	}

	/**
	 * Initializes the timeout timer property listener.
	 */
	private void initTimerPropertyListener() {
		this.timerPropertyListener = (observer, oldValue, newValue) -> {
			Platform.runLater(() -> {
				switchLoaderDisplay(false);
				
				this.lbl_Login_loginMsg.setText(translator.getTranslation("lbl_Login_loginMsg_ServerNoReaction"));
			});
		};
	}

	/**
	 * Initializes the user property listener.
	 * Once a UserDTO is set, the login is completed and the MenuBarController can be shown.
	 */
	private void initUserPropertyListener() {
		this.userPropertyListener = new ChangeListener<UserDTO>() {

			@Override
			public void changed(ObservableValue<? extends UserDTO> observable, UserDTO oldValue, UserDTO newValue) {
				UserPrefs.getInstance().put("USERNAME", txt_Login_username.getText());
				UserPrefs.getInstance().put("SERVER", txt_Login_serverServer.getText());
				UserPrefs.getInstance().put("SERVER_PORT", txt_Login_serverPort.getText());
				UserPrefs.getInstance().put("LANG", translator.getLocale().getLanguage());

				destroy();
				
				Platform.runLater(() -> {
					Controller.initMVCAsRoot(MenuBarController.class, MenuBarModel.class, MenuBarView.class);
				});
			}
		};
	}

	/**
	 * Opens a web page in the default browser where the used Open Source packages are declared.
	 *
	 * @param event the click event
	 */
	@FXML
	private void link_Login_OpenSourceResourcesClicked(ActionEvent event) {
		String tmp_path = System.getProperty("user.dir") + "/resources/manuals/opensource.html";
		
		if(tmp_path.indexOf("\\") > -1)
			tmp_path = tmp_path.replace("/", "\\");
		
		String path = tmp_path;
		File file = new File(path);
		try {
			Desktop.getDesktop().browse(file.toURI());
		} catch (Exception e) {
			Platform.runLater(() -> {
				this.lbl_Login_loginMsg.setText(translator.getTranslation("lbl_Login_loginMsg_OpenSourceDeclNotFound") + " " + path);
			});
			logger.warning("Unable to open the open source declaration: " + e.getMessage());
		}
	}

	/**
	 * Checks if both fields in the form are filled out and decides whether to enable the "Login" button or not.
	 */
	private void LoginCredentialsChanged() {
		try {
			if (this.txt_Login_username.getText().equals("") || this.txt_Login_password.getText().equals("")) {
				Platform.runLater(() -> {
					this.btn_Login_login.setDisable(true);
				});
			} else {
				Platform.runLater(() -> {
					this.btn_Login_login.setDisable(false);
				});
			}
		} catch (Exception e) {
		}

	}

	/**
	 * Changes the language on the current view.
	 */
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
			this.link_Login_OpenSourceResources.setText(translator.getTranslation("link_Login_OpenSourceResources"));
		});
	}

	/**
	 * Sets the message in the status bar.
	 *
	 * @param reference the login status message (out of language resources)
	 */
	public void LoginSetMessage(String reference) {
		Platform.runLater(() -> {
			this.lbl_Login_loginMsg.setText(translator.getTranslation(reference));
		});
	}

	/**
	 * Asks the model to process the login request.
	 */
	private void processCredentials() {
		model.resetStatus();
		startTimer(30);
		model.LoginProcessCredentials(txt_Login_serverServer.getText(), txt_Login_serverPort.getText(),
				txt_Login_username.getText(), txt_Login_password.getText());
	}

	/**
	 * Starts a timeout timer while waiting for the server response.
	 *
	 * @param seconds the number of seconds to wait before the form is shown again
	 */
	private void startTimer(int seconds) {
		switchLoaderDisplay(true);
		this.timer = new Time();
		this.timer.startTimer(seconds * 1000);

		this.initTimerPropertyListener();
		this.timer.getPeriodCounterProperty().addListener(this.timerPropertyListener);
	}

	/**
	 * Switch between form and loading screen.
	 *
	 * @param loading is true if the loading screen has to be shown, false if the form has to be shown
	 */
	private void switchLoaderDisplay(boolean loading) {
		Platform.runLater(() -> {
			this.vbox_Login_loading.setVisible(loading);
			this.vbox_Login_form.setVisible(!loading);
		});
	}

}
