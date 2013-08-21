package client;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;

import common.BankAccount;
import common.ExcessiveAmountException;
import common.IllegalMoneyException;
import common.Money;
import common.NegativeAmountException;

/**
 * Class whose instances are intended to be run in separate threads. A Worker
 * is responsible for retrieving commands from a BlockingQueue object and for
 * making appropriate RMI calls on remote BankAccount objects. 
 */
public class Worker implements Runnable {

	/* BlockingQueue object from where commands are retrieved. */
	private BlockingQueue<String[]> fQueue;

	/* 
	 * Hashtable used to store BankAccount proxy objects. The key is the 
	 * account number.
	 */
	private Hashtable<String,BankAccount> fAccounts;

	/**
	 * Creates a Worker instance.
	 */
	public Worker(BlockingQueue<String[]> queue, Hashtable<String,BankAccount> accounts) {
		this.fQueue = queue;
		this.fAccounts = accounts;
	}

	/**
	 * Until interrupted, iteratively processes commands held in the 
	 * BlockingQueue. The Worker can be blocked when the queue is empty. 
	 * Processing each command involves a RMI call to the remote BankAccount
	 * object to which the command applies. 
	 */
	public void run() {
		boolean finished = false;
		while(!finished) {
			try {
				// Retrieve command to process.
				String[] command = fQueue.take();

				// Process command.
				processCommand(command);
			} catch(InterruptedException e) {
				finished = true;
			}
		}
	}

	/*
	 * Implementation method that interprets a command and which makes the 
	 * necessary RMI call.
	 */
	private void processCommand(String[] commandTokens) {
		// === YOUR CODE HERE ===
	}
}
