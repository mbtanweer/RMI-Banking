package common;


/**
 * An IllegalMoneyException may be thrown by class Money at instantiation time.
 * 
 * @see Money
 */
public class IllegalMoneyException extends Exception {

	public IllegalMoneyException() {
		super();
	}

	public IllegalMoneyException(String message) {
		super(message);
	}
}
