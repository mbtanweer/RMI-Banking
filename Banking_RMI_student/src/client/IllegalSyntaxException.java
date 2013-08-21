package client;

import java.util.ArrayList;
import java.util.List;

/**
 * IllegalSyntaxException represents a grammar error. An IllegalSyntaxException
 * is thrown by class ComandParser upon detecting a syntax error.
 * 
 * @see CommandParser
 */
public class IllegalSyntaxException extends Exception {

	private List<String> errors;

	public IllegalSyntaxException() {
		errors = new ArrayList<String>();
	}

	public IllegalSyntaxException(String message) {
		super(message);
		errors = new ArrayList<String>();
	}

	/**
	 * Adds an error to the log of syntax errors.
	 */
	public void add(String error) {
		errors.add(error);
	}

	/**
	 * Retrieves the list of syntax errors detected.
	 */
	public String[] getErrors() {
		String[] result = new String[errors.size()];
		int index = 0;

		for (String error : errors) {
			result[index++] = error;
		}
		return result;
	}
}
