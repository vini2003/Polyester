package com.github.vini2003.polyester.api.event.type.entity;

import com.github.vini2003.polyester.api.event.EventResult;
import net.minecraft.entity.Entity;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An event dispatched when an
 * entity's dimension changes.
 */
public class EntityChangeDimensionEvent {
	public interface Listener {
		EventResult receive(Entity entity, DimensionType dimension);
	}

	private static final Set<Listener> LISTENERS = new HashSet<>();

	public static void register(Listener listener) {
		LISTENERS.add(listener);
	}

	public static void unregister(Listener listener) {
		LISTENERS.remove(listener);
	}

	public static EventResult dispatch(Entity entity, DimensionType dimension) {
		if (entity.world.isClient()) return EventResult.CONTINUE;

		AtomicReference<EventResult> result = new AtomicReference<>(EventResult.CONTINUE);

		LISTENERS.forEach(listener -> {
			if (listener.receive(entity, dimension).isCancelled()) {
				result.set(EventResult.CANCEL);
			}
		});

		return result.get();
	}
}
