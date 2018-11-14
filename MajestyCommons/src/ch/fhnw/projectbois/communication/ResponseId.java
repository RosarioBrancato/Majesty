package ch.fhnw.projectbois.communication;

public class ResponseId {

	public static final int EMPTY = -1;

	public static final int AUTH_RANGE_START = 0;
	public static final int AUTH_RANGE_END = 99;

	public static final int LOBBY_RANGE_START = 100;
	public static final int PLAYERS_LOBBY = 101;
	public static final int LOBBY_RANGE_END = 199;

	public static final int GAME_RANGE_START = 200;
	public static final int UPDATE_GAMESTATE = 201;
	public static final int GAME_RANGE_END = 299;

	public static final int LEADERBOARD_RANGE_START = 300;
	public static final int LEADERBOARD_RANGE_END = 399;

	public static final int CHAT_RANGE_START = 400;
	public static final int RECEIVE_MSG = 401;
	public static final int CHAT_RANGE_END = 499;

	public static final int TEST = 999;

}
