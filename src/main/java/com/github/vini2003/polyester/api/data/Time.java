package com.github.vini2003.polyester.api.data;

public class Time {
	long nanoseconds;

	private Time(long nanoseconds) {
		this.nanoseconds = nanoseconds;
	}

	public static Time of(long nanoseconds) {
		return new Time(nanoseconds);
	}

	public long nanoseconds() {
		return nanoseconds;
	}

	public long microseconds() {
		return nanoseconds / 1000;
	}

	public long milliseconds() {
		return microseconds() / 1000;
	}

	public long seconds() {
		return milliseconds() / 1000;
	}

	public long minutes() {
		return seconds() / 60;
	}

	public long hours() {
		return minutes() / 60;
	}

	public long days() {
		return hours() / 24;
	}

	public long weeks() {
		return days() / 7;
	}

	public long months() {
		return (long) (weeks() / 4.34821428571428571429);
	}

	public long years() {
		return months() / 12;
	}

	public long decades() {
		return years() / 10;
	}

	public long centuries() {
		return decades() / 10;
	}

	public long millenniums() {
		return centuries() / 10;
	}

	public long ticks() {
		return milliseconds() / 50;
	}
}
