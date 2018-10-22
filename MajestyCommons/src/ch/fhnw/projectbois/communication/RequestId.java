package ch.fhnw.projectbois.communication;

public enum RequestId {

	LOGIN(1), LOGOUT(2), CREATE_LOBBY(90), LEAVE_LOBBY(91), DO_MOVE(100), QUIT_GAME(120);

	private final int id;

	private RequestId(int id) {
		this.id = id;
	}

	public int getValue() {
		return id;
	}

}
