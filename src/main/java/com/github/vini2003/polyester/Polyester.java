package com.github.vini2003.polyester;

import com.github.vini2003.polyester.registry.PolyesterCommandRegistry;
import com.github.vini2003.polyester.registry.PolyesterEventRegistry;
import net.fabricmc.api.ModInitializer;
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
