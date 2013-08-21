package client;

/**
 * A class which defines a grammar for commands that operate on bank accounts.
 */
public class BankCommandParser extends CommandParser {

	/* Comment symbol. */
	private static final char COMMENT = '!';

	/* Command and token delimiter symbols. */
	private static final char COMMAND_DELIMITER = ';';

	private static final char TOKEN_DELIMITER = ',';

	/* Account numbers are 8 digit strings. */
	private static final String ACCOUNT_NUMBER = "\\d{8}";

	/*
	 * Amounts of money are specified as: an optional - sign, one or more
	 * digits, the token delimiter symbol (i.e. comma), and one or two digits.
	 */
	private static final String MONEY = "-?\\d+" + TOKEN_DELIMITER + "\\d{1,2}";

	/*
	 * 4 commands are defined, each structured using tokens. All commands have a
	 * name (e.g. balance) indicated by the first token of each command. The
	 * "name" and "balance" commands each take one additional token which is the
	 * account number to query. The "deposit" and "withdraw" commands each have
	 * an account number token plus an amount of money.
	 */
	private static final String[] NAME_CMD = { "name", ACCOUNT_NUMBER };

	private static final String[] BALANCE_CMD = { "balance", ACCOUNT_NUMBER };

	private static final String[] DEPOSIT_CMD = { "deposit", ACCOUNT_NUMBER,
			MONEY };

	private static final String[] WITHDRAW_CMD = { "withdraw", ACCOUNT_NUMBER,
			MONEY };

	private static final String[][] allCommands = { NAME_CMD, BALANCE_CMD,
			DEPOSIT_CMD, WITHDRAW_CMD };

	/**
	 * Returns a two-dimensional structure representing the command definitions
	 * for banking operations.
	 * 
	 * @see CommandParser
	 */
	protected String[][] getCommands() {
		return allCommands;
	}

	/**
	 * @see CommandParser
	 */
	protected char getLineComment() {
		return COMMENT;
	}

	/**
	 * @see CommandParser
	 */
	protected char getTokenDelimiter() {
		return TOKEN_DELIMITER;
	}

	/**
	 * @see CommandParser
	 */
	protected char getCommandDelimiter() {
		return COMMAND_DELIMITER;
	}
}

