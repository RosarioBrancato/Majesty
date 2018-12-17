package ch.fhnw.projectbois.game.meepletrader;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;

/**
 * The Class MeepleTraderView.
 * 
 * @author Rosario Brancato
 */
public class MeepleTraderView extends View<MeepleTraderModel> {

	/**
	 * Instantiates a new meeple trader view.
	 *
	 * @param model the model
	 */
	public MeepleTraderView(MeepleTraderModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("MeepleTraderView.fxml");
	}

}
