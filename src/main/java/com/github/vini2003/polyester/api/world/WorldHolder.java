package com.github.vini2003.polyester.api.world;

import net.minecraft.server.world.ServerWorld;

public interface WorldHolder {
	/**
	 * Retrieves the world managed by this holder.
	 *
	 * @return the requested world.
	 */
	ServerWorld getWorld();

	/**
	 * Binds a world to this holder.
	 *
	 * @param world the world to be bound.
	 */
	void bindWorld(ServerWorld world);

	/**
	 * Unbinds the world of this holder.
	 */
	void unbindWorld();
}
