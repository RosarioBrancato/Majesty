package ch.fhnw.projectbois.validation;

import org.apache.commons.validator.routines.EmailValidator;

public class CredentialsValidator {
	
	public CredentialsValidator() {
		
	}
	
	public boolean stringIsAlphanumeric(String input) {
		// Minimum length of 3 alphanumeric characters
		return input.matches("^[a-zA-Z0-9_-][a-zA-Z0-9_-][a-zA-Z0-9_-]+$");
	}
	
	public boolean passwordStrenghtIsSufficient(String input) {
		// Minimum 1 uppercase, 1 lowercase, 1 numeric, 1 special, minimum length is 8 in total
		// Source: https://www.regextester.com/97402
		return input.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$^+=!*()@%&]).{8,24}$");
	}
	
	public boolean stringIsValidEmailAddress(String input) {
		return EmailValidator.getInstance().isValid(input);
	}
	
}
