package com.github.vini2003.spork.utility;

import com.github.vini2003.spork.api.dimension.DimensionFactory;
import com.github.vini2003.spork.api.dimension.DimensionRegistry;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSourceConfig;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;

import java.util.Random;

public class DimensionUtilities {
	private static final Random RANDOM = new Random();

	public static void teleport(PlayerEntity player, DimensionType type, BlockPos position) {
		FabricDimensions.teleport(player, type, null);
		player.teleport(position.getX(), position.getY(), position.getZ());
	}

	public static Pair<Identifier, DimensionType> getSuperFlat() {
		Identifier name = new Identifier("spork", String.valueOf(RANDOM.nextInt()));

		DimensionFactory factory = new DimensionFactory()
				.allowBeds()
				.allowWater()
				.enableSky()
				.withChunkGeneratorFunction((world) -> new FlatChunkGenerator(world, new FixedBiomeSource(new FixedBiomeSourceConfig(null)), FlatChunkGeneratorConfig.getDefaultConfig()))
				.withSpawnChunkPositionFunction((chunkPosition, canMobSpawn) -> new BlockPos(0, 4, 0))
				.withTopChunkPositionFunction(((x, z, canMobSpawn) -> new BlockPos(0, 4, 0)));

		return new Pair<>(name, DimensionRegistry.INSTANCE.register(name, factory, VoronoiBiomeAccessType.INSTANCE));
	}
}
