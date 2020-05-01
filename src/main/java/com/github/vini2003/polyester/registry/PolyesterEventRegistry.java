package com.github.vini2003.polyester.registry;

import com.github.vini2003.polyester.api.event.EventResult;
import com.github.vini2003.polyester.api.event.type.logic.ServerShutdownEvent;
import com.github.vini2003.polyester.api.event.type.logic.ServerStartEvent;
import com.github.vini2003.polyester.api.event.type.player.PlayerDisconnectEvent;
import com.github.vini2003.polyester.api.manager.LobbyManager;
import com.github.vini2003.polyester.api.structure.registry.StructureRegistry;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class PolyesterEventRegistry {
	public static void initialize() {
		PlayerDisconnectEvent.register(player -> {
			LobbyManager.INSTANCE.getLobbies().forEach((identifier, lobby) -> lobby.unbindPlayer(player));
			return EventResult.CONTINUE;
		});

		ServerStartEvent.register(server -> {
			File path = new File(FabricLoader.getInstance().getGameDirectory() + "/structures");

			if (!path.exists()) {
				path.mkdirs();
			}

			StructureRegistry.INSTANCE.deserialize(path);

			return EventResult.CONTINUE;
		});

		ServerShutdownEvent.register(server -> {
			File path = new File(FabricLoader.getInstance().getGameDirectory() + "/structures");

			if (!path.exists()) {
				path.mkdirs();
			}

			StructureRegistry.INSTANCE.serialize(path);

			return EventResult.CONTINUE;
		});
	}
}
