package com.github.vini2003.polyester.api.preset;

import com.github.vini2003.polyester.api.lobby.Lobby;
import net.minecraft.util.Identifier;

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

	public abstract void restart(Lobby lobby);
}
