/*
 * 
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.login;

import java.awt.Desktop;
import java.io.File;
import java.util.Locale;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._application.UserPrefs;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.components.menubar.MenuBarController;
import ch.fhnw.projectbois.components.menubar.MenuBarModel;
import ch.fhnw.projectbois.components.menubar.MenuBarView;
import ch.fhnw.projectbois.dto.UserDTO;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

/**
 * 
 * @author Alexandre Miccoli
 *
 */

public class LoginController extends Controller<LoginModel, LoginView> {
	private final String pref_user = UserPrefs.getInstance().get("USERNAME", null);
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
	 * Btn login login clicked.
	 *
	 * @param event the event
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

	/**
	 * Btn login log in alex clicked.
	 *
	 * @param event the event
	 */
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
	 * Btn login register clicked.
	 *
	 * @param event the event
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
	 * Link login open source resources clicked.
	 *
	 * @param event the event
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
	 * Check server port validity.
	 *
	 * @return true, if successful
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
	 * Fill choice box.
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
	@Override
	protected void initialize() {
		super.initialize();

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
		
		Platform.runLater(() -> {
			if(this.pref_user != null)
				this.txt_Login_username.setText(pref_user);
				this.txt_Login_password.requestFocus();
			this.txt_Login_serverServer.setText(pref_server);
			this.txt_Login_serverPort.setText(pref_port);
		});
		
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
	 * Inits the login status property listener.
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
	 * Inits the timer property listener.
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
	 * Inits the user property listener.
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
	 * Login credentials changed.
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
	 * Login set language.
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
		});
	}

	/**
	 * Login set message.
	 *
	 * @param reference the reference
	 */
	protected void LoginSetMessage(String reference) {
		Platform.runLater(() -> {
			this.lbl_Login_loginMsg.setText(translator.getTranslation(reference));
		});
	}

	/**
	 * Process credentials.
	 */
	private void processCredentials() {
		model.resetStatus();
		startTimer(30);
		model.LoginProcessCredentials(txt_Login_serverServer.getText(), txt_Login_serverPort.getText(),
				txt_Login_username.getText(), txt_Login_password.getText());
	}

	/**
	 * Start timer.
	 *
	 * @param seconds the seconds
	 */
	private void startTimer(int seconds) {
		switchLoaderDisplay(true);
		this.timer = new Time();
		this.timer.startTimer(seconds * 1000);

		this.initTimerPropertyListener();
		this.timer.getPeriodCounterProperty().addListener(this.timerPropertyListener);
	}

	/**
	 * Switch loader display.
	 *
	 * @param loading the loading
	 */
	private void switchLoaderDisplay(boolean loading) {
		Platform.runLater(() -> {
			this.vbox_Login_loading.setVisible(loading);
			this.vbox_Login_form.setVisible(!loading);
		});
	}

}
