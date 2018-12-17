package ch.fhnw.projectbois.game.splitcardchooser;

import java.net.URL;

import ch.fhnw.projectbois._mvc.Controller;
import ch.fhnw.projectbois._mvc.View;

/**
 * The Class SplitCardChooserView.
 * 
 * @author Rosario Brancato
 */
public class SplitCardChooserView extends View<SplitCardChooserModel>{

	/**
	 * Instantiates a new split card chooser view.
	 *
	 * @param model the model
	 */
	public SplitCardChooserView(SplitCardChooserModel model) {
		super(model);
	}

	@Override
	protected URL getFXML() {
		return this.getClass().getResource("SplitCardChooserView.fxml");
	}
	
	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.View#loadRoot(ch.fhnw.projectbois._mvc.Controller)
	 */
	@Override
	public <T extends Controller<SplitCardChooserModel, ? extends View<SplitCardChooserModel>>> void loadRoot(
			T controller) {
		super.loadRoot(controller);

		String css = this.getClass().getResource("SplitCardChooserView.css").toExternalForm();
		this.root.getStylesheets().add(css);
	}

}
