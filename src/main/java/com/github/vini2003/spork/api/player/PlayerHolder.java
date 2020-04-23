package com.github.vini2003.spork.api.player;

import com.github.vini2003.spork.api.entity.Player;

import java.util.Collection;

public interface PlayerHolder {
	/**
	 * Retrieves the players managed by this holder.
	 *
	 * @return the requested players.
	 */
	Collection<Player> getPlayers();

	/**
	 * Binds a player to this holder.
	 *
	 * @param player the player to be bound.
	 */
	void bindPlayer(Player player);

	/**
	 * Unbinds a player from this holder.
	 *
	 * @param player the player to be unbound.
	 */
	void unbindPlayer(Player player);
}
