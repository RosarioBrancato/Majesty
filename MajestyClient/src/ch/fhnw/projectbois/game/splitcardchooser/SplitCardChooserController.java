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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SplitCardChooserController extends Controller<SplitCardChooserModel, SplitCardChooserView> implements IDialog {

	private Stage stage = null;

	private int decision = 1;

	@FXML
	private ImageView imgViewLeft;

	@FXML
	private ImageView imgViewRight;

	public SplitCardChooserController(SplitCardChooserModel model, SplitCardChooserView view) {
		super(model, view);
	}

	public void loadSplitCard(Card splitCard) {
		if (splitCard.isSplitCard()) {
			GameResourceHelper helper = new GameResourceHelper();

			Card left = new Card(splitCard.getCardType1());
			this.imgViewLeft.setImage(helper.getCardImage(left));
			this.imgViewLeft.setPreserveRatio(true);
			this.imgViewLeft.setFitWidth(200);

			Card right = new Card(splitCard.getCardType2());
			this.imgViewRight.setImage(helper.getCardImage(right));
			this.imgViewRight.setPreserveRatio(true);
			this.imgViewRight.setFitWidth(200);
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
	private void imgViewLeft_Click(MouseEvent event) {
		this.decision = 1;

		Platform.runLater(() -> {
			this.stage.close();
		});
	}

	@FXML
	private void imgViewRight_Click(MouseEvent event) {
		this.decision = 2;

		Platform.runLater(() -> {
			this.stage.close();
		});
	}

}
