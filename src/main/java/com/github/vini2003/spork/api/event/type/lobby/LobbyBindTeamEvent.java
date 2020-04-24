package com.github.vini2003.spork.api.event.type.lobby;

import com.github.vini2003.spork.api.event.EventResult;
import com.github.vini2003.spork.api.lobby.Lobby;
import com.github.vini2003.spork.api.team.Team;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An event dispatched when a
 * team is bound to a lobby.
 */
public class LobbyBindTeamEvent {
	public interface Listener {
		EventResult receive(Lobby lobby, Team team);
	}

	private static final Set<Listener> LISTENERS = new HashSet<>();

	public static void register(Listener listener) {
		LISTENERS.add(listener);
	}

	public static void unregister(Listener listener) {
		LISTENERS.remove(listener);
	}

	public static EventResult dispatch(Lobby lobby, Team team) {
		AtomicReference<EventResult> result = new AtomicReference<>(EventResult.CONTINUE);

		LISTENERS.forEach(listener -> {
			if (listener.receive(lobby, team).isCancelled()) {
				result.set(EventResult.CANCEL);
			}
		});

		return result.get();
	}
}
