package com.github.vini2003.spork.api.queue;

import com.github.vini2003.spork.api.lobby.Lobby;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface QueueHolder {
	/**
	 * Retrieves the action queue managed by this holder.
	 *
	 * @return the requested queue.
	 */
	Map<Predicate<Lobby>, Consumer<Lobby>> getQueue();

	/**
	 * Enqueues an action to be performed when a given predicate
	 * is matched.
	 *
	 * @param predicate predicated to match.
	 * @param action    action to perform.
	 */
	default void enqueueAction(Predicate<Lobby> predicate, Consumer<Lobby> action) {
		getQueue().put(predicate, action);
	}

	/**
	 * Unqueues a queued action, based on its predicate.
	 *
	 * @param predicate action to be unqueued.
	 */
	default void unqueueAction(Predicate<Lobby> predicate) {
		getQueue().remove(predicate);
	}

	/**
	 * Unqueues a queued action, based on its predicate and action.
	 *
	 * @param predicate predicate of action to be removed.
	 * @param action    action to be removed.
	 */
	default void unqueueAction(Predicate<Lobby> predicate, Consumer<Lobby> action) {
		getQueue().remove(predicate, action);
	}

	/**
	 * Unqueues all queued actions.
	 */
	default void unqueueAllActions() {
		getQueue().clear();
	}
}
