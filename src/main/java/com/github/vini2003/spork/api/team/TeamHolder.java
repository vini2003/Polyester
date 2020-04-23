package com.github.vini2003.spork.api.team;

import java.util.Collection;

public interface TeamHolder {
	/**
	 * Retrieves the teams managed by this holder.
	 *
	 * @return the requested teams.
	 */
	Collection<Team> getTeams();

	/**
	 * Binds a team to this holder.
	 *
	 * @param team the team to be bound.
	 */
	void bindTeam(Team team);

	/**
	 * Unbinds a team from this holder.
	 *
	 * @param team the team to be unbound.
	 */
	void unbindTeam(Team team);


}
