package ch.fhnw.projectbois._mvc;

import java.util.logging.Logger;

import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.dto.ReportDTO;
import ch.fhnw.projectbois.log.LoggerFactory;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.translate.Translator;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

/**
 * Based on example from course Java 2
 * 
 * @author Rosario Brancato
 */
public abstract class Model {
	protected Logger logger;
	protected Translator translator;

	private SimpleObjectProperty<Response> responseProperty;
	private SimpleObjectProperty<ReportDTO> reportProperty;

	private ChangeListener<Response> responsePropertyListener = null;

	public Model() {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.translator = Translator.getTranslator();

		this.responseProperty = new SimpleObjectProperty<>();
		this.reportProperty = new SimpleObjectProperty<>();
	}

	public void destroy() {
		if (this.responsePropertyListener != null) {
			this.responseProperty.removeListener(this.responsePropertyListener);
		}
	}

	public SimpleObjectProperty<ReportDTO> getReportProperty() {
		return this.reportProperty;
	}

	protected ChangeListener<Response> getChangeListener() {
		return null;
	}

	protected void initResponseListener() {
		this.responsePropertyListener = this.getChangeListener();
		if (this.responsePropertyListener != null) {
			this.responseProperty.bind(Network.getInstance().getResponseProperty());
			this.responseProperty.addListener(this.responsePropertyListener);
		}
	}

}
