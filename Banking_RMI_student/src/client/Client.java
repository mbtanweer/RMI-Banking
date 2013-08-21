package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import java.util.regex.PatternSyntaxException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Hashtable;

import common.BankAccount;
import common.Money;

/**
 * Class to represent a client process in a RMI banking application. The client
 * loads a set of account transactions from a data file and makes RMI calls on
 * corresponding remote account objects. The client is multi-threaded.
 */
public class Client {

	public static void main(String[] args) {

		final int THREAD_POOL_SIZE = 5;

		try {
			/* Check command line arguments. */
			if (args.length != 3) {
				System.err
						.println("Usage: Client <input-filename> <registry-host> <registry-port>");
				System.exit(1);
			}

			long start = System.currentTimeMillis();

			/* Lookup remote BankAccount objects. */
			Hashtable<String, BankAccount> accounts = lookupRemoteAccounts(args[1], args[2]);
			if (accounts.isEmpty()) {
				System.err
						.println("Unable to acquire proxy objects, program terminating.");
				System.exit(1);
			}

			long acquiredProxies = System.currentTimeMillis();
			System.out.println("Time taken to acquire proxies: "
					+ (acquiredProxies - start) / 1000.0 + "sec.");

			/*
			 * Create a parser and queue to store commands read from the data
			 * file.
			 */
			BankCommandParser parser = new BankCommandParser();
			BlockingQueue<String[]> queue = new ArrayBlockingQueue<String[]>(
					100);

			/* Create a pool of worker (consumer) threads to process the queue. */
			List<Thread> workers = new ArrayList<Thread>();
			for (int i = 0; i < THREAD_POOL_SIZE; i++) {
				Thread thread = new Thread(new Worker(queue, accounts));
				thread.start();
				workers.add(thread);
			}

			/* Start a producer thread that deposits commands into the queue. */
			Thread producerThread = new Thread(new Producer(args[0], parser,
					queue));
			producerThread.start();

			/* Wait for the producer thread to finish. */
			producerThread.join();
			System.out.println("Producer thread finished ...");
			
			/* Start a thread to monitor the queue and wait for it to become empty. */
			Thread queueMonitorThread = new Thread(new QueueMonitor<String[]>(queue));
			queueMonitorThread.start();
			queueMonitorThread.join();
			System.out.println("Buffer is now empty ...");

			/* Ask the workers to terminate gracefully. */
			for (Thread t : workers) {
				t.interrupt();
			}

			/* Wait for the workers to terminate. */
			for (Thread t : workers) {
				t.join();
			}
			System.out.println("Worker threads finished ...");

			/* Request final balances. */
			Enumeration<String> e = accounts.keys();
			BankAccount account = null;
			while (e.hasMoreElements()) {
				String accountNumber = e.nextElement();
				account = accounts.get(accountNumber);
				Money balance = account.getBalance();
				System.out.println(accountNumber + ": " + balance);
			}

			long stop = System.currentTimeMillis();
			System.out.println("Total elapsed time: " + (stop - start) / 1000.0
					+ "sec.");
		} catch (PatternSyntaxException e) {
			System.err.println("Grammar is invalid ...");
			System.err.println(e);
			System.exit(1);
		} catch (InterruptedException e) {
			System.err
					.println("Main application thread interrupted unexpectedly.");
		} catch (RemoteException e) {
			System.err.println("Communication error.");
		}
	}

	/**
	 * Returns a hashtable of <account-number, BankAccount proxy object> pairs.
	 * The hashtable contains one entry for each remotely accessible BankAccount
	 * object registered with the RMI Registry.
	 * 
	 * @param registryHost
	 *            the name of the host machine on which the RMI Registry is
	 *            expected to be running.
	 * @param registryPort
	 * 			  the port the RMI Registry is using to listen for incoming
	 *            invocations.
	 */
	private static Hashtable<String, BankAccount> lookupRemoteAccounts(
			String registryHost, String registryPort) {
		Hashtable<String, BankAccount> accounts = new Hashtable<String, BankAccount>();

		// === YOUR CODE HERE ===
		return null;
	}
}