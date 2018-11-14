package test.ch.fhnw.projectbois.general;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.general.IdFactory;

class IdFactoryTest {

	@Test
	void testNewId() {
		LobbyDTO l1 = new LobbyDTO();
		l1.setId(IdFactory.getInstance().getNewId(LobbyDTO.class.getName()));

		LobbyDTO l2 = new LobbyDTO();
		l2.setId(IdFactory.getInstance().getNewId(LobbyDTO.class.getName()));

		assertEquals(l1.getId(), 1);
		assertEquals(l2.getId(), 2);
	}

}
