package ch.fhnw.projectbois.game;

import java.util.ArrayList;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.gameobjects.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class GameController extends Controller<GameModel, GameView> {

	private final String PATH_TO_CARD = "cards/character cards/";
	private final String PATH_TO_LOCATION_A = "locations/Side A/";

	private GameState gameState = null;

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

		this.model.getGameStateProperty().addListener((observer, oldValue, newValue) -> {
			loadGameState(newValue);
		});
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
			v.setFitWidth(100);

			this.pnlPlayer1Cards.getChildren().add(v);
		}
	}

	private void loadGameState(GameState gameState) {
		this.logger.info("GameController.loadGameState()");

		if (this.gameState == null || gameState.getId() > this.gameState.getId()) {
			this.gameState = gameState;
			this.drawGui();
		}
	}

	private void drawGui() {
		Board board = this.gameState.getBoard();

		this.drawDisplay(board.getDisplay());
	}

	private void drawDisplay(ArrayList<DisplayCard> displayCards) {
		Platform.runLater(() -> {
			this.pnlDisplay.getChildren().clear();
		});
		for (DisplayCard card : displayCards) {
			ImageView v = new ImageView();

			switch (card.getCardType()) {
			case Miller:
				v.setImage(new Image(PATH_TO_CARD + "Orange.jpg"));
				break;
			case Brewer:
				v.setImage(new Image(PATH_TO_CARD + "Brown.jpg"));
				break;
			case Guard:
				v.setImage(new Image(PATH_TO_CARD + "Blue.jpg"));
				break;
			case Innkeeper:
				v.setImage(new Image(PATH_TO_CARD + "Yellow.jpg"));
				break;
			case Knight:
				v.setImage(new Image(PATH_TO_CARD + "Red.jpg"));
				break;
			case Noble:
				v.setImage(new Image(PATH_TO_CARD + "Violet.jpg"));
				break;
			case Witch:
				v.setImage(new Image(PATH_TO_CARD + "Green.jpg"));
				break;
			default:
				break;
			}

			v.setPreserveRatio(true);
			v.setFitHeight(100);

			v.setOnMouseClicked((e) -> {
				logger.info("Image clicked!");
				model.sendMove();
			});

			BorderPane borderPane = new BorderPane(v);
			borderPane.getStyleClass().add("display");

			Platform.runLater(() -> {
				this.pnlDisplay.getChildren().add(borderPane);
			});
		}
	}

}
