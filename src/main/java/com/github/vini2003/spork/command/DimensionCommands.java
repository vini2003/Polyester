package com.github.vini2003.spork.command;

import com.github.vini2003.spork.Spork;
import com.github.vini2003.spork.api.dimension.DimensionFactory;
import com.github.vini2003.spork.api.dimension.DimensionRegistry;
import com.github.vini2003.spork.api.dimension.DimensionState;
import com.github.vini2003.spork.api.dimension.ImplementedDimension;
import com.github.vini2003.spork.api.entity.Player;
import com.github.vini2003.spork.api.server.MinecraftServerWrapper;
import com.github.vini2003.spork.api.text.TextWrapper;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSourceConfig;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.command.arguments.BlockPosArgumentType.blockPos;
import static net.minecraft.command.arguments.BlockPosArgumentType.getBlockPos;
import static net.minecraft.command.arguments.IdentifierArgumentType.getIdentifier;
import static net.minecraft.command.arguments.IdentifierArgumentType.identifier;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class DimensionCommands {
	public static SuggestionProvider<ServerCommandSource> suggestDimensions() {
		return (context, builder) -> getSuggestionsBuilder(builder, (List<String>) DimensionRegistry.INSTANCE.getNames().values().stream().map(Identifier::toString).collect(Collectors.toList()));
	}

	private static CompletableFuture<Suggestions> getSuggestionsBuilder(SuggestionsBuilder builder, List<String> list) {
		String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

		if (list.isEmpty()) {
			return Suggestions.empty();
		} else {
			list.forEach(string -> {
				if (string.toLowerCase().startsWith(remaining)) {
					builder.suggest(string);
				}
			});
		}

		return builder.buildFuture();
	}

	private static int teleport(CommandContext<ServerCommandSource> context, DimensionType type, BlockPos position) throws CommandSyntaxException {
		PlayerEntity player = context.getSource().getPlayer();
		FabricDimensions.teleport(player, type, (entity, world, direction, pitch, yaw) -> new BlockPattern.TeleportTarget(new Vec3d(position).add(0.5d, 0d, 0.5d), Vec3d.ZERO, (int) yaw));
		player.teleport(position.getX(), position.getY(), position.getZ());

		Player.of(context.getSource().getPlayer()).sendChatMessage(
				TextWrapper.builder()
						.with(TextWrapper.literal("Teleported to dimension"))
						.with(TextWrapper.literal(DimensionRegistry.INSTANCE.getByIdentifier(new Identifier(getString(context, "name"))).toString()))
						.build(), MessageType.CHAT
		);

		return 1;
	}

	private static int create(CommandContext<ServerCommandSource> context, Identifier name) throws CommandSyntaxException {
		DimensionFactory factory = new DimensionFactory()
				.allowBeds()
				.allowWater()
				.enableSky()
				.withChunkGeneratorFunction((world) -> new FlatChunkGenerator(world, new FixedBiomeSource(new FixedBiomeSourceConfig(null)), FlatChunkGeneratorConfig.getDefaultConfig()))
				.withSpawnChunkPositionFunction((chunkPosition, canMobSpawn) -> new BlockPos(0, 4, 0))
				.withTopChunkPositionFunction(((x, z, canMobSpawn) -> new BlockPos(0, 4, 0)));

		DimensionRegistry.INSTANCE.register(name, factory, VoronoiBiomeAccessType.INSTANCE);

		Player.of(context.getSource().getPlayer()).sendChatMessage(
				TextWrapper.builder()
						.with(TextWrapper.literal("Teleported to dimension"))
						.with(TextWrapper.literal(name.toString()))
						.build(), MessageType.CHAT
		);

		return 1;
	}

	private static int destroy(CommandContext<ServerCommandSource> context, Identifier name) throws CommandSyntaxException {
		ArrayList<World> worldsToUnload = new ArrayList<>();

		context.getSource().getMinecraftServer().getPlayerManager().getPlayerList().forEach(player -> {
			if (DimensionRegistry.INSTANCE.getByType(player.dimension).equals(name)) {
				((ImplementedDimension) player.world.dimension).setState(DimensionState.RESET_UNREGISTER);
				worldsToUnload.add(player.world);
				try {
					teleport(context, DimensionType.OVERWORLD, new BlockPos(0, 64, 0));
				} catch (CommandSyntaxException exception) {
					Spork.LOGGER.log(Level.ERROR, "Failed to move player after dimension " + name.toString() + " was destroyed!");
				}
			}
		});

		worldsToUnload.forEach(world -> {
			((MinecraftServerWrapper) context.getSource().getMinecraftServer()).unloadWorld((ServerWorld) world);
		});

		DimensionRegistry.INSTANCE.unregister(name);

		Player.of(context.getSource().getPlayer()).sendChatMessage(
				TextWrapper.builder()
						.with(TextWrapper.literal("Destroyed dimension"))
						.with(TextWrapper.literal(name.toString()))
						.build(), MessageType.CHAT
		);

		return 1;
	}

	public static void initialize() {
		CommandRegistry.INSTANCE.register(false, dispatcher -> {
			LiteralCommandNode<ServerCommandSource> baseNode =
					literal("dimension").requires(source -> source.hasPermissionLevel(4)).build();

			LiteralCommandNode<ServerCommandSource> teleportNode =
					literal("teleport")
							.then(argument("name", identifier())
									.suggests(suggestDimensions())
									.executes(context -> teleport(context, DimensionRegistry.INSTANCE.getByIdentifier(getIdentifier(context, "name")), new BlockPos(0, 64, 0)))
									.then(argument("position", blockPos())
											.executes(context -> teleport(context, DimensionRegistry.INSTANCE.getByIdentifier(getIdentifier(context, "name")), getBlockPos(context, "position")))))
							.build();

			LiteralCommandNode<ServerCommandSource> createNode =
					literal("create")
							.then(argument("name", identifier())
									.executes(context -> create(context, getIdentifier(context, "name"))))
							.build();

			LiteralCommandNode<ServerCommandSource> destroyNode =
					literal("destroy")
							.then(argument("name", identifier())
									.suggests(suggestDimensions())
									.executes(context -> destroy(context, getIdentifier(context, "name"))))
							.build();

			dispatcher.getRoot().addChild(baseNode);
			baseNode.addChild(teleportNode);
			baseNode.addChild(createNode);
			baseNode.addChild(destroyNode);
		});
	}
}