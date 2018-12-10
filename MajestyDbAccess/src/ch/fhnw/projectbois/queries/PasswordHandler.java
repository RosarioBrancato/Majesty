/*
 * 
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.queries;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 
 * @author Alexandre Miccoli
 * Inspired by the example found at https://stackoverflow.com/questions/9290533/byte-to-string-and-vice-versa
 *
 */

public class PasswordHandler {
	static PasswordHandler ph = null;
	
	/**
	 * Instantiates a new password handler.
	 */
	private PasswordHandler(){}
	
	/**
	 * Gets the single instance of PasswordHandler.
	 *
	 * @return single instance of PasswordHandler
	 */
	public static PasswordHandler getInstance() {
		if(ph == null)
			ph = new PasswordHandler();
		return ph;
	}
	
	/**
	 * Gets the next salt.
	 *
	 * @return the next salt
	 */
	public String getNextSalt(){
		String output = "";
		SecureRandom rand = new SecureRandom();
		byte[] salt = new byte[16];
		rand.nextBytes(salt);
		byte[] encodedBytes = Base64.getEncoder().encode(salt);
		for (int i = 0; i < encodedBytes.length; i++) {
			output = output + Character.toString((char) encodedBytes[i]);
	    }
		return output;
	}
	
	/**
	 * Gets the hashed password.
	 *
	 * @param salt the salt
	 * @param password the password
	 * @return the hashed password
	 */
	public String getHashedPassword(String salt, String password) {
		String input = salt + password;
		
		try {
			MessageDigest msgD = MessageDigest.getInstance("SHA-256");
			byte[] result = msgD.digest(input.getBytes());
	        StringBuffer output = new StringBuffer();
	        for (int i=0; i<result.length; i++) {
	        	output.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        return output.toString();
		}catch(Exception e) {
			return "";
		}
	}
	
	/**
	 * Check matching passwords.
	 *
	 * @param enteredPassword the entered password
	 * @param expectedHash the expected hash
	 * @param salt the salt
	 * @return true, if successful
	 */
	public boolean checkMatchingPasswords(String enteredPassword, String expectedHash, String salt) {
		if(getHashedPassword(salt, enteredPassword).equals(expectedHash)) {
			return true;
		}else {
			return false;
		}
	}
	
}
