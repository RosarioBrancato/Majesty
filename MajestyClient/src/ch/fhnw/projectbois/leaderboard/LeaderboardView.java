package ch.fhnw.projectbois.leaderboard;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;

public class LeaderboardView extends View<LeaderboardModel> {

	public LeaderboardView(LeaderboardModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("LeaderboardView.fxml");
	}

}
