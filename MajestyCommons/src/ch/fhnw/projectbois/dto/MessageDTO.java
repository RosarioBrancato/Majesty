package ch.fhnw.projectbois.dto;

import java.util.ArrayList;

import ch.fhnw.projectbois.enumerations.ChatMember;

public class MessageDTO {

	private int id;
	private ChatMember receiver;
	private ChatMember author;
	private String message;

	private String translationKey;
	private ArrayList<String> formatVariables;

	public MessageDTO() {
		this.message = "";
		this.formatVariables = new ArrayList<>();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ChatMember getReceiver() {
		return receiver;
	}

	public void setReceiver(ChatMember receiver) {
		this.receiver = receiver;
	}

	public ChatMember getAuthor() {
		return author;
	}

	public void setAuthor(ChatMember author) {
		this.author = author;
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

	public ArrayList<String> getFormatVariables() {
		return formatVariables;
	}

	public void setFormatVariables(ArrayList<String> formatVariables) {
		this.formatVariables = formatVariables;
	}

}
