/**
 * Various methods used to create and validate secure passwords
 * @author Alexandre Miccoli
 * Inspired by the example found at https://stackoverflow.com/questions/9290533/byte-to-string-and-vice-versa
 *
 */
package ch.fhnw.projectbois.queries;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHandler {
	static PasswordHandler ph = null;
	
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
	 * Instantiates a new password handler.
	 */
	private PasswordHandler(){}
	
	/**
	 * Check if the entered password matches the password/salt combination of the database.
	 *
	 * @param enteredPassword the entered password
	 * @param expectedHash the hashed password from the database
	 * @param salt the salt from the database
	 * @return true, if entered password matches the password/salt combination of the database
	 */
	public boolean checkMatchingPasswords(String enteredPassword, String expectedHash, String salt) {
		if(getHashedPassword(salt, enteredPassword).equals(expectedHash)) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Transforms the desired password and generated salt into a hashed string.
	 *
	 * @param salt the generated salt
	 * @param password the desired (validated) password
	 * @return the hashed password string
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
	 * Generated a random salt string.
	 * Salting passwords helps prevent dictionary brute-force attacks by prepending
	 * a random string to the password before hashing the whole string.
	 *
	 * @return a random string of 16 bytes
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
	
}
