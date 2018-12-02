package ch.fhnw.projectbois.game.meepletrader;

import java.net.URL;

import ch.fhnw.projectbois._mvc.View;

public class MeepleTraderView extends View<MeepleTraderModel> {

	public MeepleTraderView(MeepleTraderModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("MeepleTraderView.fxml");
	}

}
