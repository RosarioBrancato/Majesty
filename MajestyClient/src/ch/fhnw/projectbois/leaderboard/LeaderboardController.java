package ch.fhnw.projectbois.leaderboard;

import ch.fhnw.projectbois._mvc.Controller;

public class LeaderboardController extends Controller<LeaderboardModel, LeaderboardView> {

	public LeaderboardController() {
	}

	public LeaderboardController(LeaderboardModel model, LeaderboardView view) {
		super(model, view);
	}

}
