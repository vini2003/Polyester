package com.github.vini2003.spork.api.dimension;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;

/**
 * This ImplementedDimension is heavily based on
 * the Tesseract API by Vatuu Komalia, with
 * permission.
 *
 * You can find Tesseract at: https://github.com/Vatuu/tesseract
 */
public class ImplementedDimension extends Dimension {
	private DimensionType type;
	private DimensionState state;
	private DimensionSettings settings;

	public ImplementedDimension(World world, DimensionType type, DimensionSettings settings) {
		super(world, type, 15);
		this.type = type;
		this.settings = settings;
		this.state = DimensionState.SAVE;
	}

	@Override
	public float getSkyAngle(long timeOfDay, float tickDelta) {
		return 0;
	}

	@Override
	public DimensionType getType() {
		return type;
	}

	@Override
	public boolean hasVisibleSky() {
		return settings.hasSky;
	}

	@Override
	public boolean isFogThick(int x, int z) {
		return settings.fogThickFunction.apply(x, z);
	}

	@Override
	public boolean doesWaterVaporize() {
		return settings.allowsWater;
	}

	@Override
	public boolean canPlayersSleep() {
		return !settings.allowsBeds;
	}

	@Override
	public boolean hasSkyLight() {
		return type.hasSkyLight();
	}

	@Override
	public Vec3d getFogColor(float skyAngle, float tickDelta) {
		return settings.fogColorFunction.apply(skyAngle, tickDelta);
	}

	@Override
	public BlockPos getForcedSpawnPoint() {
		return settings.spawnPosition;
	}

	@Override
	public BlockPos getSpawningBlockInChunk(ChunkPos chunkPosition, boolean canMobSpawn) {
		return settings.spawnChunkPositionFunction.apply(chunkPosition, canMobSpawn);
	}

	@Override
	public BlockPos getTopSpawningBlockPosition(int x, int z, boolean canMobSpawn) {
		return settings.topSpawnPositionFunction.apply(x, z, canMobSpawn);
	}

	@Override
	public ChunkGenerator<? extends ChunkGeneratorConfig> createChunkGenerator() {
		return settings.chunkGeneratorFunction.apply(this.world);
	}

	@Override
	public float getCloudHeight() {
		return settings.cloudHeight;
	}

	@Override
	public boolean isNether() {
		return false;
	}

	public DimensionState getState() {
		return state;
	}

	public void setState(DimensionState state) {
		this.state = state;
	}

	public DimensionSettings getSettings() {
		return settings;
	}

	public void setSettings(DimensionSettings settings) {
		this.settings = settings;
	}
}
