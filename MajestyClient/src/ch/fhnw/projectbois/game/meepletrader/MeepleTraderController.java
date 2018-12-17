package ch.fhnw.projectbois.game.meepletrader;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._interfaces.IDialog;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._utils.DialogUtils;
import ch.fhnw.projectbois.gameobjects.GameMove;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

/**
 * The Class MeepleTraderController.
 * 
 * @author Rosario Brancato
 */
public class MeepleTraderController extends Controller<MeepleTraderModel, MeepleTraderView> implements IDialog {

	private Stage stage;
	private int meeples = 0;
	private int points = 0;
	
	@FXML
	private Label lblMeeples;
	
	@FXML
	private Label lblPoints;

	@FXML
	private RadioButton rdbBuy;

	@FXML
	private RadioButton rdbSell;

	@FXML
	private Slider sliderMeeples;

	/**
	 * Instantiates a new meeple trader controller.
	 *
	 * @param model the model
	 * @param view the view
	 */
	public MeepleTraderController(MeepleTraderModel model, MeepleTraderView view) {
		super(model, view);
	}

	/**
	 * Sets the currencies.
	 *
	 * @param meeples the meeples
	 * @param points the points
	 */
	public void setCurrencies(int meeples, int points) {
		this.meeples = meeples;
		this.points = points;
		
		this.lblMeeples.setText(String.valueOf(this.meeples));
		this.lblPoints.setText(String.valueOf(this.points));

		this.setUpSlider();
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois.interfaces.IDialog#showAndWait()
	 */
	public void showAndWait() {
		this.stage = DialogUtils.getStageModal(MetaContainer.getInstance().getMainStage());
		this.stage.setTitle("Trader");
		this.stage.setScene(new Scene(this.getViewRoot()));

		this.stage.showAndWait();
	}

	public int getDecision() {
		// decision = amount of meeples bought(positive number) or sold (negative
		// number)
		int decision = GameMove.DECISION_NONE;

		int meeples = (int) sliderMeeples.getValue();

		if (rdbBuy.isSelected()) {
			decision = meeples;

		} else if (rdbSell.isSelected()) {
			decision = meeples * -1;
		}

		return decision;
	}

	/**
	 * Sets the up slider.
	 */
	private void setUpSlider() {
		sliderMeeples.setValue(0);

		if (rdbBuy.isSelected()) {
			if (points >= 5) {
				//ruling: max 5 meeples
				sliderMeeples.setMax(5);
			} else {
				sliderMeeples.setMax(points);
			}

		} else if (rdbSell.isSelected()) {
			if (meeples >= 5) {
				//ruling: max 5 meeples
				sliderMeeples.setMax(5);
			} else {
				sliderMeeples.setMax(meeples);
			}
		}
	}

	/**
	 * Rdb buy click.
	 *
	 * @param event the event
	 */
	@FXML
	private void rdbBuy_Click(ActionEvent event) {
		Platform.runLater(() -> {
			this.setUpSlider();
		});
	}

	/**
	 * Rdb sell click.
	 *
	 * @param event the event
	 */
	@FXML
	private void rdbSell_Click(ActionEvent event) {
		Platform.runLater(() -> {
			this.setUpSlider();
		});
	}

	/**
	 * Btn trade click.
	 *
	 * @param event the event
	 */
	@FXML
	private void btnTrade_Click(ActionEvent event) {
		Platform.runLater(() -> {
			this.stage.close();
		});
	}

}
