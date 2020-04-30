package com.github.vini2003.polyester.registry;

import com.github.vini2003.polyester.api.event.EventResult;
import com.github.vini2003.polyester.api.event.type.player.PlayerDisconnectEvent;
import com.github.vini2003.polyester.api.manager.LobbyManager;

public class PolyesterEventRegistry {
	public static void initialize() {
		PlayerDisconnectEvent.register(player -> {
			LobbyManager.INSTANCE.getLobbies().forEach((identifier, lobby) -> lobby.unbindPlayer(player));
			return EventResult.CONTINUE;
		});
	}
}
