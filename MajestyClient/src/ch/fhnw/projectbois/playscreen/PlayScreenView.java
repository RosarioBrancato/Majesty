package ch.fhnw.projectbois.playscreen;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;

/**
 * 
 * @author Dario Stöckli
 *
 */

public class PlayScreenView extends View<PlayScreenModel> {

	public PlayScreenView(PlayScreenModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("PlayScreenView.fmxl");
	}
	
}
