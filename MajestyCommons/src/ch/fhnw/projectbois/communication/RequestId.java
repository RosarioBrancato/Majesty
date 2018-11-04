package ch.fhnw.projectbois.communication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RequestId {

	LOGIN(1), LOGOUT(2), CREATE_LOBBY(90), JOIN_LOBBY(91), GET_LOBBIES(95), LEAVE_LOBBY(99), DO_MOVE(100), QUIT_GAME(120);

	private final int id;

	private RequestId(int id) {
		this.id = id;
	}

	public int getValue() {
		return id;
	}

	@JsonCreator
	public static RequestId forValue(String value) {
		return Enum.valueOf(RequestId.class, value);
	}
	
	@JsonValue
	public String getName() {
		return this.name();
	}

}
