package ch.fhnw.projectbois.game;

import ch.fhnw.projectbois._mvc.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class GameController extends Controller<GameModel, GameView> {

	private final String PATH_TO_CARD = "cards/character cards/";
	private final String PATH_TO_LOCATION_A = "locations/Side A/";

	@FXML
	private HBox pnlDisplay;
	
	@FXML
	private HBox pnlPlayer1Cards;

	public GameController(GameModel model, GameView view) {
		super(model, view);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		
		this.initLocations();
		this.initDisplay();
	}

	@FXML
	private void btnLeave_Click(ActionEvent e) {

		
	}

	private void initLocations() {
		String[] locations = new String[] { PATH_TO_LOCATION_A + "Side A1.jpg", PATH_TO_LOCATION_A + "Side A2.jpg",
				PATH_TO_LOCATION_A + "Side A3.jpg", PATH_TO_LOCATION_A + "Side A4.jpg",
				PATH_TO_LOCATION_A + "Side A5.jpg", PATH_TO_LOCATION_A + "Side A6.jpg",
				PATH_TO_LOCATION_A + "Side A7.jpg", PATH_TO_LOCATION_A + "Side A8.jpg" };

		for (String location : locations) {
			ImageView v = new ImageView(location);
			v.setPreserveRatio(true);
			v.setFitWidth(150);

			this.pnlPlayer1Cards.getChildren().add(v);
		}
	}
	
	public void initDisplay() {
		String[] chars = new String[] {
				PATH_TO_CARD + "Blue.jpg",
				PATH_TO_CARD + "Brown.jpg",
				PATH_TO_CARD + "Green.jpg",
				PATH_TO_CARD + "Orange.jpg",
				PATH_TO_CARD + "Red.jpg",
				PATH_TO_CARD + "Violet.jpg",
				PATH_TO_CARD + "Yellow.jpg"
		};
		
		for (String c : chars) {
			ImageView v = new ImageView(c);
			v.setPreserveRatio(true);
			v.setFitWidth(150);

			this.pnlDisplay.getChildren().add(v);
		}
	}

}
