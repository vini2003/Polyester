package com.github.vini2003.spork.api.lobby;

import com.github.vini2003.spork.api.data.Tracker;
import com.github.vini2003.spork.api.entity.Player;
import com.github.vini2003.spork.api.event.type.lobby.LobbyBindPlayerEvent;
import com.github.vini2003.spork.api.event.type.lobby.LobbyBindTeamEvent;
import com.github.vini2003.spork.api.event.type.lobby.LobbyUnbindPlayerEvent;
import com.github.vini2003.spork.api.event.type.lobby.LobbyUnbindTeamEvent;
import com.github.vini2003.spork.api.player.PlayerHolder;
import com.github.vini2003.spork.api.queue.QueueHolder;
import com.github.vini2003.spork.api.team.Team;
import com.github.vini2003.spork.api.team.TeamHolder;
import net.minecraft.util.Tickable;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
public class Lobby implements QueueHolder, PlayerHolder, TeamHolder, Tickable {
	private String identifier;

	private final ArrayList<Team> teams = new ArrayList<>();

	private final ArrayList<Player> players = new ArrayList<>();

	private final ArrayList<Tracker<?>> trackers = new ArrayList<>();

	private final HashMap<Predicate<Lobby>, Consumer<Lobby>> queue = new HashMap<>();

	private final Tracker<MutableInt> time = new Tracker<MutableInt>(new MutableInt()) {
		@Override
		public void tick() {
			getValue().add(1);
		}
	};

	public Lobby(String identifier) {
		setIdentifier(identifier);
		trackers.add(time);
	}

	public Tracker<MutableInt> getTime() {
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
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets this lobby's identifier.
	 *
	 * @param identifier the specified identifier.
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/*
	 * Implement PlayerHolder.
	 */

	@Override
	public ArrayList<Player> getPlayers() {
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
	}

	public void unbindTeam(Team team) {
		if (LobbyUnbindTeamEvent.dispatch(this, team).isCancelled()) return;
		this.teams.remove(team);
	}

	/*
	 * Implement QueueHolder.
	 */

	@Override
	public HashMap<Predicate<Lobby>, Consumer<Lobby>> getQueue() {
		return queue;
	}

	/*
	 * Implement Tickable.
	 */

	@Override
	public void tick() {
		trackers.forEach((Tracker::tick));
		teams.forEach(Team::tick);
		queue.forEach((predicate, action) -> {
			if (predicate.test(this)) action.accept(this);
		});
	}
}
