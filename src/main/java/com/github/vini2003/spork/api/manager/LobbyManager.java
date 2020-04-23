package com.github.vini2003.spork.api.manager;

import com.github.vini2003.spork.api.lobby.Lobby;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * A centralized control system for
 * lobbies.
 */
public class LobbyManager {
	private static final HashMap<String, Lobby> lobbies = new HashMap<>();

	/**
	 * Adds a lobby to this manager.
	 *
	 * @param lobby the specified lobby.
	 */
	public static void add(Lobby lobby) {
		lobbies.put(lobby.getIdentifier(), lobby);
	}

	/**
	 * Removes a lobby from this manager.
	 *
	 * @param lobby the specified lobby.
	 */
	public static void remove(Lobby lobby) {
		lobbies.remove(lobby.getIdentifier(), lobby);
	}

	/**
	 * Removes a lobby from this manager by its name.
	 *
	 * @param name the specified lobby's name.
	 */
	public static void remove(String name) {
		lobbies.remove(name);
	}

	/**
	 * Retrieves a lobby from this manager by its name.
	 *
	 * @param name the specified lobby's name.
	 */
	public static Lobby getLobby(String name) {
		return lobbies.get(name);
	}

	/**
	 * Retrieves the names of all known lobbies.
	 *
	 * @return the requested names.
	 */
	public static Collection<String> getNames() {
		return lobbies.values().stream().map(Lobby::getIdentifier).collect(Collectors.toList());
	}

	/**
	 * Asserts whether a given lobby exists
	 * in this manager's lobbies by its name.
	 *
	 * @param name the specified lobby's name.
	 * @return true if yes; false if no.
	 */
	public static boolean exists(String name) {
		return getLobby(name) != null;
	}

	/**
	 * Retrieves the size of the lobbies managed
	 * by this manager.
	 *
	 * @return the requested size.
	 */
	public static int size() {
		return lobbies.size();
	}

	/**
	 * Propagates ticks for each lobby.
	 */
	public static void tick() {
		lobbies.values().forEach(Lobby::tick);
	}
}
