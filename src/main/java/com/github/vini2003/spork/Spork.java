package com.github.vini2003.spork;

import com.github.vini2003.spork.registry.SporkCommands;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Spork implements ModInitializer {
	public static final String NAME = "spork";
	public static final Logger LOGGER = LogManager.getLogger("Spork");

	@Override
	public void onInitialize() {
		SporkCommands.initialize();
	}
}
