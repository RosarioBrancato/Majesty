package ch.fhnw.projectbois.gameobjects;

public class GameState {

	private int id = -1;
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

}
