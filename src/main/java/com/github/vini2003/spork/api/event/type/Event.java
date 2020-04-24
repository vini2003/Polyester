package com.github.vini2003.spork.api.event.type;

import java.util.HashSet;
import java.util.Set;

public abstract class Event<T> {
	private final Set<T> listeners = new HashSet<>();

	public Set<T> getListeners() {
		return listeners;
	}

	public void listen(T listener) {
		listeners.add(listener);
	}

	public void mute(T listener) {
		listeners.remove(listener);
	}
}
