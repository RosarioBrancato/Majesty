package ch.fhnw.projectbois.dto;

import ch.fhnw.projectbois.enumerations.ReportSeverity;

public class ReportDTO {

	private ReportSeverity severity = null;
	private String message = null;
	private String translationKey = null;
	
	/**
	 * For JSON serializer
	 */
	public ReportDTO() {
		
	}
	
	public ReportDTO(ReportSeverity severity, String message) {
		this(severity, message, null);
	}
	
	public ReportDTO(ReportSeverity severity, String defaultMessage, String translationKey) {
		this.setSeverity(severity);
		this.setMessage(defaultMessage);
		this.setTranslationKey(translationKey);
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

	public String getTranslationKey() {
		return translationKey;
	}

	public void setTranslationKey(String translationKey) {
		this.translationKey = translationKey;
	}
}
