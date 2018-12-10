/*
 * 
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.registration;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.interfaces.IDialog;
import ch.fhnw.projectbois.time.Time;
import ch.fhnw.projectbois.utils.DialogUtils;
import ch.fhnw.projectbois.validation.CredentialsValidator;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class RegistrationController extends Controller<RegistrationModel, RegistrationView> implements IDialog {
	private Stage stage;
	private ChangeListener<String> regStat = null;
	private String server = null;
	private int port = 0;
	private String final_username = null;
	private String final_password = null;
	
	private Time timer = null;
	private ChangeListener<Number> timerPropertyListener = null;
	
	/**
	 * Instantiates a new registration controller.
	 *
	 * @param model the model
	 * @param view the view
	 */
	public RegistrationController(RegistrationModel model, RegistrationView view) {
		super(model, view);
	}
	
	/**
	 * Inits the registration status property listener.
	 */
	private void initRegistrationStatusPropertyListener() {
		this.regStat = new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.equals("OK")) {
					final_username = txt_Registration_username.getText();
					final_password = txt_Registration_pwd.getText();
					Platform.runLater(() -> {
						stage.close();
					});
				}else {
					Platform.runLater(() -> {
						lbl_Registration_msg.setTextFill(Paint.valueOf("RED"));
						lbl_Registration_msg.setText(translator.getTranslation(newValue.toString()));
					});
				}
				switchLoaderDisplay(false);
			}
		};	
	}
	
	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Controller#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();
		
		this.initRegistrationStatusPropertyListener();
		model.getRegistrationStatus().addListener(regStat);
		
		txt_Registration_username.textProperty().addListener((observable, oldValue, newValue) -> {
			checkInputValidity();
		});

		txt_Registration_email.textProperty().addListener((observable, oldValue, newValue) -> {
			checkInputValidity();
		});

		txt_Registration_pwd.textProperty().addListener((observable, oldValue, newValue) -> {
			checkInputValidity();
		});

		txt_Registration_pwdRepeat.textProperty().addListener((observable, oldValue, newValue) -> {
			checkInputValidity();
		});
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois.interfaces.IDialog#showAndWait()
	 */
	public void showAndWait() {
		this.stage = DialogUtils.getStageModal(MetaContainer.getInstance().getMainStage());
		this.stage.setTitle(translator.getTranslation("lbl_Registration_Title"));
		this.stage.setScene(new Scene(this.getViewRoot()));
		this.stage.showAndWait();
		
	}
	
	/**
	 * Sets the server param.
	 *
	 * @param server the server
	 * @param port the port
	 */
	public void setServerParam(String server, int port) {
		this.server = server;
		this.port = port;
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

		model.getRegistrationStatus().removeListener(regStat);
	}
	
	@FXML
	private Label lbl_Registration_msg;
	
	@FXML
	private Button btn_Registration_register;
	
	@FXML
	private TextField txt_Registration_username;
	
	@FXML
	private TextField txt_Registration_email;
	
	@FXML
	private PasswordField txt_Registration_pwd;
	
	@FXML
	private PasswordField txt_Registration_pwdRepeat;
	
	@FXML
	private Hyperlink hyp_Registration_Helper_username;
	
	@FXML
	private Hyperlink hyp_Registration_Helper_email;
	
	@FXML
	private Hyperlink hyp_Registration_Helper_password;
	
	@FXML
	private Hyperlink hyp_Registration_Helper_passwordRepeat;
	
	@FXML
	private VBox vbox_Registration_loading;
	
	@FXML
	private VBox vbox_Registration_form;
	
	/**
	 * Btn registration cancel clicked.
	 *
	 * @param event the event
	 */
	@FXML
	private void btn_Registration_cancelClicked(ActionEvent event) {
		this.stage.close();
	}
	
	/**
	 * Btn registration register clicked.
	 *
	 * @param event the event
	 */
	@FXML
	private void btn_Registration_registerClicked(ActionEvent event) {
		model.resetStatus();
		startTimer(30);
		model.RegistrationProcessInput(
				this.server, this.port, this.txt_Registration_username.getText(), this.txt_Registration_pwd.getText(), this.txt_Registration_email.getText());
	}
	
	/**
	 * Inits the timer property listener.
	 */
	private void initTimerPropertyListener() {
		this.timerPropertyListener = (observer, oldValue, newValue) -> {
			Platform.runLater(() -> {
				switchLoaderDisplay(false);
				this.lbl_Registration_msg.setText(translator.getTranslation("lbl_Login_loginMsg_ServerNoReaction"));
			});
		};
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
			this.vbox_Registration_loading.setVisible(loading);
			this.vbox_Registration_form.setVisible(!loading);
		});
	}
	
	/**
	 * Change field color.
	 *
	 * @param f the f
	 * @param valid the valid
	 */
	private void changeFieldColor(TextField f, Boolean valid) {
		if(f.getText().toString().length() > 0 && valid) {
			Platform.runLater(() -> {
				f.getStyleClass().removeAll("field_error");
				f.getStyleClass().add("field_ok");
			});
		}else if(f.getText().toString().length() > 0) {
			Platform.runLater(() -> {
				f.getStyleClass().removeAll("field_ok");
				f.getStyleClass().add("field_error");
			});
		}
	}
	
	/**
	 * Check input validity.
	 */
	private void checkInputValidity() {
		CredentialsValidator cv = CredentialsValidator.getInstance();
		String username = this.txt_Registration_username.getText();
		String email = this.txt_Registration_email.getText();
		String pwd = this.txt_Registration_pwd.getText();
		String pwd_repeat = this.txt_Registration_pwdRepeat.getText();
		
		boolean username_ok = cv.stringIsAlphanumeric(username);
		boolean email_ok = cv.stringIsValidEmailAddress(email);
		boolean pwd_ok = cv.passwordStrenghtIsSufficient(pwd);
		boolean pwd_match = (pwd.length() > 0 && pwd.equals(pwd_repeat));
		
		changeFieldColor(this.txt_Registration_username, username_ok);
		changeFieldColor(this.txt_Registration_email, email_ok);
		changeFieldColor(this.txt_Registration_pwd, pwd_ok);
		changeFieldColor(this.txt_Registration_pwdRepeat, pwd_match);
		
		Platform.runLater(() -> {
			this.btn_Registration_register.setDisable(!(username_ok && email_ok && pwd_ok && pwd_match));
		});
	}
	
	/**
	 * Registration show helper text username.
	 *
	 * @param event the event
	 */
	@FXML
	private void RegistrationShowHelperText_username(ActionEvent event) {
		Platform.runLater(() -> {
			lbl_Registration_msg.setTextFill(Paint.valueOf("BLACK"));
			this.lbl_Registration_msg.setText(translator.getTranslation("lbl_Registration_Helper_username"));
		});
	}
	
	/**
	 * Registration show helper text email.
	 *
	 * @param event the event
	 */
	@FXML
	private void RegistrationShowHelperText_email(ActionEvent event) {
		Platform.runLater(() -> {
			lbl_Registration_msg.setTextFill(Paint.valueOf("BLACK"));
			this.lbl_Registration_msg.setText(translator.getTranslation("lbl_Registration_Helper_email"));
		});
	}
	
	/**
	 * Registration show helper text password.
	 *
	 * @param event the event
	 */
	@FXML
	private void RegistrationShowHelperText_password(ActionEvent event) {
		Platform.runLater(() -> {
			lbl_Registration_msg.setTextFill(Paint.valueOf("BLACK"));
			this.lbl_Registration_msg.setText(translator.getTranslation("lbl_Registration_Helper_password"));
		});
	}
	
	/**
	 * Registration show helper text password repeat.
	 *
	 * @param event the event
	 */
	@FXML
	private void RegistrationShowHelperText_passwordRepeat(ActionEvent event) {
		Platform.runLater(() -> {
			lbl_Registration_msg.setTextFill(Paint.valueOf("BLACK"));
			this.lbl_Registration_msg.setText(translator.getTranslation("lbl_Registration_Helper_passwordRepeat"));
		});
	}
	
	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return this.final_username;
	}
	
	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return this.final_password;
	}

}
