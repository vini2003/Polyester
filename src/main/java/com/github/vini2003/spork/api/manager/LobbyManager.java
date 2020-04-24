package com.github.vini2003.spork.api.manager;

import com.github.vini2003.spork.api.lobby.Lobby;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A centralized control system for
 * lobbies.
 */
public class LobbyManager {
	private final HashMap<String, Lobby> lobbies = new HashMap<>();

	private LobbyManager() {
	}

	public static final LobbyManager INSTANCE = new LobbyManager();

	/**
	 * Retrieves all lobbies managed by this manager.
	 *
	 * @return lobbies the requested lobbies.
	 */
	public Map<String, Lobby> getLobbies() {
		return lobbies;
	}

	/**
	 * Adds a lobby to this manager.
	 *
	 * @param lobby the specified lobby.
	 */
	public void add(Lobby lobby) {
		lobbies.put(lobby.getIdentifier(), lobby);
	}

	/**
	 * Removes a lobby from this manager.
	 *
	 * @param lobby the specified lobby.
	 */
	public void remove(Lobby lobby) {
		lobbies.remove(lobby.getIdentifier(), lobby);
	}

	/**
	 * Removes a lobby from this manager by its name.
	 *
	 * @param name the specified lobby's name.
	 */
	public void remove(String name) {
		lobbies.remove(name);
	}

	/**
	 * Retrieves a lobby from this manager by its name.
	 *
	 * @param name the specified lobby's name.
	 */
	public Lobby getLobby(String name) {
		return lobbies.get(name);
	}

	/**
	 * Retrieves the names of all known lobbies.
	 *
	 * @return the requested names.
	 */
	public Collection<String> getNames() {
		return lobbies.values().stream().map(Lobby::getIdentifier).collect(Collectors.toList());
	}

	/**
	 * Asserts whether a given lobby exists
	 * in this manager's lobbies by its name.
	 *
	 * @param name the specified lobby's name.
	 * @return true if yes; false if no.
	 */
	public boolean exists(String name) {
		return getLobby(name) != null;
	}

	/**
	 * Retrieves the size of the lobbies managed
	 * by this manager.
	 *
	 * @return the requested size.
	 */
	public int size() {
		return lobbies.size();
	}

	/**
	 * Propagates ticks for each lobby.
	 */
	public void tick() {
		lobbies.values().forEach(Lobby::tick);
	}
}
