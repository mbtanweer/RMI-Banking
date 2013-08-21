package common;


/**
 * Class of exception to represent a negative sum of money. A
 * NegativeAmountException is thrown by BankAccount objects.
 * 
 * @see BankAccount
 */
public class NegativeAmountException extends Exception {

	public NegativeAmountException() {
		super();
	}

	public NegativeAmountException(String message) {
		super(message);
	}

}
