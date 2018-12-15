package ch.fhnw.projectbois.game.splitcardchooser;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.game.GameResourceHelper;
import ch.fhnw.projectbois.gameobjects.Card;
import ch.fhnw.projectbois.interfaces.IDialog;
import ch.fhnw.projectbois.utils.DialogUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SplitCardChooserController extends Controller<SplitCardChooserModel, SplitCardChooserView> implements IDialog {

	private Stage stage = null;

	private int decision = 1;

	@FXML
	private Label lblCardInfo;
	
	@FXML
	private Pane pnlLeft;

	@FXML
	private Pane pnlRight;

	public SplitCardChooserController(SplitCardChooserModel model, SplitCardChooserView view) {
		super(model, view);
	}

	public void loadSplitCard(Card splitCard, String splitCardInfo) {
		if (splitCard.isSplitCard()) {
			GameResourceHelper helper = new GameResourceHelper();

			this.lblCardInfo.setText(splitCardInfo);

			String style = "-fx-background-size: contain ; ";
			style += "-fx-background-position: center ; ";
			style += "-fx-background-repeat: stretch ; ";
			
			Card left = new Card(splitCard.getCardType1());
			String url = helper.getUrlByCard(left);
			pnlLeft.setStyle(style + "-fx-background-image: url('" + url + "'); ");

			Card right = new Card(splitCard.getCardType2());
			url = helper.getUrlByCard(right);
			pnlRight.setStyle(style + "-fx-background-image: url('" + url + "'); ");
		}
	}

	public void showAndWait() {
		this.stage = DialogUtils.getStageModal(MetaContainer.getInstance().getMainStage());
		this.stage.setTitle("Split Card");
		this.stage.setScene(new Scene(this.getViewRoot()));

		this.stage.showAndWait();
	}

	public int getDecision() {
		return this.decision;
	}

	@FXML
	private void pnlLeft_Click(MouseEvent event) {
		this.decision = 1;

		Platform.runLater(() -> {
			this.stage.close();
		});
	}

	@FXML
	private void pnlRight_Click(MouseEvent event) {
		this.decision = 2;

		Platform.runLater(() -> {
			this.stage.close();
		});
	}

}
