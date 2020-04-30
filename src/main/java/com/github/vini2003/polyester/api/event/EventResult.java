package com.github.vini2003.polyester.api.event;

public enum EventResult {
	CONTINUE,
	CANCEL;

	/**
	 * Retrieves the result matching the given boolean.
	 *
	 * @param isCancelled the specified boolean.
	 * @return CANCEL if true; CONTINUE if false.
	 */
	public static EventResult of(boolean isCancelled) {
		return isCancelled ? CANCEL : CONTINUE;
	}

	/**
	 * Asserts whether this result is cancelled or not.
	 *
	 * @return true if yes; false if no.
	 */
	public boolean isCancelled() {
		return this == CANCEL;
	}
}
