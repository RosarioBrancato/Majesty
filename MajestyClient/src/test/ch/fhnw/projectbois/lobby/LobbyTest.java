package test.ch.fhnw.projectbois.lobby;

import org.junit.jupiter.api.Test;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.network.Network;

class LobbyTest {

	@Test
	void testCreateLobby() {
		Request request = new Request("PLACEHOLDER TOKEN", RequestId.CREATE_LOBBY, null);
		Network.getInstance().sendRequest(request);
	}

}
