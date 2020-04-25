package com.github.vini2003.spork.api.event.type.block;

import com.github.vini2003.spork.api.block.BlockData;
import com.github.vini2003.spork.api.event.EventResult;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An event dispatched when a
 * block is placed by an entity.
 */
public class BlockPlaceEvent {
	public interface Listener {
		EventResult receive(World world, Optional<LivingEntity> placer, BlockData data, ItemStack stack);
	}

	private static final Set<Listener> LISTENERS = new HashSet<>();

	public static void register(Listener listener) {
		LISTENERS.add(listener);
	}

	public static void unregister(Listener listener) {
		LISTENERS.remove(listener);
	}

	public static EventResult dispatch(World world, Optional<LivingEntity> placer, BlockData data, ItemStack stack) {
		if (world.isClient()) return EventResult.CONTINUE;

		AtomicReference<EventResult> result = new AtomicReference<>(EventResult.CONTINUE);

		LISTENERS.forEach(listener -> {
			if (listener.receive(world, placer, data, stack).isCancelled()) {
				result.set(EventResult.CANCEL);
			}
		});

		return result.get();
	}
}
