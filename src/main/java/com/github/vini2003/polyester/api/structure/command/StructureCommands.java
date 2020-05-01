package com.github.vini2003.polyester.api.structure.command;

import com.github.vini2003.polyester.api.block.BlockInformation;
import com.github.vini2003.polyester.api.data.Position;
import com.github.vini2003.polyester.api.entity.Player;
import com.github.vini2003.polyester.api.structure.Structure;
import com.github.vini2003.polyester.api.structure.registry.StructureRegistry;
import com.github.vini2003.polyester.utility.RayUtilities;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RayTraceContext;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static net.minecraft.command.arguments.BlockPosArgumentType.blockPos;
import static net.minecraft.command.arguments.BlockPosArgumentType.getBlockPos;
import static net.minecraft.command.arguments.IdentifierArgumentType.getIdentifier;
import static net.minecraft.command.arguments.IdentifierArgumentType.identifier;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class StructureCommands {
	public static final Map<Player, Position> cornersOne = new HashMap<>();
	public static final Map<Player, Position> cornersTwo = new HashMap<>();
	public static final Map<Player, Position> origins = new HashMap<>();
	public static final Map<Player, Structure> loadStructures = new HashMap<>();
	public static final Map<Player, List<List<BlockInformation>>> placeStructures = new HashMap<>();

	public static SuggestionProvider<ServerCommandSource> suggestStructures() {
		return (context, builder) -> getSuggestionsBuilder(builder, (List<String>) StructureRegistry.INSTANCE.getKeys().stream().map(Identifier::toString).collect(Collectors.toList()));
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

	private static int set(CommandContext<ServerCommandSource> context, int corner) throws CommandSyntaxException {
		Position position = Position.of(((BlockHitResult) RayUtilities.rayTrace(context.getSource().getWorld(), context.getSource().getPlayer(), RayTraceContext.FluidHandling.ANY)).getBlockPos());

		if (corner == 1) {
			cornersOne.put(Player.of(context.getSource().getPlayer()), position);
		} else if (corner == 2) {
			cornersTwo.put(Player.of(context.getSource().getPlayer()), position);
		} else if (corner == 3) {
			origins.put(Player.of(context.getSource().getPlayer()), position);
		}

		return 1;
	}

	private static int load(CommandContext<ServerCommandSource> context, Identifier identifier, Player player) {
		loadStructures.put(player, StructureRegistry.INSTANCE.getByKey(identifier));

		return 1;
	}

	private static int save(CommandContext<ServerCommandSource> context, Identifier identifier, Player player, boolean includeAir) {
		Structure structure = new Structure();

		BlockPos origin = origins.get(player).asBlockPosition();

		Iterable<BlockPos> iterable = BlockPos.Mutable.iterate(cornersOne.get(player).asBlockPosition(), cornersTwo.get(player).asBlockPosition());
		Iterator<BlockPos> iterator = iterable.iterator();

		iterator.forEachRemaining(position -> {
			BlockInformation blockInformation = BlockInformation.of(context.getSource().getWorld(), position);

			if (!includeAir && blockInformation.hasState() && !blockInformation.getState().isAir()) {
				structure.bindBlock(Position.of(new BlockPos(origin.getX() - position.getX(), origin.getY() - position.getY(), origin.getZ() - position.getZ())), blockInformation);
			}
		});

		StructureRegistry.INSTANCE.register(identifier, structure);

		return 1;
	}

	private static int place(CommandContext<ServerCommandSource> context, Identifier identifier, Player player) {
		Structure structure = StructureRegistry.INSTANCE.getByKey(identifier);

		BlockPos origin = origins.get(player).asBlockPosition();

		ArrayList<BlockInformation> cache = new ArrayList<>();

		if (placeStructures.get(player) == null) {
			placeStructures.put(player, new ArrayList<>());
		}

		structure.blocks.forEach(((rawPosition, information) -> {
			BlockPos position = rawPosition.asBlockPosition();

			BlockPos newPosition = new BlockPos(origin.getX() + position.getX(), origin.getY() - position.getY(), origin.getZ() + position.getZ());

			BlockInformation oldInformation = BlockInformation.of(player.getWorld(), newPosition);

			if (information.hasState()) {
				player.getWorld().setBlockState(newPosition, information.getState());
			}
			if (information.hasEntity()) {
				information.getEntity().setLocation(player.getWorld(), newPosition);

				player.getWorld().removeBlockEntity(information.getPosition().asBlockPosition());
				player.getWorld().addBlockEntity(information.getEntity());
			}

			placeStructures.computeIfAbsent(player, k -> new ArrayList<>());

			cache.add(oldInformation);
		}));

		placeStructures.get(player).add(cache);

		return 1;
	}

	public static int undo(CommandContext<ServerCommandSource> context, Player player) {
		ArrayList<BlockInformation> cache = (ArrayList<BlockInformation>) placeStructures.get(player).get(0);
		placeStructures.get(player).remove(0);

		cache.forEach((information -> {
			if (information.hasState()) {
				player.getWorld().setBlockState(information.getPosition().asBlockPosition(), information.getState());
			}
			if (information.hasEntity()) {
				player.getWorld().removeBlockEntity(information.getPosition().asBlockPosition());
				player.getWorld().addBlockEntity(information.getEntity());
			}
		}));

		return 1;
	}

	public static void initialize() {
		CommandRegistry.INSTANCE.register(false, dispatcher -> {
			LiteralCommandNode<ServerCommandSource> baseNode =
					literal("structure").requires(source -> source.hasPermissionLevel(4)).build();

			LiteralCommandNode<ServerCommandSource> loadNode =
					literal("load")
							.then(argument("name", identifier())
									.suggests(suggestStructures())
									.executes(context -> load(context, getIdentifier(context, "name"), Player.of(context.getSource().getPlayer()))))
							.build();

			LiteralCommandNode<ServerCommandSource> saveNode =
					literal("save")
							.then(argument("name", identifier())
									.suggests(suggestStructures())
									.executes(context -> save(context, getIdentifier(context, "name"), Player.of(context.getSource().getPlayer()), false)))
										.then(argument("air", bool()))
										.executes(context -> save(context, getIdentifier(context, "name"), Player.of(context.getSource().getPlayer()), getBool(context, "air")))
							.build();

			LiteralCommandNode<ServerCommandSource> placeNode =
					literal("place")
							.then(argument("name", identifier())
									.suggests(suggestStructures())
										.executes(context -> place(context, getIdentifier(context, "name"), Player.of(context.getSource().getPlayer()))))
							.build();

			LiteralCommandNode<ServerCommandSource> undoNode =
					literal("undo")
							.executes(context -> undo(context, Player.of(context.getSource().getPlayer())))
					.build();

			LiteralCommandNode<ServerCommandSource> setNode =
					literal("set").build();

			LiteralCommandNode<ServerCommandSource> cornerNode =
					literal("corner").build();

			LiteralCommandNode<ServerCommandSource> oneNode =
					literal("one")
							.executes(context -> set(context, 1))
					.build();

			LiteralCommandNode<ServerCommandSource> twoNode =
					literal("two")
							.executes(context -> set(context, 2))
					.build();

			LiteralCommandNode<ServerCommandSource> originNode =
					literal("origin")
							.executes(context -> set(context, 3))
					.build();



			dispatcher.getRoot().addChild(baseNode);

			baseNode.addChild(setNode);
			baseNode.addChild(loadNode);
			baseNode.addChild(saveNode);
			baseNode.addChild(placeNode);
			baseNode.addChild(undoNode);

			setNode.addChild(cornerNode);
			setNode.addChild(originNode);

			cornerNode.addChild(oneNode);
			cornerNode.addChild(twoNode);
		});
	}
}