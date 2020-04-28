package com.github.vini2003.spork.utility;

import com.github.vini2003.spork.api.dimension.DimensionFactory;
import com.github.vini2003.spork.api.dimension.DimensionRegistry;
import com.github.vini2003.spork.world.VoidWorldGenerator;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;
import net.minecraft.world.dimension.DimensionType;

import java.util.Random;

public class DimensionUtilities {
	private static final Random RANDOM = new Random();

	public static void teleport(PlayerEntity player, DimensionType type, BlockPos position) {
		FabricDimensions.teleport(player, type, (entity, world, direction, pitch, yaw) -> new BlockPattern.TeleportTarget(new Vec3d(position).add(0.5d, 0d, 0.5d), Vec3d.ZERO, (int) yaw));
		((ServerPlayerEntity) player).teleport(WorldUtilities.getWorld(type), position.getX(), position.getY(), position.getZ(), player.yaw, player.pitch);

		((ServerPlayerEntity) player).closeContainer();
		((ServerPlayerEntity) player).closeCurrentScreen();
	}

	public static Pair<Identifier, DimensionType> getSuperFlat() {
		Identifier name = new Identifier("spork", String.valueOf(RANDOM.nextInt()));

		DimensionFactory factory = new DimensionFactory()
				.allowBeds()
				.allowWater()
				.enableSky()
				.withChunkGeneratorFunction(VoidWorldGenerator::new)
				.withSpawnChunkPositionFunction((chunkPosition, canMobSpawn) -> new BlockPos(0, 4, 0))
				.withTopChunkPositionFunction(((x, z, canMobSpawn) -> new BlockPos(0, 4, 0)));

		return new Pair<>(name, DimensionRegistry.INSTANCE.register(name, factory, VoronoiBiomeAccessType.INSTANCE));
	}
}
