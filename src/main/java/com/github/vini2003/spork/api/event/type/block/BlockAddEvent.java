package com.github.vini2003.spork.api.event.type.block;

import com.github.vini2003.spork.api.block.BlockData;
import com.github.vini2003.spork.api.event.EventResult;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An event dispatched when a
 * block is added.
 */
public class BlockAddEvent {
	public interface Listener {
		EventResult receive(World world, BlockData data, boolean moved);
	}

	private static final Set<Listener> LISTENERS = new HashSet<>();

	public static void register(Listener listener) {
		LISTENERS.add(listener);
	}

	public static EventResult dispatch(World world, BlockData data, boolean moved) {
		if (world instanceof ClientWorld) return EventResult.CONTINUE;

		AtomicReference<EventResult> result = new AtomicReference<>(EventResult.CONTINUE);

		LISTENERS.forEach(listener -> {
			if (listener.receive(world, data, moved).isCancelled()) {
				result.set(EventResult.CANCEL);
			}
		});

		return result.get();
	}
}