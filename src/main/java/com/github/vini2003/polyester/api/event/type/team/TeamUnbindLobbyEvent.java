package com.github.vini2003.polyester.api.event.type.team;

import com.github.vini2003.polyester.api.event.EventResult;
import com.github.vini2003.polyester.api.lobby.Lobby;
import com.github.vini2003.polyester.api.team.Team;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An event dispatched when a team
 * is unbound from a lobby.
 */
public class TeamUnbindLobbyEvent {
	public interface Listener {
		EventResult receive(Team team, Lobby lobby);
	}

	private static final Set<Listener> LISTENERS = new HashSet<>();

	public static void register(Listener listener) {
		LISTENERS.add(listener);
	}

	public static void unregister(Listener listener) {
		LISTENERS.remove(listener);
	}

	public static EventResult dispatch(Team team, Lobby lobby) {
		AtomicReference<EventResult> result = new AtomicReference<>(EventResult.CONTINUE);

		LISTENERS.forEach(listener -> {
			if (listener.receive(team, lobby).isCancelled()) {
				result.set(EventResult.CANCEL);
			}
		});

		return result.get();
	}
}
