package com.github.vini2003.spork;

import com.github.vini2003.spork.api.data.Time;
import com.github.vini2003.spork.registry.SporkCommands;
import com.github.vini2003.spork.registry.SporkEvents;
import com.sun.jmx.remote.internal.ArrayQueue;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Spork implements ModInitializer {
	public static final String NAME = "spork";
	public static final Logger LOGGER = LogManager.getLogger("Spork");

	@Override
	public void onInitialize() {
		SporkCommands.initialize();
		SporkEvents.initialize();
	}
}
