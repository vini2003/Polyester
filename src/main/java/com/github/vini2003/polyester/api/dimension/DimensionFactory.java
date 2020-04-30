package com.github.vini2003.polyester.api.dimension;

import com.github.vini2003.polyester.api.data.Position;
import com.github.vini2003.polyester.api.dimension.implementation.DimensionImplementation;
import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

/**
 * This DimensionFactory is heavily based on
 * the Tesseract API by Vatuu Komalia, with
 * permission.
 * <p>
 * You can find Tesseract at: https://github.com/Vatuu/tesseract
 */
public class DimensionFactory {
	public DimensionSettings settings = new DimensionSettings();

	public DimensionFactory() {
	}

	public DimensionFactory enableSky() {
		settings.hasSky = true;
		return this;
	}

	public DimensionFactory enableThickFog() {
		settings.fogThickFunction = ((skyAngle, tickDelta) -> true);
		return this;
	}

	public DimensionFactory allowWater() {
		settings.allowsWater = true;
		return this;
	}

	public DimensionFactory allowBeds() {
		settings.allowsBeds = true;
		return this;
	}

	public DimensionFactory withCloudHeight(int height) {
		settings.cloudHeight = height;
		return this;
	}

	public DimensionFactory withFogRenderFunction(DimensionSettings.FockThickFunction function) {
		settings.fogThickFunction = function;
		return this;
	}

	public DimensionFactory withFogColorFunction(DimensionSettings.FogColorFunction function) {
		settings.fogColorFunction = function;
		return this;
	}

	public DimensionFactory withSpawnChunkPositionFunction(DimensionSettings.SpawnChunkPositionFunction function) {
		settings.spawnChunkPositionFunction = function;
		return this;
	}

	public DimensionFactory withTopChunkPositionFunction(DimensionSettings.TopChunkPositionFunction function) {
		settings.topSpawnPositionFunction = function;
		return this;
	}

	public DimensionFactory withChunkGeneratorFunction(DimensionSettings.ChunkGeneratorFunction function) {
		settings.chunkGeneratorFunction = function;
		return this;
	}

	public DimensionFactory withBiomeAccessType(BiomeAccessType biomeAccessType) {
		settings.biomeAccessType = biomeAccessType;
		return this;
	}

	public DimensionFactory withEntityPlacer(EntityPlacer entityPlacer) {
		settings.entityPlacer = entityPlacer;
		return this;
	}

	public DimensionFactory withSpawnPosition(Position position) {
		settings.spawnPosition = position.asBlockPosition();
		return this;
	}

	public Dimension build(World world, DimensionType type) {
		return new DimensionImplementation(world, type, settings);
	}
}
