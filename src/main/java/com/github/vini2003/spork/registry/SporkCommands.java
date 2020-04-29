package com.github.vini2003.spork.registry;

import com.github.vini2003.spork.api.dimension.DimensionCommands;
import com.github.vini2003.spork.command.LobbyCommands;

public class SporkCommands {
	public static void initialize() {
		LobbyCommands.initialize();
		DimensionCommands.initialize();
	}
}
