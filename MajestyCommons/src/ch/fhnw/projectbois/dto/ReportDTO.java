package ch.fhnw.projectbois.dto;

import ch.fhnw.projectbois.enumerations.ReportSeverity;

public class ReportDTO {

	private ReportSeverity severity = null;
	private String message = null;
	
	/**
	 * For JSON serializer
	 */
	public ReportDTO() {
		
	}
	
	public ReportDTO(ReportSeverity severity, String message) {
		this.setSeverity(severity);
		this.setMessage(message);
	}

	public ReportSeverity getSeverity() {
		return severity;
	}

	public void setSeverity(ReportSeverity severity) {
		this.severity = severity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
