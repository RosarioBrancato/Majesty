package ch.fhnw.projectbois.auth;

import java.util.UUID;

public class TokenFactory {

	private static TokenFactory instance = null;

	private TokenFactory() {
	}

	public static TokenFactory getInstance() {
		if (instance == null) {
			instance = new TokenFactory();
		}
		return instance;
	}

	public String getNewToken() {
		String token;

		// bytes
//		SecureRandom random = new SecureRandom();
//		byte bytes[] = new byte[20];
//		random.nextBytes(bytes);
//		token = bytes.toString();

		// UUID
		token = UUID.randomUUID().toString();

		return token;
	}
}
