package com.github.vini2003.polyester.mixin.event;

import com.github.vini2003.polyester.api.entity.Player;
import com.github.vini2003.polyester.api.lobby.Lobby;
import com.github.vini2003.polyester.api.team.Team;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements Player {
	@Unique
	Team team;

	@Unique
	Lobby lobby;

	@Override
	public Lobby getLobby() {
		return lobby;
	}

	@Override
	public void bindLobby(Lobby lobby) {
		this.lobby = lobby;
	}

	@Override
	public void unbindLobby() {
		this.lobby = null;
	}

	@Override
	public Team getTeam() {
		return team;
	}

	@Override
	public void bindTeam(Team team) {
		this.team = team;
	}

	@Override
	public void unbindTeam() {
		this.team = null;
	}
}
