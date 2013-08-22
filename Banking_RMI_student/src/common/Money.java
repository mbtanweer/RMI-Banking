package common;

import java.io.Serializable;

/**
 * Class to represent a currency based on dollars and cents. The value of a
 * Money object comprises a value for dollars and a value for cents.
 */
public class Money implements Comparable<Money>, Serializable {

	/* Amount of money, represented as cents, in a Money instance. */
	private int fCents;

	/**
	 * Creates a Money object whose value is zero. In other words, the Money
	 * object will have a value of 0 dollars and 0 cents.
	 */
	public Money() {
		fCents = 0;
	}

	/**
	 * Creates a Money object whose value is specified by the arguments: dollars
	 * and cents. Using this constructor, a Money object with a negative value
	 * can be created be specifying a negative amount for the dollars argument.
	 * 
	 * @param dollars
	 *            the value in dollars for the Money object.
	 * @param cents
	 *            the value in cents for the Money object.
	 * @throws IllegalMoneyException
	 *             if the value for cents evaluates < 0 or > 99.
	 * @throws NumberFormatException
	 *             if either argument evaluates to a non-integer.
	 */
	public Money(String dollars, String cents) throws IllegalMoneyException,
			NumberFormatException {
		this(Integer.parseInt(dollars), Integer.parseInt(cents));
	}

	public Money(int dollars, int cents) throws IllegalMoneyException {
		if (cents < 0 || cents > 99) {
			throw new IllegalMoneyException();
		}

		if (dollars < 0) {
			this.fCents = dollars * 100 - cents;
		} else {
			this.fCents = dollars * 100 + cents;
		}
	}

	/**
	 * A copy-constructor that creates a new copy of the Money object passed in
	 * as the argument to this method.
	 */
	public Money(Money money) {
		this.fCents = money.fCents;
	}

	/**
	 * Returns the dollars value from a Money instance. If the Money object
	 * represents a negative amount of money, the value returned from this
	 * method will be negative.
	 */
	public int getDollars() {
		int result = fCents / 100;

		if (result == -0) {
			result = 0;
		}
		return result;
	}

	/**
	 * Returns the cents value from a Money instance. If the Money object
	 * represents a negative amount of money, the value returned from this
	 * method will be negative.
	 */
	public int getCents() {
		return fCents % 100;
	}

	/**
	 * Returns true if this Money object represents a negative amount of money,
	 * false otherwise.
	 */
	public boolean isNegative() {
		return fCents < 0;
	}

	/**
	 * Adds the amount of money represented by the otherMoney argument to this
	 * Money object.
	 */
	public void add(Money other) {
		this.fCents += other.fCents;
	}

	/**
	 * Subtracts the amount of money represented by the otherMoney argument from
	 * this Money object.
	 */
	public void subtract(Money other) {
		this.fCents -= other.fCents;
	}

	/**
	 * Returns a string representation of this Money object.
	 */
	public String toString() {
		StringBuffer description = new StringBuffer();

		description.append('[');

		/* Append '-' for negative amounts of money. */
		if (fCents < 0) {
			description.append('-');
		}

		description.append(Integer.toString(Math.abs(this.getDollars())));
		description.append('.');

		/* Append leading zero to the cents part of the string if necessary. */
		int centsPart = this.getCents();
		if (centsPart < 10 && centsPart > -10) {
			description.append('0');
		}

		description.append(Integer.toString(Math.abs(centsPart)));
		description.append(']');

		return description.toString();
	}

	/**
	 * Returns true if the value of the object argument is the same as the value
	 * of this Money object; false otherwise. The object argument must refer to
	 * a Money object that represents the same amount of money as this Money
	 * object for this method to return true.
	 */
	public boolean equals(Object object) {
		boolean result = false;

		if (object instanceof Money) {
			result = this.fCents == ((Money) object).getCents();
		}
		return result;
	}

	/**
	 * Returns a separate copy of this Money object.
	 */
	public Object clone() throws CloneNotSupportedException {
		return new Money(this);
	}

	/**
	 * Implements the Comparable interface.
	 */
	public int compareTo(Money other) {
		if (this.fCents < other.fCents) {
			return -1;
		} else if (this.fCents > other.fCents) {
			return 1;
		} else {
			return 0;
		}
	}
}
