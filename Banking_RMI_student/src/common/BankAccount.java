package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface representing remotely accessible BankAccount objects.
 */
public interface BankAccount extends Remote {

	/**
	 * Returns the balance of a BankAccount instance.
	 */
	public Money getBalance() throws RemoteException;

	/**
	 * Returns the BankAccount owner's name.
	 */
	public String getName() throws RemoteException;

	/**
	 * Returns the BankAccount's unique number.
	 */
	public String getNumber() throws RemoteException;

	/**
	 * Attempts to deposit a sum of Money into a BankAccount object. If 
	 * successful the BankAccount's balance is updated.
	 * @param amount the amount of money to deposit.
	 * @throws NegativeAmountException if the amount of money to deposit is
	 * negative.
	 */
	public void deposit(Money amount) throws RemoteException,
			NegativeAmountException;

	/**
	 * Attempts to withdraw a sum of money from a BankAccount.
	 * @param amount the amount of money to withdraw.
	 * @throws NegativeAmountException if the amount of money to withdraw is
	 * negative.
	 * @throws ExcessiveAmountException if the amount of money to withdraw 
	 * exceeds some threshold associated with the account.
	 */
	public void withdraw(Money amount) throws RemoteException,
			NegativeAmountException, ExcessiveAmountException;
	
}
