package com.github.vini2003.spork.api.preset;

import com.github.vini2003.spork.api.lobby.Lobby;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * A Preset is a set of actions
 * to be queued or performed
 * by a lobby, to, for example,
 * create a minigame.
 */
public abstract class Preset {
	public abstract Identifier getIdentifier();

	public abstract void apply(Lobby lobby);

	public abstract void retract(Lobby lobby);
}
