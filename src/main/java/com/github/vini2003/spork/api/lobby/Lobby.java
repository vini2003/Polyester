package com.github.vini2003.spork.api.lobby;

import com.github.vini2003.spork.api.data.Tracker;
import com.github.vini2003.spork.api.data.TrackerHolder;
import com.github.vini2003.spork.api.entity.Player;
import com.github.vini2003.spork.api.event.type.Event;
import com.github.vini2003.spork.api.event.type.lobby.LobbyBindPlayerEvent;
import com.github.vini2003.spork.api.event.type.lobby.LobbyBindTeamEvent;
import com.github.vini2003.spork.api.event.type.lobby.LobbyUnbindPlayerEvent;
import com.github.vini2003.spork.api.event.type.lobby.LobbyUnbindTeamEvent;
import com.github.vini2003.spork.api.player.PlayerHolder;
import com.github.vini2003.spork.api.preset.Preset;
import com.github.vini2003.spork.api.preset.PresetHolder;
import com.github.vini2003.spork.api.queue.QueueHolder;
import com.github.vini2003.spork.api.team.Team;
import com.github.vini2003.spork.api.team.TeamHolder;
import com.github.vini2003.spork.api.world.DimensionHolder;
import com.github.vini2003.spork.api.world.WorldHolder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.world.dimension.DimensionType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A lobby is a centralized control system
 * for managing a group of players.
 * <p>
 * It is responsible for dealing with
 * scheduled events for the given
 * player group.
 */
public class Lobby implements QueueHolder, WorldHolder, DimensionHolder, PresetHolder, PlayerHolder, TeamHolder, TrackerHolder, Tickable {
	private Identifier identifier;

	private DimensionType dimension;

	private ServerWorld world;

	private Preset preset;

	private final List<Team> teams = new ArrayList<>();

	private final List<Player> players = new ArrayList<>();

	private final Map<Object, Tracker<?>> trackers = new HashMap<>();

	private final Map<Predicate<Lobby>, Consumer<Lobby>> queue = new ConcurrentHashMap<>();

	private final Map<Class<? extends Event<?>>, Event<?>> events = new HashMap<>();

	private final Tracker<Integer> time = new Tracker<Integer>(0) {
		@Override
		public void tick() {
			setValue(getValue() + 1);
		}
	};

	public Lobby(Identifier identifier) {
		setIdentifier(identifier);
		trackers.put(identifier + "_time", time);
	}

	public Tracker<Integer> getTime() {
		return time;
	}

	/**
	 * Retrieve this lobby's size.
	 *
	 * @return the requested size.
	 */
	public int size() {
		return players.size();
	}

	/**
	 * Retrieves this lobby's identifier.
	 *
	 * @return the requested identifier.
	 */
	public Identifier getIdentifier() {
		return identifier;
	}

	/**
	 * Sets this lobby's identifier.
	 *
	 * @param identifier the specified identifier.
	 */
	public void setIdentifier(Identifier identifier) {
		this.identifier = identifier;
	}

	/**
	 * Asserts whether this lobby has a bound preset or not.
	 *
	 * @return true if yes; false if no.
	 */
	public boolean hasPreset() {
		return getPreset() != null;
	}

	/**
	 * Asserts whether this lobby has a bound world or not.
	 *
	 * @return true if yes; false if no.
	 */
	public boolean hasWorld() {
		return getWorld() != null;
	}

	/**
	 * Asserts whether this lobby has a bound dimension or not.
	 *
	 * @return true if yes; false if no.
	 */
	public boolean hasDimension() {
		return getDimension() != null;
	}

	/**
	 * Unbinds everything managed by this lobby.
	 */
	public void unbindAll() {
		players.forEach(this::unbindPlayer);
		teams.forEach(this::unbindTeam);
		unbindPreset();
		unbindWorld();
		unbindDimension();
	}

	/*
	 * Implement WorldHolder.
	 */

	@Override
	public ServerWorld getWorld() {
		return world;
	}

	@Override
	public void bindWorld(ServerWorld world) {
		this.world = world;
	}

	@Override
	public void unbindWorld() {
		this.world = null;
	}

	/*
	 * Implement DimensionHolder.
	 */

	@Override
	public DimensionType getDimension() {
		return dimension;
	}

	@Override
	public void bindDimension(DimensionType dimension) {
		this.dimension = dimension;
	}

	@Override
	public void unbindDimension() {
		this.dimension = null;
	}

	/*
	 * Implement PresetHolder.
	 */

	@Override
	public Preset getPreset() {
		return preset;
	}

	@Override
	public void bindPreset(Preset preset) {
		this.preset = preset;
	}

	@Override
	public void unbindPreset() {
		this.preset.retract(this);
		this.preset = null;
	}

	/*
	 * Implement PlayerHolder.
	 */

	@Override
	public Collection<Player> getPlayers() {
		return players;
	}

	@Override
	public void bindPlayer(Player player) {
		if (LobbyBindPlayerEvent.dispatch(this, player).isCancelled()) return;
		this.players.add(player);
		player.bindLobby(this);
	}

	@Override
	public void unbindPlayer(Player player) {
		if (LobbyUnbindPlayerEvent.dispatch(this, player).isCancelled()) return;
		this.players.remove(player);
		player.unbindLobby();
	}

	/*
	 * Implement TeamHolder.
	 */

	public Collection<Team> getTeams() {
		return teams;
	}

	public void bindTeam(Team team) {
		if (LobbyBindTeamEvent.dispatch(this, team).isCancelled()) return;
		this.teams.add(team);
		team.bindLobby(this);
	}

	public void unbindTeam(Team team) {
		if (LobbyUnbindTeamEvent.dispatch(this, team).isCancelled()) return;
		this.teams.remove(team);
		team.unbindLobby();
	}

	/*
	 * Implement QueueHolder.
	 */

	@Override
	public Map<Predicate<Lobby>, Consumer<Lobby>> getQueue() {
		return queue;
	}

	/*
	 * Implement TrackerHolder.
	 */

	@Override
	public Map<Object, Tracker<?>> getTrackers() {
		return trackers;
	}
	/*
	 * Implement Tickable.
	 */

	@Override
	public void tick() {
		trackers.forEach((key, tracker) -> tracker.tick());
		teams.forEach(Team::tick);
		queue.forEach((predicate, action) -> {
			if (predicate.test(this)) action.accept(this);
		});
	}
}
