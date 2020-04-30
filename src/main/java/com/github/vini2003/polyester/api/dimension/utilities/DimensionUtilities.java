package com.github.vini2003.polyester.api.dimension.utilities;

import com.github.vini2003.polyester.Polyester;
import com.github.vini2003.polyester.api.dimension.DimensionFactory;
import com.github.vini2003.polyester.api.dimension.registry.DimensionRegistry;
import com.github.vini2003.polyester.utility.WorldUtilities;
import com.github.vini2003.polyester.world.VoidWorldGenerator;
import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;
import net.minecraft.world.dimension.DimensionType;

import java.util.UUID;
import java.util.function.Function;

public class DimensionUtilities {
	public static final Function<BlockPos, EntityPlacer> PLACER = ((position) -> {
		return (entity, world, direction, pitch, yaw) -> new BlockPattern.TeleportTarget(new Vec3d(position).add(0.5d, 0d, 0.5d), Vec3d.ZERO, (int) yaw);
	});

	public static void teleport(PlayerEntity player, DimensionType type, BlockPos position) {
		FabricDimensions.teleport(player, type, PLACER.apply(position));
		((ServerPlayerEntity) player).teleport(WorldUtilities.getWorld(type), position.getX(), position.getY(), position.getZ(), player.yaw, player.pitch);

		((ServerPlayerEntity) player).closeContainer();
		((ServerPlayerEntity) player).closeCurrentScreen();
	}

	public static Identifier getVoidDimension() {
		Identifier name = new Identifier(Polyester.IDENTIFIER, UUID.randomUUID().toString().replace("-", "").toLowerCase());

		DimensionFactory factory = new DimensionFactory()
				.allowBeds()
				.allowWater()
				.enableSky()
				.withChunkGeneratorFunction(VoidWorldGenerator::new)
				.withSpawnChunkPositionFunction((chunkPosition, canMobSpawn) -> new BlockPos(0, 4, 0))
				.withTopChunkPositionFunction(((x, z, canMobSpawn) -> new BlockPos(0, 4, 0)))
				.withEntityPlacer(DimensionUtilities.PLACER.apply(new BlockPos(0, 64, 0)))
				.withBiomeAccessType(VoronoiBiomeAccessType.INSTANCE);

		DimensionRegistry.INSTANCE.register(name, factory);

		return name;
	}
}
