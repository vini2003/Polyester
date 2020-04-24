package com.github.vini2003.spork.api.event.type.team;

import com.github.vini2003.spork.api.entity.Player;
import com.github.vini2003.spork.api.event.EventResult;
import com.github.vini2003.spork.api.team.Team;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An event dispatched when a player
 * is bound to a team.
 */
public class TeamBindPlayerEvent {
	public interface Listener {
		EventResult receive(Team team, Player player);
	}

	private static final Set<Listener> LISTENERS = new HashSet<>();

	public static void register(Listener listener) {
		LISTENERS.add(listener);
	}

	public static void unregister(Listener listener) {
		LISTENERS.remove(listener);
	}

	public static EventResult dispatch(Team team, Player player) {
		AtomicReference<EventResult> result = new AtomicReference<>(EventResult.CONTINUE);

		LISTENERS.forEach(listener -> {
			if (listener.receive(team, player).isCancelled()) {
				result.set(EventResult.CANCEL);
			}
		});

		return result.get();
	}
}
