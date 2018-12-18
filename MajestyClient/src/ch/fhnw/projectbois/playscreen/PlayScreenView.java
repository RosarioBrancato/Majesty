package ch.fhnw.projectbois.playscreen;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;

/**
 * The PlayScreenView Class
 * @author Dario Stoeckli
 *
 */

public class PlayScreenView extends View<PlayScreenModel> {

	public PlayScreenView(PlayScreenModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("PlayScreenView.fxml");
	}

}
