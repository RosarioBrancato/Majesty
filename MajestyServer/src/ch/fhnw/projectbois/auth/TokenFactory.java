/**
 * Generation of authentication tokens
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.auth;

import java.util.UUID;

/**
 * 
 * @author Rosario Brancato
 *
 */
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
	 * Generate a new authentication token based on UUID (time-based and immutable identifier).
	 *
	 * @return the newely generated token
	 */
	public String getNewToken() {
		String token;

		// UUID
		token = UUID.randomUUID().toString();

		return token;
	}
}
