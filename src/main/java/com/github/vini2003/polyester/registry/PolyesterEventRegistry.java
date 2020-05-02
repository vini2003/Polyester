package com.github.vini2003.polyester.registry;

import com.github.vini2003.polyester.api.data.Position;
import com.github.vini2003.polyester.api.entity.Player;
import com.github.vini2003.polyester.api.event.EventResult;
import com.github.vini2003.polyester.api.event.type.item.ItemUseEvent;
import com.github.vini2003.polyester.api.event.type.logic.ServerShutdownEvent;
import com.github.vini2003.polyester.api.event.type.logic.ServerStartEvent;
import com.github.vini2003.polyester.api.event.type.player.PlayerDisconnectEvent;
import com.github.vini2003.polyester.api.manager.LobbyManager;
import com.github.vini2003.polyester.api.structure.StructureManager;
import com.github.vini2003.polyester.api.structure.command.StructureCommands;
import com.github.vini2003.polyester.api.structure.registry.StructureRegistry;
import com.github.vini2003.polyester.api.text.TextBuilder;
import com.sun.corba.se.impl.logging.POASystemException;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.LiteralText;
import net.minecraft.util.BlockRotation;

import java.io.File;

public class PolyesterEventRegistry {
	private static int particleCountdown = 20;

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

		ItemUseEvent.register((world, player, data, stack, hand) -> {
			if (player.isSneaking()) {
				StructureManager.setCorner(StructureManager.Corner.SECOND, Player.of(player), data.getPosition());

				Player.of(player).sendChatMessage(
						TextBuilder.builder()
								.with(new LiteralText("§o§dSelected " + StructureManager.Corner.SECOND.toString() + " corner at "  + data.getPosition().toCoordinates()))
								.build()
				);
			} else {
				StructureManager.setCorner(StructureManager.Corner.FIRST, Player.of(player), data.getPosition());

				Player.of(player).sendChatMessage(
						TextBuilder.builder()
								.with(new LiteralText("§o§dSelected " + StructureManager.Corner.FIRST.toString() + " corner at " + data.getPosition().toCoordinates()))
								.build()
				);
			}

			return EventResult.CONTINUE;
		});

		ServerTickCallback.EVENT.register(server -> {
			if (particleCountdown <= 0) {
				server.getPlayerManager().getPlayerList().forEach(player -> {
					if (StructureManager.getCorner(StructureManager.Corner.FIRST, Player.of(player)) != null) {
						Position position = StructureManager.getCorner(StructureManager.Corner.FIRST, Player.of(player));
						player.world.addParticle(ParticleTypes.LARGE_SMOKE, position.x, position.y + 0.5, position.z, 0, 0, 0);
					}
					if (StructureManager.getCorner(StructureManager.Corner.SECOND, Player.of(player)) != null) {
						Position position = StructureManager.getCorner(StructureManager.Corner.SECOND, Player.of(player));
						player.world.addParticle(ParticleTypes.LARGE_SMOKE, position.x, position.y + 0.5, position.z, 0, 0, 0);
					}
					if (StructureManager.getAnchor(Player.of(player)) != null) {
						Position position = StructureManager.getAnchor(Player.of(player));
						player.world.addParticle(ParticleTypes.LARGE_SMOKE, position.x, position.y + 0.5, position.z, 0, 0, 0);
					}
				});

				particleCountdown = 20;
			} else {
				--particleCountdown;
			}
		});
	}
}
