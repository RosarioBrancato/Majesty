/*
 * 
 * @author Alexandre Miccoli
 * 
 */
package ch.fhnw.projectbois.validation;

import org.apache.commons.validator.routines.EmailValidator;

public class CredentialsValidator {
	static CredentialsValidator cv;
	
	/**
	 * Gets the single instance of CredentialsValidator.
	 *
	 * @return single instance of CredentialsValidator
	 */
	public static CredentialsValidator getInstance() {
		if(cv == null) {
			cv = new CredentialsValidator();
		}
		return cv;
	}
	
	/**
	 * Instantiates a new credentials validator.
	 */
	private CredentialsValidator() {}
	
	/**
	 * Password strenght is sufficient.
	 *
	 * @param input the input
	 * @return true, if successful
	 */
	public boolean passwordStrenghtIsSufficient(String input) {
		// Minimum 1 uppercase, 1 lowercase, 1 numeric, 1 special, minimum length is 8 in total
		// Source: https://www.regextester.com/97402
		return input.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$^+=!*()@%&]).{8,24}$");
	}
	
	/**
	 * String is alphanumeric.
	 *
	 * @param input the input
	 * @return true, if successful
	 */
	public boolean stringIsAlphanumeric(String input) {
		// Minimum length of 3 alphanumeric characters
		return input.matches("^[a-zA-Z0-9_-][a-zA-Z0-9_-][a-zA-Z0-9_-]+$");
	}
	
	/**
	 * String is valid email address.
	 *
	 * @param input the input
	 * @return true, if successful
	 */
	public boolean stringIsValidEmailAddress(String input) {
		return EmailValidator.getInstance().isValid(input);
	}
	
	/**
	 * String is valid server address.
	 *
	 * @param input the input
	 * @return true, if successful
	 */
	public boolean stringIsValidServerAddress(String input) {
		// Alphanumeric between 1 and 255 characters, "." allowed excepted at start and end of string
		boolean response = (input.matches("^[a-zA-Z0-9.]+$") && input.length()>0 && input.length()<256);
		if(response) 
			response = !Character.toString(input.charAt(input.length()-1)).equals(".");
		if(response) 
			response = !Character.toString(input.charAt(0)).equals(".");
		return response;
	}
	
}
