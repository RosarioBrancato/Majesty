/*
 * Controller for the GameEnd View
 */
package ch.fhnw.projectbois.gameend;

import java.util.ArrayList;
import ch.fhnw.projectbois._application.MetaContainer;
import ch.fhnw.projectbois._interfaces.IDialog;
import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._utils.DialogUtils;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.gameobjects.Player;
import ch.fhnw.projectbois.session.Session;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * @author Dario Stoeckli
 * 
 */

public class GameEndController extends Controller<GameEndModel, GameEndView> implements IDialog {

	private GameState gameState = null;
	private Player thisplayer = null;

	private Stage stage = null;

	@FXML
	private Label lblPlayersRank;
	@FXML
	private Label lblPlayerRanking;
	@FXML
	private Label lblPlayerRanking1;
	@FXML
	private Label lblPlayerRanking2;
	@FXML
	private Label lblPlayerRanking3;
	@FXML
	private Label lblPlayerRanking4;
	@FXML
	private Label lblGamePoints;
	@FXML
	private Label lblInfirmaryPoints;
	@FXML
	private Label lblVarietyPoints;
	@FXML
	private Label lblMajorityPoints;
	@FXML
	private Label lblOverallPoints;
	@FXML
	private Label lblDatabase;
	
	/**
	 * Instantiates a new game end controller.
	 *
	 * @param model the model
	 * @param view the view
	 */
	public GameEndController(GameEndModel model, GameEndView view) {
		super(model, view);
	}
	
	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois.interfaces.IDialog#showAndWait()
	 */
	public void showAndWait() {
		this.stage = DialogUtils.getStageModal(MetaContainer.getInstance().getMainStage());
		this.stage.setTitle(translator.getTranslation("ttl_GameEndView_Title"));
		this.stage.setScene(new Scene(this.getViewRoot()));

		// Gets the player ranking of this game from the gameState and sorts it
		// with the determineRanking method, then to updates the labels
		ArrayList<Player> ranking = model.determineRanking(gameState.getBoard().getPlayers());
		// Update labels
		setLocalInformation(ranking);
		
		this.stage.showAndWait();
	}
	
	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Controller#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();
	}
	
	/**
	 * Closes the stage with the GameEnd Information
	 *
	 * @param event the event
	 */
	@FXML
	private void btnConfirm_Click(ActionEvent event) {
		Platform.runLater(() -> {
			this.stage.close();
		});
	}

	/**
	 * Sets the local information - according to each player
	 * 
	 * Displays a ranking, tells the player what rank he/she is on and shows
	 * statistics
	 *
	 * @param ranking the ArrayList consisting of Player objects ordered according to gained points
	 */	
	private void setLocalInformation(ArrayList<Player> ranking) {
		this.lblPlayerRanking1.setVisible(false);
		this.lblPlayerRanking2.setVisible(false);
		this.lblPlayerRanking3.setVisible(false);
		this.lblPlayerRanking4.setVisible(false);

		int index = 0;

		// List the player on the corresponding position and make the Label visible
		for (Player player : ranking) {
			if (index == 0) {
				this.lblPlayerRanking1.setVisible(true);
				this.lblPlayerRanking1.setText("1) " + player.getUsername() + " - " + player.getPoints());
			}
			if (index == 1) {
				this.lblPlayerRanking2.setVisible(true);
				this.lblPlayerRanking2.setText("2) " + player.getUsername() + " - " + player.getPoints());
			}
			if (index == 2) {
				this.lblPlayerRanking3.setVisible(true);
				this.lblPlayerRanking3.setText("3) " + player.getUsername() + " - " + player.getPoints());
			}
			if (index == 3) {
				this.lblPlayerRanking4.setVisible(true);
				this.lblPlayerRanking4.setText("4) " + player.getUsername() + " - " + player.getPoints());
			}

			// Determine the rank of the player and show according Label
			if (player.getUsername().equals(Session.getCurrentUsername())) {
				thisplayer = player;

				if (index == 0)
					Platform.runLater(() -> {
						this.lblPlayersRank.setText(translator.getTranslation("lbl_GameEndView_PlayersRank1"));
					});
				if (index == 1)
					Platform.runLater(() -> {
						this.lblPlayersRank.setText(translator.getTranslation("lbl_GameEndView_PlayersRank2"));
					});
				if (index == 2)
					Platform.runLater(() -> {
						this.lblPlayersRank.setText(translator.getTranslation("lbl_GameEndView_PlayersRank3"));
					});
				if (index == 3)
					Platform.runLater(() -> {
						this.lblPlayersRank.setText(translator.getTranslation("lbl_GameEndView_PlayersRank4"));
					});

			}
			index++;
		}

		// Update all the statistics with player information that is assigned to the client
		Platform.runLater(() -> {
			this.lblGamePoints.setText(translator.getTranslation("lbl_GameEndView_GamePoints") + " "
					+ thisplayer.getFinalCalculation().getGameCount());
			this.lblInfirmaryPoints.setText(translator.getTranslation("lbl_GameEndView_InfirmaryPoints") + " "
					+ thisplayer.getFinalCalculation().getInfirmaryCount());
			this.lblVarietyPoints.setText(translator.getTranslation("lbl_GameEndView_VarietyPoints") + " "
					+ thisplayer.getFinalCalculation().getLocationCount());
			this.lblMajorityPoints.setText(translator.getTranslation("lbl_GameEndView_MajorityPoints") + " "
					+ thisplayer.getFinalCalculation().getMajorityCount());
			this.lblOverallPoints.setText(translator.getTranslation("lbl_GameEndView_OverallPoints") + " "
					+ thisplayer.getFinalCalculation().getTotalCount());
			this.lblDatabase.setText(thisplayer.getFinalCalculation().getTotalCount() + " "
					+ translator.getTranslation("lbl_GameEndView_Database"));
		});
	}

	/**
	 * Gets the game state.
	 *
	 * @return the game state
	 */
	public GameState getGameState() {
		return gameState;
	}

	/**
	 * Sets the game state.
	 *
	 * @param gameState the new game state
	 */
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

}
