package com.github.vini2003.spork.api.data;

/**
 * A Range which specifies an interval
 * from a minimum to a maximum.
 *
 * @param <T> the specified value's type.
 */
public class Range<T extends Number> {
	private final T minimum;
	private final T maximum;

	public Range(T minimum, T maximum) {
		this.minimum = minimum;
		this.maximum = maximum;
	}

	public static <T extends Number> Range<T> of(T minimum, T maximum) {
		return new Range<>(minimum, maximum);
	}

	public T getMinimum() {
		return minimum;
	}

	public T getMaximum() {
		return maximum;
	}
}
