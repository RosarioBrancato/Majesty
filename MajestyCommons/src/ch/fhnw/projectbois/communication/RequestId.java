package ch.fhnw.projectbois.communication;

public class RequestId {

	public static final int EMPTY = -1;

	public static final int AUTH_RANGE_START = 0;
	public static final int LOGIN = 1;
	public static final int LOGOUT = 2;
	public static final int REGISTER = 3;
	public static final int LOAD_PROFILE = 4;
	public static final int SAVE_PROFILE = 5;
	public static final int AUTH_RANGE_END = 99;

	public static final int LOBBY_RANGE_START = 100;
	public static final int CREATE_LOBBY = 101;
	public static final int JOIN_LOBBY = 102;
	public static final int LEAVE_LOBBY = 103;
	public static final int GET_LOBBIES = 104;
	public static final int GET_LOBBY_OF_CLIENT = 105;
	public static final int EXTEND_LIFETIME_LOBBY = 107;
	public static final int DESTROY_LOBBY = 108;
	public static final int LOBBY_RANGE_END = 199;

	public static final int GAME_RANGE_START = 200;
	public static final int START_GAME = 201;
	public static final int GET_GAMESTATE = 202;
	public static final int DO_MOVE = 203;
	public static final int lEAVE_GAME = 204;
	public static final int GAME_RANGE_END = 299;

	public static final int LEADERBOARD_RANGE_START = 300;
	public static final int GET_LEADERBOARD = 301;
	public static final int LEADERBOARD_RANGE_END = 399;

	public static final int CHAT_RANGE_START = 400;
	public static final int CHAT_SEND_MSG = 401;
	public static final int CHAT_RANGE_END = 499;
	
	public static final int PROFILE_RANGE_START = 500;
	public static final int PROFILE_UPDATE = 501;
	public static final int PROFILE_DELETE = 588;
	public static final int PROFILE_RANGE_END = 599;

	public static final int TEST = 999;

}
