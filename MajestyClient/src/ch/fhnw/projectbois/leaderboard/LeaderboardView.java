package ch.fhnw.projectbois.leaderboard;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;

/**
 * The Class LeaderboardView.
 * @author Dario Stöckli
 */
public class LeaderboardView extends View<LeaderboardModel> {

	/**
	 * Instantiates a new leaderboard view.
	 *
	 * @param model the model
	 */
	public LeaderboardView(LeaderboardModel model) {
		super(model);
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.View#getFXML()
	 */
	@Override
	protected URL getFXML() {
		return this.getClass().getResource("LeaderboardView.fxml");
	}

}
