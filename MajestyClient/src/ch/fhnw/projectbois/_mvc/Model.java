package ch.fhnw.projectbois._mvc;

import java.util.Locale;
import java.util.logging.Logger;

import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.log.LoggerFactory;
import ch.fhnw.projectbois.network.Network;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

/**
 * Based on example from course Java 2
 * 
 * @author Rosario Brancato
 */
public abstract class Model {
	protected Logger logger;
	// Set locale to English in the beginning
	private static Locale locale = new Locale("en");

	private SimpleObjectProperty<Response> responseProperty;

	public Model() {
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

	protected void initResponseListener() {
		ChangeListener<Response> listener = this.getChangeListener();
		if (listener != null) {
			this.responseProperty = new SimpleObjectProperty<>();
			this.responseProperty.bind(Network.getInstance().getResponseProperty());
			this.responseProperty.addListener(listener);
		}
	}

	protected ChangeListener<Response> getChangeListener() {
		return null;
	}
	
	public Locale getLocale() {
		return Model.locale;
	}
	
	public void setLocale(Locale locale) {
		Model.locale = locale;
	}

}
