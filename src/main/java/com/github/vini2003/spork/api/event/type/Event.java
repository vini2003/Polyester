package com.github.vini2003.spork.api.event.type;

import java.util.*;

public abstract class Event<T> {
	private final List<T> listeners = new ArrayList<>();

	public Collection<T> getListeners() {
		return listeners;
	}

	public void listen(T listener) {
		listeners.add(listener);
	}

	public void mute(T listener) {
		listeners.remove(listener);
	}
}
