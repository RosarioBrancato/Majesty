package ch.fhnw.projectbois.communication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseId {

	EMPTY(0), PLAYERS_LOBBY(40), UPDATE_GAMESTATE(50), TEST(999);
	
	private final int id;

	private ResponseId(int id) {
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
