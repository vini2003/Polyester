package com.github.vini2003.polyester.utility;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionType;

public class IntegratedWorldUtilities {
	public static ServerWorld getWorld(DimensionType type) {
		return MinecraftClient.getInstance().getServer().getWorld(type);
	}
}
