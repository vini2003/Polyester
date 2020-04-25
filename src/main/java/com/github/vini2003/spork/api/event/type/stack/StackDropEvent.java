package com.github.vini2003.spork.api.event.type.stack;

import com.github.vini2003.spork.api.event.EventResult;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class StackDropEvent {
	public interface Listener {
		EventResult receive(Entity entity, ItemStack stack);
	}

	private static final Set<Listener> LISTENERS = new HashSet<>();

	public static void register(Listener listener) {
		LISTENERS.add(listener);
	}

	public static void unregister(Listener listener) {
		LISTENERS.remove(listener);
	}

	public static EventResult dispatch(Entity entity, ItemStack stack) {
		AtomicReference<EventResult> result = new AtomicReference<>(EventResult.CONTINUE);

		LISTENERS.forEach(listener -> {
			if (listener.receive(entity, stack).isCancelled()) {
				result.set(EventResult.CANCEL);
			}
		});

		return result.get();
	}
}
