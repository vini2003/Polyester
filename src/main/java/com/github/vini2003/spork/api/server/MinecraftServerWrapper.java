package com.github.vini2003.spork.api.server;

import net.minecraft.server.world.ServerWorld;

public interface MinecraftServerWrapper {
	void unloadWorld(ServerWorld serverWorld);

	void resetWorld(ServerWorld serverWorld);
}
