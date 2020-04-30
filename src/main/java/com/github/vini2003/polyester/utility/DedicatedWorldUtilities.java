package com.github.vini2003.polyester.utility;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionType;

public class DedicatedWorldUtilities {
	public static ServerWorld getWorld(DimensionType type) {
		return ((MinecraftDedicatedServer) FabricLoader.getInstance().getGameInstance()).getWorld(type);
	}
}
