package common;


/**
 * Exception class to represent an excessive amount of money. An
 * ExcessiveAmountException is thrown can be thrown by class BankAccount's
 * withdraw method.
 * 
 * @see BankAccount
 */
public class ExcessiveAmountException extends Exception {

	public ExcessiveAmountException() {
		super();
	}

	public ExcessiveAmountException(String message) {
		super(message);
	}
}
