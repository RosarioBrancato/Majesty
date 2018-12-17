/**
 * Controls the profile view
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.profile;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.login.LoginController;
import ch.fhnw.projectbois.login.LoginModel;
import ch.fhnw.projectbois.login.LoginView;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.preferences.UserPrefs;
import ch.fhnw.projectbois.session.Session;
import ch.fhnw.projectbois.time.Time;
import ch.fhnw.projectbois.validation.CredentialsValidator;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
	private Button btn_Profile_Delete;
	
	@FXML
	private CheckBox lbl_Profile_Delete;
	
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
	 * Checks whether the deletion warning is checked before activating the "Delete profile" button.
	 * 
	 * @param event the change event
	 */
	@FXML
	private void lbl_Profile_DeleteChecked(ActionEvent event) {
		if(this.lbl_Profile_Delete.isSelected()) {
			Platform.runLater(() -> {
				this.btn_Profile_Delete.setDisable(false);
			});
		}else {
			Platform.runLater(() -> {
				this.btn_Profile_Delete.setDisable(true);
			});
		}
	}
	
	/**
	 * Asks the model to delete the profile.
	 * 
	 * @param event the change event
	 */
	@FXML
	private void btn_Profile_DeleteClicked(ActionEvent event) {
		model.resetStatus();
		startTimer(5);
		model.DeleteProfile();
	}
	
	/**
	 * Resets and starts a timeout timer before letting the model process the input after the "save" button gets clicked.
	 *
	 * @param event the click event
	 */
	@FXML
	private void btn_Profile_updateClicked(ActionEvent event) {
		model.resetStatus();
		startTimer(30);
		model.UpdateProfileProcessInput(this.txt_Profile_email.getText(), this.txt_Profile_pwd.getText());
	}
	
	/**
	 * Change color of a text field border to indicate validity of the input.
	 *
	 * @param f the field to alter
	 * @param valid true if field has to become green, false if it has to become red
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
	 * Check the validity of typed data against defined patterns to decide whether the "save" button shall be enabled.
	 */
	private void checkInputValidity() {
		CredentialsValidator cv = CredentialsValidator.getInstance();
		String email = this.txt_Profile_email.getText();
		String pwd = this.txt_Profile_pwd.getText();
		String pwd_repeat = this.txt_Profile_pwdRepeat.getText();
		
		boolean email_ok = (!Session.getCurrentEmail().equals(email) && cv.stringIsValidEmailAddress(email));
		boolean pwd_ok = cv.passwordStrengthIsSufficient(pwd);
		boolean pwd_match = (pwd.length() > 0 && pwd.equals(pwd_repeat));
		
		if(Session.getCurrentEmail().equals(email)) {
			Platform.runLater(() -> {
				this.txt_Profile_email.getStyleClass().removeAll("field_error");
				this.txt_Profile_email.getStyleClass().removeAll("field_ok");
			});
		}else {
			changeFieldColor(this.txt_Profile_email, email_ok);
		}
		
		changeFieldColor(this.txt_Profile_pwd, pwd_ok);
		changeFieldColor(this.txt_Profile_pwdRepeat, pwd_match);
				
		Platform.runLater(() -> {
			this.btn_Profile_update.setDisable(!(email_ok || (pwd_ok && pwd_match)));
		});
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

		model.getProfUpdateStatus().removeListener(profUpdateStat);
	}
	
	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Controller#initialize()
	 */
	/**
	 * Creates change listeners for the fields in the form and pre-fills them.
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
	 * Initializes the registration status property listener.
	 */
	private void initRegistrationStatusPropertyListener() {
		this.profUpdateStat = new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				timer.getPeriodCounterProperty().removeListener(timerPropertyListener);
				timer.stop();
				if(newValue != null) {
					if(newValue.equals("OK")) {
						Platform.runLater(() -> {
							lbl_Profile_msg.setTextFill(Paint.valueOf("GREEN"));
							lbl_Profile_msg.setText(translator.getTranslation("lbl_Profile_Response_Success"));
						});
						
					}else if(newValue.equals("DELETED")){
						Network.getInstance().stopConnection();
						UserPrefs.getInstance().put("USERNAME", "");
						LoginController controller = Controller.initMVCAsRoot(LoginController.class, LoginModel.class, LoginView.class);
						controller.LoginSetMessage("msg_Profile_Deleted");
					}else {
						Platform.runLater(() -> {
							lbl_Profile_msg.setTextFill(Paint.valueOf("RED"));
							lbl_Profile_msg.setText(translator.getTranslation(newValue.toString()));
						});
					}
					switchLoaderDisplay(false);
				}
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
				Platform.runLater(() -> {
					lbl_Profile_msg.setTextFill(Paint.valueOf("RED"));
					this.lbl_Profile_msg.setText(translator.getTranslation("lbl_Login_loginMsg_ServerNoReaction"));
				});
			});
		};
	}
	
	/**
	 * Displays the hint regarding "email address" field.
	 *
	 * @param event the click event
	 */
	@FXML
	private void ProfileShowHelperText_email(ActionEvent event) {
		Platform.runLater(() -> {
			lbl_Profile_msg.setTextFill(Paint.valueOf("BLACK"));
			this.lbl_Profile_msg.setText(translator.getTranslation("lbl_Profile_Helper_email"));
		});
	}
	
	/**
	 * Displays the hint regarding "password" field.
	 *
	 * @param event the click event
	 */
	@FXML
	private void ProfileShowHelperText_password(ActionEvent event) {
		Platform.runLater(() -> {
			lbl_Profile_msg.setTextFill(Paint.valueOf("BLACK"));
			this.lbl_Profile_msg.setText(translator.getTranslation("lbl_Profile_Helper_password"));
		});
	}
	
	/**
	 * Displays the hint regarding "repeated password" field.
	 *
	 * @param event the click event
	 */
	@FXML
	private void ProfileShowHelperText_passwordRepeat(ActionEvent event) {
		Platform.runLater(() -> {
			lbl_Profile_msg.setTextFill(Paint.valueOf("BLACK"));
			this.lbl_Profile_msg.setText(translator.getTranslation("lbl_Profile_Helper_passwordRepeat"));
		});
	}
	
	/**
	 * Displays the hint regarding "username" field (not editable by the user).
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
	 * Start timeout timer while waiting for a server response.
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
	 * Hide the form and show the loading screen or vice-versa.
	 *
	 * @param loading true if loading view shall be shown, false if form has to be visible
	 */
	private void switchLoaderDisplay(boolean loading) {
		Platform.runLater(() -> {
			this.vbox_Profile_loader.setVisible(loading);
			this.vbox_Profile_form.setVisible(!loading);
		});
	}
}
