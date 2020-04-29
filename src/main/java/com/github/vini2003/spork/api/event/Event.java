package com.github.vini2003.spork.api.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class Event<T extends EventSubscriber> {
	private final List<T> listeners = new ArrayList<>();

	private final T type;
	private final Class<?>[] parameters;

	public Event(T type, Class<?>[] parameters) {
		this.type = type;
		this.parameters = parameters;
	}

	public void subscribe(T listener) {
		listeners.add(listener);
	}

	public void unsubscribe(T listener) {
		listeners.remove(listener);
	}

	public void dispatch(Object... arguments) throws Exception {
		Method method = type.getClass().getDeclaredMethod("receive", parameters);

		for (T listener : listeners) {
			method.invoke(listener, arguments);
		}
	}
}
