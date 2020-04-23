package com.github.vini2003.spork;

import com.github.vini2003.spork.api.data.Position;
import com.github.vini2003.spork.api.dimension.DimensionFactory;
import com.github.vini2003.spork.api.dimension.DimensionRegistry;
import com.github.vini2003.spork.api.dimension.DimensionSettings;
import com.github.vini2003.spork.command.DimensionCommands;
import com.github.vini2003.spork.registry.SporkCommands;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.source.*;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Spork implements ModInitializer {
	public static final String NAME = "spork";
	public static final Logger LOGGER = LogManager.getLogger(NAME);

	@Override
	public void onInitialize() {
	}
}
