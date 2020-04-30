package com.github.vini2003.polyester.api.tracker;

import com.github.vini2003.polyester.api.data.Time;
import net.minecraft.util.Tickable;

/**
 * A Tracker which tracks a given value
 * throughout time.
 *
 * @param <T> the specified value's type.
 */
public abstract class Tracker<T> implements Tickable {
	private long timeOfLastChange = 0;
	private long timeOfCreation = 0;

	private T value;

	/**
	 * Instantiates a new tracker based
	 * on a given value.
	 *
	 * @param value the specified value.
	 */
	public Tracker(T value) {
		timeOfCreation = System.nanoTime();
		setValue(value);
	}

	/**
	 * Retrieves the value of this tracker.
	 *
	 * @return the requested value.
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Sets the value of this tracker
	 * and updates the last changed time.
	 *
	 * @param value the specified value.
	 */
	public void setValue(T value) {
		this.timeOfLastChange = System.nanoTime();
		this.value = value;
	}

	/**
	 * Retrieves the time since the value
	 * was last changed.
	 *
	 * @return the requested time.
	 */
	public Time getTimeSinceLastChange() {
		return Time.of(System.nanoTime() - timeOfLastChange);
	}

	/**
	 * Retrieves the time since this value was created.
	 *
	 * @return the requested time.
	 */
	public Time getTimeSinceCreation() {
		return Time.of(System.nanoTime() - timeOfCreation);
	}

	/**
	 * Implement Tickable.
	 */
	@Override
	public void tick() {

	}
}
