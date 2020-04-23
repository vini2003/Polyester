package com.github.vini2003.spork.api.event.type.logic;

import com.github.vini2003.spork.api.event.EventResult;
import net.minecraft.server.MinecraftServer;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An event dispatched when
 * this server is started.
 */
public class ServerStartEvent {
	public interface Listener {
		EventResult receive(MinecraftServer server);
	}

	private static final Set<Listener> LISTENERS = new HashSet<>();

	public static void register(Listener listener) {
		LISTENERS.add(listener);
	}

	public static EventResult dispatch(MinecraftServer server) {
		AtomicReference<EventResult> result = new AtomicReference<>(EventResult.CONTINUE);

		LISTENERS.forEach(listener -> {
			if (listener.receive(server).isCancelled()) {
				result.set(EventResult.CANCEL);
			}
		});

		return result.get();
	}
}
