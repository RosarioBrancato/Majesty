package ch.fhnw.projectbois.profile;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.session.Session;
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
import javafx.scene.paint.Paint;

public class ProfileController extends Controller<ProfileModel, ProfileView> {
	
	public ProfileController(ProfileModel model, ProfileView view) {
		super(model, view);
	}
	
	private ChangeListener<String> profUpdateStat = null;
	
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
			}
		};	
	}
	
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

	@Override
	public void destroy() {
		super.destroy();

		model.getProfUpdateStatus().removeListener(profUpdateStat);
	}
	
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
	private void btn_Profile_updateClicked(ActionEvent event) {
		model.UpdateProfileProcessInput(this.txt_Profile_email.getText(), this.txt_Profile_pwd.getText());
	}
	
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
	
	@FXML
	private void ProfileShowHelperText_username(ActionEvent event) {
		Platform.runLater(() -> {
			lbl_Profile_msg.setTextFill(Paint.valueOf("BLACK"));
			this.lbl_Profile_msg.setText(translator.getTranslation("lbl_Profile_Helper_username"));
		});
	}
	
	@FXML
	private void ProfileShowHelperText_email(ActionEvent event) {
		Platform.runLater(() -> {
			lbl_Profile_msg.setTextFill(Paint.valueOf("BLACK"));
			this.lbl_Profile_msg.setText(translator.getTranslation("lbl_Profile_Helper_email"));
		});
	}
	
	@FXML
	private void ProfileShowHelperText_password(ActionEvent event) {
		Platform.runLater(() -> {
			lbl_Profile_msg.setTextFill(Paint.valueOf("BLACK"));
			this.lbl_Profile_msg.setText(translator.getTranslation("lbl_Profile_Helper_password"));
		});
	}
	
	@FXML
	private void ProfileShowHelperText_passwordRepeat(ActionEvent event) {
		Platform.runLater(() -> {
			lbl_Profile_msg.setTextFill(Paint.valueOf("BLACK"));
			this.lbl_Profile_msg.setText(translator.getTranslation("lbl_Profile_Helper_passwordRepeat"));
		});
	}
}
