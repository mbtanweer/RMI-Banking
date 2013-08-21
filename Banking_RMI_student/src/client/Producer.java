package client;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/** 
 * Class that implements a Producer. A Producer instance is intended to be run
 * in a thread. The producer uses a parser to read commands from a data file;
 * the commands are used to populate a buffer (in this case a BlockingQueue
 * implementation). The thread that runs the producer terminates after all
 * commands have been placed in the buffer.
 */
public class Producer implements Runnable {
	
	private String fCommandFileName;
	private CommandParser fParser;
	private BlockingQueue<String[]> fQueue;

	public Producer(String commandFileName, CommandParser parser, BlockingQueue<String[]> queue) {
		this.fCommandFileName = commandFileName;
		this.fParser = parser;
		this.fQueue = queue;
	}
	
	public void run() {
		try {
			fParser.parse(fCommandFileName, 0, fQueue);
		} catch(InterruptedException e) {
			System.err.println("Producer thread interrupted while reading data file.");
		} catch(IOException e) {
			System.err.println( "Input/output exception reading from data file." );
			System.err.println( e );
		} catch(IllegalSyntaxException e) {
			System.err.println( "Input file contains errors ..." );
			
			String[] syntaxErrors = e.getErrors();
			for( int i = 0; i < syntaxErrors.length; i++ ) {
				System.err.println( syntaxErrors[ i ] ); 
			}
		}
	}
	
}
