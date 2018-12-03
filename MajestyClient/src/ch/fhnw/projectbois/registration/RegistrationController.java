package ch.fhnw.projectbois.registration;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.interfaces.IDialog;
import ch.fhnw.projectbois.utils.DialogUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RegistrationController extends Controller<RegistrationModel, RegistrationView> implements IDialog {
	private Stage stage;
	
	public RegistrationController(RegistrationModel model, RegistrationView view) {
		super(model, view);
	}

	public void showAndWait() {
		this.stage = DialogUtils.getStageModal(MetaContainer.getInstance().getMainStage());
		this.stage.setTitle(translator.getTranslation("lbl_Registration_Title"));
		//this.stage.initStyle(StageStyle.UNDECORATED);
		this.stage.setScene(new Scene(this.getViewRoot()));
		this.stage.showAndWait();
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
	private void btn_Registration_cancelClicked(ActionEvent event) {
		this.stage.close();
	}
	
	@FXML
	private void btn_Registration_registerClicked(ActionEvent event) {
		System.out.println("Register Clicked");
	}

}
