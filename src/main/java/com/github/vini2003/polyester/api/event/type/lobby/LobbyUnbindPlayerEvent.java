package com.github.vini2003.polyester.api.event.type.lobby;

import com.github.vini2003.polyester.api.entity.Player;
import com.github.vini2003.polyester.api.event.EventResult;
import com.github.vini2003.polyester.api.lobby.Lobby;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An event dispatched when a
 * player is unbound from a lobby.
 */
public class LobbyUnbindPlayerEvent {
	public interface Listener {
		EventResult receive(Lobby lobby, Player player);
	}

	private static final Set<Listener> LISTENERS = new HashSet<>();

	public static void register(Listener listener) {
		LISTENERS.add(listener);
	}

	public static void unregister(Listener listener) {
		LISTENERS.remove(listener);
	}

	public static EventResult dispatch(Lobby lobby, Player player) {
		AtomicReference<EventResult> result = new AtomicReference<>(EventResult.CONTINUE);

		LISTENERS.forEach(listener -> {
			if (listener.receive(lobby, player).isCancelled()) {
				result.set(EventResult.CANCEL);
			}
		});

		return result.get();
	}
}
