package com.github.vini2003.polyester.api.dimension;

import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;

/**
 * These DimensionSettings are heavily based on
 * the Tesseract API by Vatuu Komalia, with
 * permission.
 * <p>
 * You can find Tesseract at: https://github.com/Vatuu/tesseract
 */
public class DimensionSettings {
	public interface FockThickFunction {
		boolean apply(float skyAngle, float tickDelta);
	}

	public interface FogColorFunction {
		Vec3d apply(float position, float tickDelta);
	}

	public interface SpawnChunkPositionFunction {
		BlockPos apply(ChunkPos chunkPosition, boolean canMobSpawn);
	}

	public interface TopChunkPositionFunction {
		BlockPos apply(int x, int z, boolean canMobSpawn);
	}

	public interface ChunkGeneratorFunction {
		ChunkGenerator<? extends ChunkGeneratorConfig> apply(World world);
	}

	public boolean hasSky = false;

	public boolean allowsWater = false;
	public boolean allowsBeds = false;

	public int cloudHeight = 127;

	public FockThickFunction fogThickFunction = (skyAngle, tickDelta) -> false;
	public FogColorFunction fogColorFunction = (position, tickDelta) -> new Vec3d(255, 255, 255);
	public SpawnChunkPositionFunction spawnChunkPositionFunction;
	public TopChunkPositionFunction topSpawnPositionFunction;
	public ChunkGeneratorFunction chunkGeneratorFunction;

	public BiomeAccessType biomeAccessType;

	public EntityPlacer entityPlacer;

	public BlockPos spawnPosition = new BlockPos(0, 64, 0);
}
