package com.github.vini2003.spork.api.event.type.player;

import com.github.vini2003.spork.api.entity.Player;
import com.github.vini2003.spork.api.event.EventResult;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An event called when a
 * player is damaged.
 */
public class PlayerDamageEvent {
	public interface Listener {
		EventResult receive(Player player, DamageSource source, float amount);
	}

	private static final Set<Listener> LISTENERS = new HashSet<>();

	public static void register(Listener listener) {
		LISTENERS.add(listener);
	}

	public static void unregister(Listener listener) {
		LISTENERS.remove(listener);
	}

	public static EventResult dispatch(Player player, DamageSource source, float amount) {
		if (player.getWorld() instanceof ClientWorld) return EventResult.CONTINUE;

		AtomicReference<EventResult> result = new AtomicReference<>(EventResult.CONTINUE);

		LISTENERS.forEach(listener -> {
			if (listener.receive(player, source, amount).isCancelled()) {
				result.set(EventResult.CANCEL);
			}
		});

		return result.get();
	}
}
