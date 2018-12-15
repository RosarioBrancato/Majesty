/**
 * Various methods helping to validate credentials (username, password, email) entered by users
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
	 * Password strength is sufficient as defined below.
	 * Minimum 1 uppercase, 1 lowercase, 1 numeric, 1 special, minimum length is 8 in total, maximum length is 24 in total
	 * 
	 * Source of the regex string https://www.regextester.com/97402
	 *
	 * @param input the input
	 * @return true, if the strength is suffiscient
	 */
	public boolean passwordStrengthIsSufficient(String input) {
		return input.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$^+=!*()@%&]).{8,24}$");
	}
	
	/**
	 * Checks if a string is an alphanumeric (plus "_" and "-") and contains 3 or more characters.
	 *
	 * @param input the input
	 * @return true, if string matches the requirements
	 */
	public boolean stringIsAlphanumeric(String input) {
		// Minimum length of 3 alphanumeric characters
		return input.matches("^[a-zA-Z0-9_-][a-zA-Z0-9_-][a-zA-Z0-9_-]+$");
	}
	
	/**
	 * Checks if a string is a valid email address.
	 *
	 * @param input the input
	 * @return true, if valid
	 */
	public boolean stringIsValidEmailAddress(String input) {
		return EmailValidator.getInstance().isValid(input);
	}
	
	/**
	 * Checks if the string is a plausible server address by having an alphanumeric composition of 1 to 255 characters.
	 *
	 * @param input the input
	 * @return true, if plausible
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
