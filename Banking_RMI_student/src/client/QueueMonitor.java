package client;

import java.util.concurrent.BlockingQueue;

/**
 * Class that implements a periodic thread. A QueueMonitor periodically checks
 * whether or not a particular BlockingQueue object is empty or not. The thread
 * terminates once the queue is empty.
 */
public class QueueMonitor<T> implements Runnable {

	private static final int PERIOD = 100;
	
	private BlockingQueue<T> fQueue;
	
	public QueueMonitor(BlockingQueue<T> queue) {
		fQueue = queue;
	}
	public void run() {
		try {
			while(!fQueue.isEmpty()) {
				Thread.sleep(PERIOD);
			}
		} catch(InterruptedException e) {
			// No action necessary, enclosing thread will terminate.
		}
	}

}
