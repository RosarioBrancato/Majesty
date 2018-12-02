package ch.fhnw.projectbois.game.meepletrader;

import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.gameobjects.GameMove;
import ch.fhnw.projectbois.utils.DialogUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class MeepleTraderController extends Controller<MeepleTraderModel, MeepleTraderView> {

	private Stage stage;
	private int meeples = 0;
	private int points = 0;

	@FXML
	private RadioButton rdbBuy;

	@FXML
	private RadioButton rdbSell;

	@FXML
	private Slider sliderMeeples;

	public MeepleTraderController(MeepleTraderModel model, MeepleTraderView view) {
		super(model, view);
	}

	public void setCurrencies(int meeples, int points) {
		this.meeples = meeples;
		this.points = points;

		this.setUpSlider();
	}

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

	private void setUpSlider() {
		sliderMeeples.setValue(0);

		if (rdbBuy.isSelected()) {
			if (points >= 5) {
				sliderMeeples.setMax(5);
			} else {
				sliderMeeples.setMax(points);
			}

		} else if (rdbSell.isSelected()) {
			if (meeples >= 5) {
				sliderMeeples.setMax(5);
			} else {
				sliderMeeples.setMax(meeples);
			}
		}
	}

	@FXML
	private void rdbBuy_Click(ActionEvent event) {
		Platform.runLater(() -> {
			this.setUpSlider();
		});
	}

	@FXML
	private void rdbSell_Click(ActionEvent event) {
		Platform.runLater(() -> {
			this.setUpSlider();
		});
	}

	@FXML
	private void btnTrade_Click(ActionEvent event) {
		Platform.runLater(() -> {
			this.stage.close();
		});
	}

}
