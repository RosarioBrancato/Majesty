/*
 * 
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.auth;

import java.util.UUID;

public class TokenFactory {

	private static TokenFactory instance = null;

	/**
	 * Gets the single instance of TokenFactory.
	 *
	 * @return single instance of TokenFactory
	 */
	public static TokenFactory getInstance() {
		if (instance == null) {
			instance = new TokenFactory();
		}
		return instance;
	}

	/**
	 * Instantiates a new token factory.
	 */
	private TokenFactory() {
	}

	/**
	 * Gets the new token.
	 *
	 * @return the new token
	 */
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
