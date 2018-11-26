package ch.fhnw.projectbois.dto;

import ch.fhnw.projectbois.enumerations.ChatMember;

public class MessageDTO {
	int id;
	ChatMember receiver;
	ChatMember author;
	String message;
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
	
}
