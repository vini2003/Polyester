package com.github.vini2003.example;

import com.github.vini2003.spork.api.event.EventResult;
import com.github.vini2003.spork.api.event.type.player.PlayerConnectEvent;
import com.github.vini2003.spork.api.lobby.Lobby;
import com.github.vini2003.spork.api.manager.LobbyManager;
import com.github.vini2003.spork.api.preset.PresetRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Example implements ModInitializer {
	@Override
	public void onInitialize() {
		PresetRegistry.INSTANCE.register(new Identifier("spork", "preset"), new ExamplePreset());
		Lobby mainLobby = new Lobby(new Identifier("spork", "lobby"));

		Predicate<Lobby> bootPredicate = (lobby -> true);
		Consumer<Lobby> bootAction = (lobby -> {
			lobby.bindPreset(new ExamplePreset());
			lobby.getPreset().apply(lobby);

			lobby.unqueueAction(bootPredicate);
		});

		mainLobby.enqueueAction(bootPredicate, bootAction);

		LobbyManager.INSTANCE.add(mainLobby);

		PlayerConnectEvent.register(player -> {
			mainLobby.bindPlayer(player);
			player.setGameMode(GameMode.ADVENTURE);
			return EventResult.CONTINUE;
		});
	}
}
