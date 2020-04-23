package com.github.vini2003.spork.api.lobby;

public interface LobbyHolder {
	/**
	 * Retrieves the lobby that manages this holder.
	 *
	 * @return the requested lobby.
	 */
	Lobby getLobby();

	/**
	 * Binds a lobby to this holder.
	 *
	 * @param lobby the lobby to be bound.
	 */
	void bindLobby(Lobby lobby);

	/**
	 * Unbinds a lobby from this holder.
	 *
	 * @param lobby the lobby to be unbound.
	 */
	void unbindLobby(Lobby lobby);
}
