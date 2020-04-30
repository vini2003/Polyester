package com.github.vini2003.polyester.api.lobby;

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
	 * Unbinds the lobby from this holder.
	 */
	void unbindLobby();
}
