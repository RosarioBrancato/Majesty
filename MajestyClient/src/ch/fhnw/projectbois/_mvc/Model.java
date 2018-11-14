package ch.fhnw.projectbois._mvc;

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

	private SimpleObjectProperty<Response> responseProperty;

	protected Model() {
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

	protected void initResponseListener() {
		ChangeListener<Response> listener = this.getChangeListener();
		if (listener != null) {
			this.responseProperty = new SimpleObjectProperty<>();
			this.responseProperty.bind(Network.getInstance().getResponse());
			this.responseProperty.addListener(listener);
		}
	}

	protected ChangeListener<Response> getChangeListener() {
		return null;
	}

}
