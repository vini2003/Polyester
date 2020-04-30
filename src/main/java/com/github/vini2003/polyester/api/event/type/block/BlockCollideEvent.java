package com.github.vini2003.polyester.api.event.type.block;

import com.github.vini2003.polyester.api.block.BlockInformation;
import com.github.vini2003.polyester.api.event.EventResult;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An event dispatched when a
 * block is collided with by an entity.
 */
public class BlockCollideEvent {
	public interface Listener {
		EventResult receive(World world, Entity entity, BlockInformation data);
	}

	private static final Set<Listener> LISTENERS = new HashSet<>();

	public static void register(Listener listener) {
		LISTENERS.add(listener);
	}

	public static void unregister(Listener listener) {
		LISTENERS.remove(listener);
	}

	public static EventResult dispatch(World world, Entity entity, BlockInformation data) {
		if (world.isClient()) return EventResult.CONTINUE;

		AtomicReference<EventResult> result = new AtomicReference<>(EventResult.CONTINUE);

		LISTENERS.forEach(listener -> {
			if (listener.receive(world, entity, data).isCancelled()) {
				result.set(EventResult.CANCEL);
			}
		});

		return result.get();
	}
}
