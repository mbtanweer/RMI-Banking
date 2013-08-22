package client;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

import common.BankAccount;
import common.ExcessiveAmountException;
import common.IllegalMoneyException;
import common.Money;
import common.NegativeAmountException;

/**
 * Class whose instances are intended to be run in separate threads. A Worker is
 * responsible for retrieving commands from a BlockingQueue object and for
 * making appropriate RMI calls on remote BankAccount objects.
 */
public class Worker implements Runnable {

	/* BlockingQueue object from where commands are retrieved. */
	private BlockingQueue<String[]> fQueue;
	private Hashtable<String, Semaphore> fs;

	/*
	 * Hashtable used to store BankAccount proxy objects. The key is the account
	 * number.
	 */
	private Hashtable<String, BankAccount> fAccounts;

	/**
	 * Creates a Worker instance.
	 */
	public Worker(BlockingQueue<String[]> queue,
			Hashtable<String, BankAccount> accounts, Hashtable<String, Semaphore> s) {
		this.fQueue = queue;
		this.fAccounts = accounts;
		this.fs = s;
	}

	/**
	 * Until interrupted, iteratively processes commands held in the
	 * BlockingQueue. The Worker can be blocked when the queue is empty.
	 * Processing each command involves a RMI call to the remote BankAccount
	 * object to which the command applies.
	 */
	public void run() {
		boolean finished = false;
		while (!finished) {
			try {
				// Retrieve command to process.
				String[] command = fQueue.take();

				// Process command.
				processCommand(command);
			} catch (InterruptedException e) {
				finished = true;
			}
		}
	}

	/*
	 * Implementation method that interprets a command and which makes the
	 * necessary RMI call.
	 */
	private void processCommand(String[] commandTokens) {

		BankAccount bAccount = fAccounts.get(commandTokens[1]);
		try {
			fs.get(commandTokens[1]).acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			switch (commandTokens[0]) {
//			case "balance":
//				break;
//			case "name":
//				break;
			case "deposit":
				bAccount.deposit(new Money(commandTokens[2], commandTokens[3]));
				break;
			case "withdraw":
				bAccount.withdraw(new Money(commandTokens[2], commandTokens[3]));
				break;
			default:
				break;
			}

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalMoneyException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (NegativeAmountException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (ExcessiveAmountException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		finally{
			
		
		fs.get(commandTokens[1]).release();}
		return;

		// === YOUR CODE HERE ===

	}
}
