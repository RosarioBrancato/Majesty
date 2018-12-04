package ch.fhnw.projectbois.registration;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.interfaces.IDialog;
import ch.fhnw.projectbois.login.LoginController;
import ch.fhnw.projectbois.utils.DialogUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrationController extends Controller<RegistrationModel, RegistrationView> implements IDialog {
	private Stage stage;
	private ChangeListener<String> regStat = null;
	private String server = null;
	private int port = 0;
	
	public RegistrationController(RegistrationModel model, RegistrationView view) {
		super(model, view);
	}
	
	private void initLoginStatusPropertyListener() {
		this.regStat = new ChangeListener<String>() {
	
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				Platform.runLater(() -> {
					//lbl_Login_loginMsg.setText(translator.getTranslation(newValue.toString()));
				});
			}
		};	
	}
	
	@Override
	protected void initialize() {
		System.out.println("Initializing RegContr");
		this.initLoginStatusPropertyListener();
		model.getRegistrationStatus().addListener(regStat);
	}

	public void showAndWait() {
		this.stage = DialogUtils.getStageModal(MetaContainer.getInstance().getMainStage());
		this.stage.setTitle(translator.getTranslation("lbl_Registration_Title"));
		//this.stage.initStyle(StageStyle.UNDECORATED);
		this.stage.setScene(new Scene(this.getViewRoot()));
		this.stage.showAndWait();
		
	}
	
	public void setServerParam(String server, int port) {
		this.server = server;
		this.port = port;
		System.out.println(server+":"+port);
	}
	
	@Override
	public void destroy() {
		super.destroy();

		model.getRegistrationStatus().removeListener(regStat);
	}
	
	/*@FXML
	private Label lbl_Registration_username;
	
	@FXML
	private Label lbl_Registration_email;
	
	@FXML
	private Label lbl_Registration_pwd;
	
	@FXML
	private Label lbl_Registration_pwdRepead;

	@FXML
	private Button btn_Registration_cancel;
	
	@FXML
	private Button btn_Registration_register;
	
	@FXML
	private TextField txt_Registration_username;
	
	@FXML
	private TextField txt_Registration_email;
	
	@FXML
	private PasswordField txt_Registration_pwd;
	
	@FXML
	private PasswordField txt_Registration_pwdRepead;
	*/
	
	@FXML
	private TextField txt_Registration_username;
	
	@FXML
	private TextField txt_Registration_email;
	
	@FXML
	private PasswordField txt_Registration_pwd;
	
	@FXML
	private PasswordField txt_Registration_pwdRepeat;
	
	@FXML
	private void btn_Registration_cancelClicked(ActionEvent event) {
		this.stage.close();
	}
	
	@FXML
	private void btn_Registration_registerClicked(ActionEvent event) {
		model.RegistrationProcessInput(
				this.server, this.port, this.txt_Registration_username.getText(), this.txt_Registration_email.getText(), this.txt_Registration_pwd.getText());
	}

}
