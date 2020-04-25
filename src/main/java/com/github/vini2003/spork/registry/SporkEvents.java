package com.github.vini2003.spork.registry;

import com.github.vini2003.spork.api.event.EventResult;
import com.github.vini2003.spork.api.event.type.player.PlayerDisconnectEvent;
import com.github.vini2003.spork.api.manager.LobbyManager;

public class SporkEvents {
	public static void initialize() {
		PlayerDisconnectEvent.register(player -> {
			LobbyManager.INSTANCE.getLobbies().forEach((identifier, lobby) -> lobby.unbindPlayer(player));
			return EventResult.CONTINUE;
		});
	}
}
