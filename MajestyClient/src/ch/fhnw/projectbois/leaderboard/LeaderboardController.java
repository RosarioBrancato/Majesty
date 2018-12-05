package ch.fhnw.projectbois.leaderboard;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois.dto.LeaderboardDTO;
import ch.fhnw.projectbois.dto.LeaderboardPlayerDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class LeaderboardController extends Controller<LeaderboardModel, LeaderboardView> {

	@FXML
	private ListView<LeaderboardPlayerDTO> lstPlayers;
	
	/*@FXML
	private TableView<LeaderboardPlayerDTO> tblPlayer;
	
	@FXML
	private TableColumn<LeaderboardPlayerDTO, String> usernameColumn;
	@FXML
	private TableColumn<LeaderboardPlayerDTO, Integer> rankColumn;
	@FXML
	private TableColumn<LeaderboardPlayerDTO, Integer> pointsColumn;
	*/
	@FXML
	private Label lblCurrentPlayer_Dynamic;
	
	public LeaderboardController(LeaderboardModel model, LeaderboardView view) {
		super(model, view);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		
		model.getLeaderboardProperty().addListener((observer, oldValue, newValue) -> {
			fillListView(newValue);
		});
		
		model.getLeaderboardPlayerProperty().addListener((observer, oldValue, newValue) -> {
			showPlayerInformation(newValue);
		});
		
		getLeaderboard();
		
	}
	
	private void getLeaderboard() {
		model.queryLeaderboard();
	}
	
	
	private void fillListView(LeaderboardDTO leaderboard) {
		Platform.runLater(() -> {
			//tblPlayer.getColumns().addAll(usernameColumn);
			//usernameColumn.setCellValueFactory(new PropertyValueFactory<LeaderboardPlayerDTO, String>(leaderboard.getLeaderboard().get(0).getUsername()));
			//tblPlayer.setItems();
			lstPlayers.getItems().clear();
			lstPlayers.getItems().addAll(leaderboard.getLeaderboard());
		});
	}
	
	private void showPlayerInformation(LeaderboardPlayerDTO currentplayer) {
		Platform.runLater(() -> {
			lblCurrentPlayer_Dynamic.setText(translator.getTranslation("lbl_Leaderboard_CurrentPlayer") + " " + currentplayer.getUsername() 
			+ " " + translator.getTranslation("lbl_Leaderboard_CurrentPlayer_Add1") + " " + currentplayer.getPoints() + " " 
			+ translator.getTranslation("lbl_Leaderboard_CurrentPlayer_Add2") + " " + currentplayer.getRank());
		});
	}
	

}
