/*
 * 
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.profile;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.session.Session;
import ch.fhnw.projectbois.time.Time;
import ch.fhnw.projectbois.validation.CredentialsValidator;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class ProfileController extends Controller<ProfileModel, ProfileView> {
	private Time timer = null;
	private ChangeListener<Number> timerPropertyListener = null;
	
	private ChangeListener<String> profUpdateStat = null;
	
	@FXML
	private Label lbl_Profile_msg;
	
	@FXML
	private Button btn_Profile_update;
	
	@FXML
	private TextField txt_Profile_username;
	
	@FXML
	private TextField txt_Profile_email;
	
	@FXML
	private PasswordField txt_Profile_pwd;
	
	@FXML
	private PasswordField txt_Profile_pwdRepeat;

	@FXML
	private Hyperlink hyp_Profile_Helper_username;
	
	@FXML
	private Hyperlink hyp_Profile_Helper_email;
	
	@FXML
	private Hyperlink hyp_Profile_Helper_password;
	
	@FXML
	private Hyperlink hyp_Profile_Helper_passwordRepeat;
	
	@FXML
	private VBox vbox_Profile_loader;
	
	@FXML
	private VBox vbox_Profile_form;
	
	/**
	 * Instantiates a new profile controller.
	 *
	 * @param model the model
	 * @param view the view
	 */
	public ProfileController(ProfileModel model, ProfileView view) {
		super(model, view);
	}
	
	/**
	 * Btn profile update clicked.
	 *
	 * @param event the event
	 */
	@FXML
	private void btn_Profile_updateClicked(ActionEvent event) {
		model.resetStatus();
		startTimer(30);
		model.UpdateProfileProcessInput(this.txt_Profile_email.getText(), this.txt_Profile_pwd.getText());
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
		String email = this.txt_Profile_email.getText();
		String pwd = this.txt_Profile_pwd.getText();
		String pwd_repeat = this.txt_Profile_pwdRepeat.getText();
		
		boolean email_ok = (!Session.getCurrentEmail().equals(email) && cv.stringIsValidEmailAddress(email));
		boolean pwd_ok = cv.passwordStrenghtIsSufficient(pwd);
		boolean pwd_match = (pwd.length() > 0 && pwd.equals(pwd_repeat));
		
		changeFieldColor(this.txt_Profile_email, email_ok);
		changeFieldColor(this.txt_Profile_pwd, pwd_ok);
		changeFieldColor(this.txt_Profile_pwdRepeat, pwd_match);
				
		Platform.runLater(() -> {
			this.btn_Profile_update.setDisable(!(email_ok || (pwd_ok && pwd_match)));
		});
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

		model.getProfUpdateStatus().removeListener(profUpdateStat);
	}
	
	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Controller#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();
		
		this.initRegistrationStatusPropertyListener();
		model.getProfUpdateStatus().addListener(profUpdateStat);
		
		txt_Profile_email.textProperty().addListener((observable, oldValue, newValue) -> {
			checkInputValidity();
		});

		txt_Profile_pwd.textProperty().addListener((observable, oldValue, newValue) -> {
			checkInputValidity();
		});

		txt_Profile_pwdRepeat.textProperty().addListener((observable, oldValue, newValue) -> {
			checkInputValidity();
		});
		
		Platform.runLater(() -> {
			txt_Profile_username.setText(Session.getCurrentUsername());
			txt_Profile_email.setText(Session.getCurrentEmail());
		});
		
	}
	
	/**
	 * Inits the registration status property listener.
	 */
	private void initRegistrationStatusPropertyListener() {
		this.profUpdateStat = new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.equals("OK")) {
					Platform.runLater(() -> {
						lbl_Profile_msg.setTextFill(Paint.valueOf("GREEN"));
						lbl_Profile_msg.setText(translator.getTranslation("lbl_Profile_Response_Success"));
					});
					
				}else {
					Platform.runLater(() -> {
						lbl_Profile_msg.setTextFill(Paint.valueOf("RED"));
						lbl_Profile_msg.setText(translator.getTranslation(newValue.toString()));
					});
				}
				timer.getPeriodCounterProperty().removeListener(timerPropertyListener);
				timer.stop();
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
				Platform.runLater(() -> {
					lbl_Profile_msg.setTextFill(Paint.valueOf("RED"));
					this.lbl_Profile_msg.setText(translator.getTranslation("lbl_Login_loginMsg_ServerNoReaction"));
				});
			});
		};
	}
	
	/**
	 * Profile show helper text email.
	 *
	 * @param event the event
	 */
	@FXML
	private void ProfileShowHelperText_email(ActionEvent event) {
		Platform.runLater(() -> {
			lbl_Profile_msg.setTextFill(Paint.valueOf("BLACK"));
			this.lbl_Profile_msg.setText(translator.getTranslation("lbl_Profile_Helper_email"));
		});
	}
	
	/**
	 * Profile show helper text password.
	 *
	 * @param event the event
	 */
	@FXML
	private void ProfileShowHelperText_password(ActionEvent event) {
		Platform.runLater(() -> {
			lbl_Profile_msg.setTextFill(Paint.valueOf("BLACK"));
			this.lbl_Profile_msg.setText(translator.getTranslation("lbl_Profile_Helper_password"));
		});
	}
	
	/**
	 * Profile show helper text password repeat.
	 *
	 * @param event the event
	 */
	@FXML
	private void ProfileShowHelperText_passwordRepeat(ActionEvent event) {
		Platform.runLater(() -> {
			lbl_Profile_msg.setTextFill(Paint.valueOf("BLACK"));
			this.lbl_Profile_msg.setText(translator.getTranslation("lbl_Profile_Helper_passwordRepeat"));
		});
	}
	
	/**
	 * Profile show helper text username.
	 *
	 * @param event the event
	 */
	@FXML
	private void ProfileShowHelperText_username(ActionEvent event) {
		Platform.runLater(() -> {
			lbl_Profile_msg.setTextFill(Paint.valueOf("BLACK"));
			this.lbl_Profile_msg.setText(translator.getTranslation("lbl_Profile_Helper_username"));
		});
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
			this.vbox_Profile_loader.setVisible(loading);
			this.vbox_Profile_form.setVisible(!loading);
		});
	}
}
