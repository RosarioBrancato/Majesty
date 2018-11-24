package ch.fhnw.projectbois.login;

import java.util.Locale;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;

public class LoginController extends Controller<LoginModel, LoginView> {
	
	//Language PickList selection
	private final String ENGLISH = "en";
	private final String GERMAN = "de";
	private final String FRENCH = "fr";
	private final String ITALIAN = "it";
	
	@FXML
	private ChoiceBox<String> cmbLanguage;

	public LoginController(LoginModel model, LoginView view) {
		super(model, view);
	}
	
	@Override
	protected void initialize() {
		super.initialize();

		this.fillChoiceBox();
		
        cmbLanguage.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue) -> {
            Locale locale = new Locale("en");
            if (newValue.equals(GERMAN)) {
            	locale = new Locale("de");
            } else if (newValue.equals(FRENCH)) {
            	locale = new Locale("fr");
            } else if (newValue.equals(ITALIAN)) {
            	locale = new Locale("it");
            }
        	super.model.setLocale(locale);
        });
	}
	
	private void fillChoiceBox() {
		this.cmbLanguage.getItems().add(ENGLISH);
		this.cmbLanguage.getItems().add(GERMAN);
		this.cmbLanguage.getItems().add(FRENCH);
		this.cmbLanguage.getItems().add(ITALIAN);

		this.cmbLanguage.getSelectionModel().selectFirst();
	}

}
