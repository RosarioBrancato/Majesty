package ch.fhnw.projectbois.playscreen;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.dto.LobbyListDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;

/**
 * 
 * @author Dario Stöckli
 *
 */

public class PlayScreenController extends Controller<PlayScreenModel, PlayScreenView> {

	private final String SIDE_A = "Side A";
	private final String SIDE_B = "Side B";

	@FXML
	private ChoiceBox<String> cmbCardSide;

	@FXML
	private ListView<LobbyDTO> lstLobbies;

	public PlayScreenController(PlayScreenModel model, PlayScreenView view) {
		super(model, view);
	}

	@Override
	protected void initialize() {
		super.initialize();

		this.fillChoiceBox();

		model.getLobbiesProperty().addListener((observer, oldValue, newValue) -> {
			fillListView(newValue);
		});

		model.getErrorProperty().addListener((observer, oldValue, newValue) -> {
			Platform.runLater(() -> {
				Alert dlg = new Alert(AlertType.ERROR);
				dlg.setTitle("ERROR");
				dlg.setHeaderText(null);
				dlg.setContentText("An error occured. Please try again.");

				dlg.initOwner(MetaContainer.getInstance().getMainStage());
				dlg.initModality(Modality.APPLICATION_MODAL);

				dlg.showAndWait();

				getLobbies();
			});
		});

		this.getLobbies();
	}

	private void fillChoiceBox() {
		this.cmbCardSide.getItems().add(SIDE_A);
		this.cmbCardSide.getItems().add(SIDE_B);

		this.cmbCardSide.getSelectionModel().selectFirst();
	}

	private void getLobbies() {
		model.getLobbies();
	}

	private void fillListView(LobbyListDTO lobbies) {
		Platform.runLater(() -> {
			lstLobbies.getItems().clear();
			lstLobbies.getItems().addAll(lobbies.getLobbies());
			lstLobbies.getSelectionModel().selectFirst();
		});
	}

	@FXML
	public void btnStart_Click(ActionEvent e) {
		String side = cmbCardSide.getValue();
		boolean isSindA = side.equals(SIDE_A);

		LobbyDTO lobby = new LobbyDTO();
		lobby.setCardSideA(isSindA);

		model.createLobby(lobby);
	}

	@FXML
	private void btnJoin_Click(ActionEvent e) {
		this.btnRefresh_Click(new ActionEvent());
		LobbyDTO lobby = lstLobbies.getSelectionModel().getSelectedItem();
		if (lobby != null) {
			model.joinLobby(lobby);
		}
	}

	@FXML
	private void lstLobbies_DoubleClick(MouseEvent click) {
		if (click.getButton() == MouseButton.PRIMARY && click.getClickCount() == 2) {
			this.btnJoin_Click(new ActionEvent());
		}
	}

	@FXML
	private void btnRefresh_Click(ActionEvent e) {
		getLobbies();
	}

}
