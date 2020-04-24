package com.github.vini2003.spork.api.event.type.player;

import com.github.vini2003.spork.api.entity.Player;
import com.github.vini2003.spork.api.event.EventResult;
import net.minecraft.client.world.ClientWorld;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An event dispatched when a
 * client connects to this server.
 */
public class PlayerConnectEvent {
	public interface Listener {
		EventResult receive(Player player);
	}

	private static final Set<Listener> LISTENERS = new HashSet<>();

	public static void register(Listener listener) {
		LISTENERS.add(listener);
	}

	public static void unregister(Listener listener) {
		LISTENERS.remove(listener);
	}

	public static EventResult dispatch(Player player) {
		if (player.getWorld() instanceof ClientWorld) return EventResult.CONTINUE;

		AtomicReference<EventResult> result = new AtomicReference<>(EventResult.CONTINUE);

		LISTENERS.forEach(listener -> {
			if (listener.receive(player).isCancelled()) {
				result.set(EventResult.CANCEL);
			}
		});

		return result.get();
	}
}
