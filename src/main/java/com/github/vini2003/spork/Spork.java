package com.github.vini2003.spork;

import com.github.vini2003.spork.api.data.Matrix;
import com.github.vini2003.spork.api.entity.Player;
import com.github.vini2003.spork.api.event.EventResult;
import com.github.vini2003.spork.api.event.type.block.BlockStepEvent;
import com.github.vini2003.spork.api.event.type.player.PlayerConnectEvent;
import com.github.vini2003.spork.api.formatting.FormattingWrapper;
import com.github.vini2003.spork.registry.SporkCommands;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Spork implements ModInitializer {
	public static final String NAME = "spork";
	public static final Logger LOGGER = LogManager.getLogger(NAME);

	@Override
	public void onInitialize() {
		SporkCommands.initialize();
	}
}
