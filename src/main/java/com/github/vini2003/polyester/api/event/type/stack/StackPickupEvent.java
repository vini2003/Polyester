package com.github.vini2003.polyester.api.event.type.stack;

import com.github.vini2003.polyester.api.event.EventResult;
import net.minecraft.entity.ItemEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class StackPickupEvent {
	public interface Listener {
		EventResult receive(ItemEntity entity, int count);
	}

	private static final Set<Listener> LISTENERS = new HashSet<>();

	public static void register(Listener listener) {
		LISTENERS.add(listener);
	}

	public static void unregister(Listener listener) {
		LISTENERS.remove(listener);
	}

	public static EventResult dispatch(ItemEntity entity, int count) {
		AtomicReference<EventResult> result = new AtomicReference<>(EventResult.CONTINUE);

		LISTENERS.forEach(listener -> {
			if (listener.receive(entity, count).isCancelled()) {
				result.set(EventResult.CANCEL);
			}
		});

		return result.get();
	}
}