package client;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.IOException;

/**
 * Class implementing a simple parser to process textual commands. This class
 * defines a template method, parse(), which defers parts of the parsing process
 * to subclasses. Essentially, subclasses define the grammar for the command
 * language and specify comment and delimiting characters. The parse() method
 * uses these subclass-defined properties to parse an input file whose content
 * should be zero or more commands that conform to the grammar.
 */
public abstract class CommandParser {

	/* A compiled form of the grammar specified by a subclass. */
	private Pattern compiledGrammar;

	/* Properties defined by a subclass. */
	private char lineComment;

	private char tokenDelimiter;

	private char commandDelimiter;

	/**
	 * Returns the set of command definitions which comprise the command
	 * language. Each command definition is represented as a regular expression.
	 * Command definitions are structured as an array of type String[ i ][ j ]
	 * where each i element represents a command and each j element represents
	 * the tokens for a command. This method must be implemented by concrete
	 * subclasses.
	 */
	protected abstract String[][] getCommands();

	/**
	 * Returns the character for use as a comment symbol. During parsing, any
	 * occurence of the comment symbol and all text that follows it on the same
	 * line is ignored. This method must be implemented by concrete subclasses.
	 */
	protected abstract char getLineComment();

	/**
	 * Returns the character used to seperate individual tokens of a command.
	 * The value returned by this method must not be a whitespace character.
	 * This method must be implemented by concrete subclasses.
	 */
	protected abstract char getTokenDelimiter();

	/**
	 * Returns the character used to separate commands. The value returned by
	 * this method must not be a whitespace character. This method must be
	 * implemented by concrete subclasses.
	 */
	protected abstract char getCommandDelimiter();

	/**
	 * Creates a CommandParse object. Part of construction involves compiling
	 * the grammar specified by the subclass actually being instantiated (note
	 * this is an abstract class).
	 * 
	 * @throws java.util.regex.PatternSyntaxException
	 *             if the grammar does not specify a valid regular expression.
	 */
	public CommandParser() throws PatternSyntaxException {
		/* Get subclass-specific grammar data. */
		lineComment = getLineComment();
		tokenDelimiter = getTokenDelimiter();
		commandDelimiter = getCommandDelimiter();

		/* Attempt to compile the grammar. */
		String grammar = buildGrammar();
		compiledGrammar = Pattern.compile(grammar);
	}

	/**
	 * Parses an input file according to a specified set of command definitions
	 * (a grammar), represented as regular expressions. The input file should
	 * contain zero or more commands that conform to the grammar and may contain
	 * arbitrary whitespace. Note: calling this method will block the calling
	 * thread when the supplied buffer argument becomes full.
	 * 
	 * @param filename
	 *            the name of a text file which contains the input to parse.
	 * @param numberOfCommandsToRead
	 *            the maximum number of valid commands to read from the input
	 *            file. If the value for this argument is zero, all commands
	 *            will be read.
	 * @param buffer
	 *            a reference to a BlockingQueue object to be used to store
	 *            commands read from the data file. Each element in the buffer
	 *            is an array of Strings, where each String represents a token 
	 *            for the command.
	 * @throws IOException
	 *             if the file named by the fileName argument is not found or if
	 *             an error is encountered when reading from the file.
	 * @throws IllegalSyntaxException
	 *             if at least one command in the input file does not conform to
	 *             the grammar.
	 * @throws InterruptedException
	 *             if the calling thread has an uncleared interrupted status 
	 *             and is then blocked by a call to parse(), or if the calling 
	 *             thread is interrupted while blocked on a call to parse().
	 */
	public void parse(String filename, int numberOfCommandsToRead, BlockingQueue<String[]> queue)
			throws IOException, IllegalSyntaxException, InterruptedException {
		/*
		 * IllegalSyntaxException is used to store details of each command,
		 * contained in the input file, whose syntax does not conform to the
		 * subclass-defined grammar.
		 */
		IllegalSyntaxException illegalSyntaxException = null;

		/*
		 * Use a LineNumberReader to open a stream to the input file.
		 * LineNumberReader is used to track the line number of the file being
		 * read - this is useful for locating syntax errors.
		 */
		LineNumberReader in = null;

		try {

			in = new LineNumberReader(new FileReader(filename));
			int input = 0;
			int numberOfCommandsRead = 0;

			/* Read each character, one at a time, from the input stream. */
			while (input != -1) {
				StringBuffer buffer = new StringBuffer();
				boolean delimited = false;
				while ((input != -1) && (!delimited)) {
					input = in.read();
					char ch = (char) input;

					if (ch == lineComment) {
						/*
						 * Skip over any input between a comment and
						 * end-of-line.
						 */
						in.readLine();
					} else {
						if (!Character.isWhitespace(ch)) {
							/* Record any non-whitespace character. */
							buffer.append(ch);
						}
						if (ch == commandDelimiter) {
							delimited = true;
						}
					}
				}

				if (input == -1) {
					/* Input stream is exhausted - break out of loop. */
					break;
				}

				/* Attempt to match the command using the grammar. */
				String command = buffer.toString();
				Matcher matcher = compiledGrammar.matcher(command);
				if (!matcher.matches()) {
					/* The command is syntactically incorrect. */
					if (illegalSyntaxException == null) {
						/* Create the IllegalSyntaxException on demand. */
						illegalSyntaxException = new IllegalSyntaxException();
					}
					/*
					 * Add a description identifying the offending command to
					 * the exception object.
					 */
					illegalSyntaxException.add(filename + ": "
							+ in.getLineNumber() + ": " + command);
				} else if (illegalSyntaxException == null) {
					/*
					 * Store the command if no syntax errors have been detected
					 * so far.
					 */
					numberOfCommandsRead++;
					
					if(numberOfCommandsRead % 100 == 0) {
						Date d = Calendar.getInstance().getTime();
					    DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
					    String toPrint = df.format(d);
						//System.out.println(toPrint + ": " + numberOfCommandsRead);
					}
					
					String[] tokens = command.split(tokenDelimiter + "|"
							+ commandDelimiter);
					queue.put(tokens);

					if (numberOfCommandsRead == numberOfCommandsToRead) {
						/* A sufficient number of commands have been read. */
						input = -1;
					}
				}
			}

			/*
			 * If syntax errors have been detected (illegalSyntaxException will
			 * be non-null) throw the exception.
			 */
			if (illegalSyntaxException != null) {
				throw illegalSyntaxException;
			}
		} catch (IOException e) {
			/* Simply propagate any IOException to the caller. */
			throw e;
		} finally {
			/*
			 * Whether an exception occurs or not, attempt to close the input
			 * stream. Note that that close() itself can throw an IOException -
			 * catch and ignore any exception thrown by close().
			 */
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/*
	 * Creates a regular expression which describes all permitted commands, i.e.
	 * the complete grammar.
	 */
	private String buildGrammar() {
		StringBuffer grammar = new StringBuffer();

		/* Get the list of commands defined in a subclass. */
		String[][] commands = getCommands();

		grammar.append("(");

		/*
		 * Iterate through the list of commands and add it to the grammar
		 * string.
		 */

		char commandDelimiter = getCommandDelimiter();
		for (int i = 0; i < commands.length; i++) {
			grammar.append("(");
			for (int j = 0; j < commands[i].length; j++) {
				grammar.append(commands[i][j]);
				grammar.append(tokenDelimiter);
			}
			/* Delete the last token delimiter. */
			grammar.deleteCharAt(grammar.length() - 1);
			grammar.append(commandDelimiter);
			grammar.append(")");
			grammar.append("|");
		}

		/* Remove last '|' character appended to grammar. */
		grammar.deleteCharAt(grammar.length() - 1);

		grammar.append(")*");

		return grammar.toString();
	}

}

