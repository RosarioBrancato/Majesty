package ch.fhnw.projectbois.game.splitcardchooser;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._interfaces.IDialog;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._utils.DialogUtils;
import ch.fhnw.projectbois.game.GameResourceHelper;
import ch.fhnw.projectbois.gameobjects.Card;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The Class SplitCardChooserController.
 * 
 * @author Rosario Brancato
 */
public class SplitCardChooserController extends Controller<SplitCardChooserModel, SplitCardChooserView> implements IDialog {

	private Stage stage = null;

	private int decision = 1;

	@FXML
	private Label lblCardInfo;
	
	@FXML
	private Pane pnlLeft;

	@FXML
	private Pane pnlRight;

	/**
	 * Instantiates a new split card chooser controller.
	 *
	 * @param model the model
	 * @param view the view
	 */
	public SplitCardChooserController(SplitCardChooserModel model, SplitCardChooserView view) {
		super(model, view);
	}

	/**
	 * Load split card.
	 *
	 * @param splitCard the split card
	 * @param splitCardInfo the split card info
	 */
	public void loadSplitCard(Card splitCard, String splitCardInfo) {
		if (splitCard.isSplitCard()) {
			GameResourceHelper helper = new GameResourceHelper();

			this.lblCardInfo.setText(splitCardInfo);

			String style = "-fx-background-size: contain ; ";
			style += "-fx-background-position: center ; ";
			style += "-fx-background-repeat: stretch ; ";
			
			Card left = new Card(splitCard.getCardType1(), splitCard.getCardBack());
			String url = helper.getUrlByCard(left);
			pnlLeft.setStyle(style + "-fx-background-image: url('" + url + "'); ");

			Card right = new Card(splitCard.getCardType2(), splitCard.getCardBack());
			url = helper.getUrlByCard(right);
			pnlRight.setStyle(style + "-fx-background-image: url('" + url + "'); ");
		}
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois.interfaces.IDialog#showAndWait()
	 */
	public void showAndWait() {
		this.stage = DialogUtils.getStageModal(MetaContainer.getInstance().getMainStage());
		this.stage.setTitle("Split Card");
		this.stage.setScene(new Scene(this.getViewRoot()));

		this.stage.showAndWait();
	}

	public int getDecision() {
		return this.decision;
	}

	/**
	 * Pnl left click.
	 *
	 * @param event the event
	 */
	@FXML
	private void pnlLeft_Click(MouseEvent event) {
		this.decision = 1;

		Platform.runLater(() -> {
			this.stage.close();
		});
	}

	/**
	 * Pnl right click.
	 *
	 * @param event the event
	 */
	@FXML
	private void pnlRight_Click(MouseEvent event) {
		this.decision = 2;

		Platform.runLater(() -> {
			this.stage.close();
		});
	}

}
