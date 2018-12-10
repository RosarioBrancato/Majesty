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
	
	private PasswordHandler(){}
	
	public static PasswordHandler getInstance() {
		if(ph == null)
			ph = new PasswordHandler();
		return ph;
	}
	
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
	
	public boolean checkMatchingPasswords(String enteredPassword, String expectedHash, String salt) {
		if(getHashedPassword(salt, enteredPassword).equals(expectedHash)) {
			return true;
		}else {
			return false;
		}
	}
	
}
