package com.github.vini2003.spork.api.dimension;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;

/**
 * These DimensionSettings are heavily based on
 * the Tesseract API by Vatuu Komalia, with
 * permission.
 *
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

	boolean hasSky = false;

	boolean allowsWater = false;
	boolean allowsBeds = false;

	int cloudHeight = 127;

	FockThickFunction fogThickFunction = (skyAngle, tickDelta) -> false;
	FogColorFunction fogColorFunction = (position, tickDelta) -> new Vec3d(255, 255, 255);
	SpawnChunkPositionFunction spawnChunkPositionFunction;
	TopChunkPositionFunction topSpawnPositionFunction;
	ChunkGeneratorFunction chunkGeneratorFunction;

	BlockPos spawnPosition = new BlockPos(0, 64, 0);
}
