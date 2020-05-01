package com.github.vini2003.polyester;

import com.github.vini2003.polyester.api.block.BlockInformation;
import com.github.vini2003.polyester.api.data.Position;
import com.github.vini2003.polyester.api.structure.Structure;
import com.github.vini2003.polyester.registry.PolyesterCommandRegistry;
import com.github.vini2003.polyester.registry.PolyesterEventRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Polyester implements ModInitializer {
	public static final String NAME = "Polyester";
	public static final String IDENTIFIER = "polyester";
	public static final Logger LOGGER = LogManager.getLogger(NAME);

	@Override
	public void onInitialize() {
		PolyesterCommandRegistry.initialize();
		PolyesterEventRegistry.initialize();
	}
}
