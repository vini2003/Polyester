package com.github.vini2003.polyester.registry;

import com.github.vini2003.polyester.api.dimension.command.DimensionCommands;
import com.github.vini2003.polyester.api.lobby.command.LobbyCommands;
import com.github.vini2003.polyester.api.structure.command.StructureCommands;

public class PolyesterCommandRegistry {
	public static void initialize() {
		LobbyCommands.initialize();
		DimensionCommands.initialize();
		StructureCommands.initialize();
	}
}
