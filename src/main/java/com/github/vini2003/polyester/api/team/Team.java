package com.github.vini2003.polyester.api.team;

import com.github.vini2003.polyester.api.tracker.Tracker;
import com.github.vini2003.polyester.api.tracker.TrackerHolder;
import com.github.vini2003.polyester.api.entity.Player;
import com.github.vini2003.polyester.api.event.type.team.TeamBindLobbyEvent;
import com.github.vini2003.polyester.api.event.type.team.TeamUnbindLobbyEvent;
import com.github.vini2003.polyester.api.formatting.FormattingWrapper;
import com.github.vini2003.polyester.api.lobby.Lobby;
import com.github.vini2003.polyester.api.lobby.LobbyHolder;
import com.github.vini2003.polyester.api.player.PlayerHolder;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Tickable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Team implements LobbyHolder, PlayerHolder, TrackerHolder, Tickable {
	private static final Random RANDOM = new Random();

	private final List<Player> players = new ArrayList<>();

	private final Map<Object, Tracker<?>> trackers = new HashMap<>();

	private final Map<Predicate<Team>, Consumer<Team>> queue = new HashMap<>();

	private final Details details;

	private Lobby lobby;

	/**
	 * Instantiates a team based on a collection of players
	 * and default details.
	 *
	 * @param players the specified players.
	 */
	public Team(Collection<Player> players) {
		this.players.addAll(players);
		this.details = new Details();
	}

	/**
	 * Instantiates a team based on a collection of players
	 * and custom details.
	 *
	 * @param players the specified players.
	 * @param details the specified details.
	 */
	public Team(Collection<Player> players, Details details) {
		this.players.addAll(players);
		this.details = details;
	}

	/**
	 * Retrieves this team's details.
	 *
	 * @return the requested details.
	 */
	public Details getDetails() {
		return details;
	}

	/**
	 * Retrieve this team's size.
	 *
	 * @return the requested size.
	 */
	public int size() {
		return players.size();
	}

	/*
	 * Implement LobbyHolder.
	 */

	@Override
	public Lobby getLobby() {
		return lobby;
	}

	@Override
	public void bindLobby(Lobby lobby) {
		if (TeamBindLobbyEvent.dispatch(this, lobby).isCancelled()) return;
		this.lobby = lobby;
		this.players.forEach(player -> player.bindLobby(lobby));
	}

	@Override
	public void unbindLobby() {
		if (TeamUnbindLobbyEvent.dispatch(this, lobby).isCancelled()) return;
		this.lobby = null;
		this.players.forEach(Player::unbindLobby);
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
		this.players.add(player);
		player.bindTeam(this);
	}

	@Override
	public void unbindPlayer(Player player) {
		this.players.remove(player);
		player.unbindTeam();
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
		queue.forEach((predicate, action) -> {
			if (predicate.test(this)) action.accept(this);
		});
	}

	public static class Details {
		FormattingWrapper formatting;
		Text name;
		Text prefix;
		Text suffix;

		/**
		 * Instantiates a team based on default arguments.
		 */
		public Details() {
			this((FormattingWrapper) (Object) Formatting.WHITE, new LiteralText("Team " + RANDOM.nextInt(32)));
		}

		/**
		 * Instantiates a team's details based on custom arguments.
		 *
		 * @param formatting the specified formatting.
		 * @param name       the specified name.
		 */
		public Details(FormattingWrapper formatting, Text name) {
			this(formatting, name, new LiteralText(""), new LiteralText(""));
		}

		/**
		 * Instantiates a team's details based on custom arguments
		 * including a prefix and a suffix.
		 *
		 * @param formatting the specified formatting.
		 * @param name       the specified name.
		 * @param prefix     the specified prefix.
		 * @param suffix     the specified suffix.
		 */
		public Details(FormattingWrapper formatting, Text name, Text prefix, Text suffix) {
			this.formatting = formatting;
			this.name = name;
			this.prefix = prefix;
			this.suffix = suffix;
		}

		/**
		 * Retrieves the formatting associated with this team.
		 *
		 * @return the requested formatting.
		 */
		public FormattingWrapper getFormatting() {
			return formatting;
		}

		/**
		 * Sets the formatting associated with this team.
		 *
		 * @param formatting the specified formatting.
		 */
		public void setFormatting(FormattingWrapper formatting) {
			this.formatting = formatting;
		}

		/**
		 * Retrieves the name associated with this team.
		 *
		 * @return the requested name.
		 */
		public Text getName() {
			return name;
		}

		/**
		 * Sets the name associated with this team.
		 *
		 * @param name the specified name.
		 */
		public void setName(Text name) {
			this.name = name;
		}

		/**
		 * Retrieves the prefix associated with this team.
		 *
		 * @return the requested prefix.
		 */
		public Text getPrefix() {
			return prefix;
		}

		/**
		 * Sets the prefix associated with this team.
		 *
		 * @param prefix the specified prefix.
		 */
		public void setPrefix(Text prefix) {
			this.prefix = prefix;
		}

		/**
		 * Retrieves the suffix associated with this team.
		 *
		 * @return the requested suffix.
		 */
		public Text getSuffix() {
			return suffix;
		}

		/**
		 * Sets the suffix associated with this team.
		 *
		 * @param suffix the specified suffix.
		 */
		public void setSuffix(Text suffix) {
			this.suffix = suffix;
		}

		/**
		 * Asserts whether this team has a prefix or not.
		 *
		 * @return true if yes; false if no.
		 */
		public boolean hasPrefix() {
			return !prefix.asFormattedString().isEmpty();
		}

		/**
		 * Asserts whether this team has a suffix or not.
		 *
		 * @return true if yes; false if no.
		 */
		public boolean hasSuffix() {
			return !suffix.asFormattedString().isEmpty();
		}
	}
}
