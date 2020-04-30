package com.github.vini2003.polyester.api.server;

import net.minecraft.server.world.ServerWorld;

public interface MinecraftServerWrapper {
	/**
	 * Unloads a server world.
	 *
	 * @param serverWorld the given server world.
	 */
	void unloadWorld(ServerWorld serverWorld);

	/**
	 * Resets a given server world.
	 *
	 * @param serverWorld the given server world.
	 */
	void resetWorld(ServerWorld serverWorld);
}
