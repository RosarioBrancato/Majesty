package ch.fhnw.projectbois._mvc;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.fhnw.projectbois.log.LoggerFactory;
import ch.fhnw.projectbois.translate.Translator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Based on example from course Java 2.
 *
 * @author Rosario Brancato
 * @param <M> the generic type
 */
public abstract class View<M extends Model> {
	
	protected Logger logger;
	protected Translator translator;
	protected Parent root;
	protected M model;

	/**
	 * Set any options for the stage in the subclass constructor.
	 *
	 * @param model the model
	 */
	public View(M model) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.translator = Translator.getTranslator();

		this.model = model;
	}

	/**
	 * Gets the root.
	 *
	 * @return the root
	 */
	public Parent getRoot() {
		return this.root;
	}

	/**
	 * Load root.
	 *
	 * @param <T> the generic type
	 * @param controller the controller
	 */
	public <T extends Controller<M, ? extends View<M>>> void loadRoot(T controller) {
		URL url = this.getFXML();

		// Multi-Language Support from Singleton Translator
		FXMLLoader loader = new FXMLLoader(url, translator.getResourceBundle());
		loader.setController(controller);

		try {
			this.root = loader.load();

		} catch (IOException e) {
			logger.log(Level.SEVERE, "View.loadRoot()", e);
		}
	}

	/**
	 * Destroy.
	 */
	public void destroy() {

	}

	/**
	 * Gets the fxml.
	 *
	 * @return the fxml
	 */
	protected abstract URL getFXML();

}
