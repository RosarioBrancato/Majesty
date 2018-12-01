package ch.fhnw.projectbois._mvc;

import java.util.logging.Logger;

import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.dto.ReportDTO;
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
	private SimpleObjectProperty<ReportDTO> reportProperty;

	public Model() {
		this.logger = LoggerFactory.getLogger(this.getClass());
		
		this.responseProperty = new SimpleObjectProperty<>();
		this.reportProperty = new SimpleObjectProperty<>();
	}

	protected void initResponseListener() {
		ChangeListener<Response> listener = this.getChangeListener();
		if (listener != null) {
			this.responseProperty.bind(Network.getInstance().getResponseProperty());
			this.responseProperty.addListener(listener);
		}
	}
	
	public  SimpleObjectProperty<ReportDTO> getReportProperty() {
		return this.reportProperty;
	}

	protected ChangeListener<Response> getChangeListener() {
		return null;
	}
	
}
