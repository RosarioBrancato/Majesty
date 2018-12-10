package ch.fhnw.projectbois.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.fhnw.projectbois.enumerations.ReportSeverity;

public class ReportDTO {

	private ReportSeverity severity = null;
	private String translationKey = null;
	private ArrayList<String> formatVariables = null;
	
	/**
	 * For JSON serializer
	 */
	public ReportDTO() {
		this.setFormatVariables(new ArrayList<>());
	}
	
	public ReportDTO(ReportSeverity severity,  String translationKey) {
		this();
		this.setSeverity(severity);
		this.setTranslationKey(translationKey);
	}

	public ReportSeverity getSeverity() {
		return severity;
	}

	public void setSeverity(ReportSeverity severity) {
		this.severity = severity;
	}

	public String getTranslationKey() {
		return translationKey;
	}

	public void setTranslationKey(String translationKey) {
		this.translationKey = translationKey;
	}

	public ArrayList<String> getFormatVariables() {
		return formatVariables;
	}

	public void setFormatVariables(ArrayList<String> formatVariables) {
		this.formatVariables = formatVariables;
	}
	
	@JsonIgnore
	public Object[] getFormatVariablesAsArray() {
		return this.formatVariables.toArray();
	}
}
