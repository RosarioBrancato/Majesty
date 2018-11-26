package test.ch.fhnw.projectbois.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GameLogicTest {

	@Test
	void testStartPlayerIndex() {
		int startPlayerIndex = (int)Math.round(Math.random() * 3);
		System.out.println("startPlayerIndex: " + startPlayerIndex);
		assertTrue(startPlayerIndex >= 0 && startPlayerIndex < 4);

		startPlayerIndex = (int)Math.round(Math.random() * 3);
		System.out.println("startPlayerIndex: " + startPlayerIndex);
		assertTrue(startPlayerIndex >= 0 && startPlayerIndex < 4);

		startPlayerIndex = (int)Math.round(Math.random() * 3);
		System.out.println("startPlayerIndex: " + startPlayerIndex);
		assertTrue(startPlayerIndex >= 0 && startPlayerIndex < 4);

		startPlayerIndex = (int)Math.round(Math.random() * 3);
		System.out.println("startPlayerIndex: " + startPlayerIndex);
		assertTrue(startPlayerIndex >= 0 && startPlayerIndex < 4);
	}

}
