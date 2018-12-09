package ch.fhnw.projectbois.gameobjects;

public class GameState {

	private int id = -1;
	private boolean isCardSideA = true;

	private int round = 0;
	private int playersTurn = -1;
	private int startPlayerIndex = -1;
	private boolean gameEnded = false;

	private int turntimer = -1;

	private Board board;

	public GameState() {
		this.board = new Board();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Board getBoard() {
		return this.board;
	}

	public boolean isCardSideA() {
		return isCardSideA;
	}

	public void setCardSideA(boolean isCardSideA) {
		this.isCardSideA = isCardSideA;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getPlayersTurn() {
		return playersTurn;
	}

	public void setPlayersTurn(int playersTurn) {
		this.playersTurn = playersTurn;
	}

	public int getStartPlayerIndex() {
		return startPlayerIndex;
	}

	public void setStartPlayerIndex(int startPlayerIndex) {
		this.startPlayerIndex = startPlayerIndex;
	}

	public boolean isGameEnded() {
		return gameEnded;
	}

	public void setGameEnded(boolean gameEnded) {
		this.gameEnded = gameEnded;
	}

	public int getTurntimer() {
		return turntimer;
	}

	public void setTurntimer(int turntimer) {
		this.turntimer = turntimer;
	}

}
