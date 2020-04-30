package com.github.vini2003.polyester.api.event.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
