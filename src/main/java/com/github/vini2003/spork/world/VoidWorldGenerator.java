package com.github.vini2003.spork.world;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSourceConfig;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class VoidWorldGenerator extends ChunkGenerator<ChunkGeneratorConfig> {
	public VoidWorldGenerator(IWorld world) {
		super(world, new FixedBiomeSource(new FixedBiomeSourceConfig(null)), new ChunkGeneratorConfig());
	}

	public void buildSurface(ChunkRegion chunkRegion, Chunk chunk) {
	}

	public int getSpawnHeight() {
		return 64;
	}

	protected Biome getDecorationBiome(BiomeAccess biomeAccess, BlockPos pos) {
		return Biomes.PLAINS;
	}

	public void populateNoise(IWorld world, Chunk chunk) {
	}

	public int getHeightOnGround(int x, int z, Heightmap.Type heightmapType) {
		return 0;
	}

	public void spawnEntities(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {
	}

	public boolean hasStructure(Biome biome, StructureFeature<? extends FeatureConfig> structureFeature) {
		return false;
	}

	public <C extends FeatureConfig> C getStructureConfig(Biome biome, StructureFeature<C> structureFeature) {
		return null;
	}

	public BlockPos locateStructure(World world, String id, BlockPos center, int radius, boolean skipExistingChunks) {
		return null;
	}
}
