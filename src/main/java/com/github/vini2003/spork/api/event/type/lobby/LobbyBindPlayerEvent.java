package com.github.vini2003.spork.api.event.type.lobby;

import com.github.vini2003.spork.api.entity.Player;
import com.github.vini2003.spork.api.event.EventResult;
import com.github.vini2003.spork.api.lobby.Lobby;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An event dispatched when a
 * player is bound to a lobby.
 */
public class LobbyBindPlayerEvent {
	public interface Listener {
		EventResult receive(Lobby lobby, Player player);
	}

	private static final Set<Listener> LISTENERS = new HashSet<>();

	public static void register(Listener listener) {
		LISTENERS.add(listener);
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
