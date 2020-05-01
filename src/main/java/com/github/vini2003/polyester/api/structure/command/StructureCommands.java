package com.github.vini2003.polyester.api.structure.command;

import com.github.vini2003.polyester.api.block.BlockInformation;
import com.github.vini2003.polyester.api.data.Position;
import com.github.vini2003.polyester.api.entity.Player;
import com.github.vini2003.polyester.api.structure.Structure;
import com.github.vini2003.polyester.api.structure.StructureManager;
import com.github.vini2003.polyester.api.structure.registry.StructureRegistry;
import com.github.vini2003.polyester.api.text.TextBuilder;
import com.github.vini2003.polyester.utility.RayUtilities;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.network.MessageType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RayTraceContext;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static net.minecraft.command.arguments.IdentifierArgumentType.getIdentifier;
import static net.minecraft.command.arguments.IdentifierArgumentType.identifier;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class StructureCommands {
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

	private static int setCorner(CommandContext<ServerCommandSource> context, StructureManager.Corner corner) throws CommandSyntaxException {
		Position position = Position.of(((BlockHitResult) RayUtilities.rayTrace(context.getSource().getWorld(), context.getSource().getPlayer(), RayTraceContext.FluidHandling.ANY)).getBlockPos());

		Player player = Player.of(context.getSource().getPlayer());

		StructureManager.setCorner(corner, player, position);


		player.sendChatMessage(
				TextBuilder.builder()
						.with(new LiteralText("§o§dSelected structure corner " + corner.toString() + " as " + position.toCoordinates()))
						.build()
		);

		return 1;
	}

	private static int setAnchor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Position position = Position.of(((BlockHitResult) RayUtilities.rayTrace(context.getSource().getWorld(), context.getSource().getPlayer(), RayTraceContext.FluidHandling.ANY)).getBlockPos());

		Player player = Player.of(context.getSource().getPlayer());

		StructureManager.setAnchor(player, position);

		StructureManager.setAnchor(Player.of(context.getSource().getPlayer()), position);

		player.sendChatMessage(
				TextBuilder.builder()
						.with(new LiteralText("§o§dSelected structure anchor at " + position.toCoordinates() + "!"))
						.build(), MessageType.CHAT
		);

		return 1;
	}

	private static int loadStructure(CommandContext<ServerCommandSource> context, Identifier identifier, Player player) {
		StructureManager.setStructure(StructureRegistry.INSTANCE.getByKey(identifier), player);

		return 1;
	}

	private static int saveStructure(CommandContext<ServerCommandSource> context, Identifier identifier, Player player, boolean includeAir) {
		StructureManager.saveStructure(player.getWorld(), StructureManager.getAnchor(player).asBlockPosition(), StructureManager.getCorner(StructureManager.Corner.FIRST, player).asBlockPosition(), StructureManager.getCorner(StructureManager.Corner.SECOND, player).asBlockPosition(), identifier, includeAir);

		return 1;
	}

	private static int placeStructure(CommandContext<ServerCommandSource> context, Identifier identifier, Player player) {
		StructureManager.placeStructure(player, player.getWorld(), StructureManager.getAnchor(player).asBlockPosition(), identifier);

		return 1;
	}

	public static int undo(CommandContext<ServerCommandSource> context, Player player) {
		StructureManager.undoStructure(player);

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
									.executes(context -> loadStructure(context, getIdentifier(context, "name"), Player.of(context.getSource().getPlayer()))))
							.build();

			LiteralCommandNode<ServerCommandSource> saveNode =
					literal("save")
							.then(argument("name", identifier())
									.suggests(suggestStructures())
									.executes(context -> saveStructure(context, getIdentifier(context, "name"), Player.of(context.getSource().getPlayer()), false)))
										.then(argument("air", bool()))
										.executes(context -> saveStructure(context, getIdentifier(context, "name"), Player.of(context.getSource().getPlayer()), getBool(context, "air")))
							.build();

			LiteralCommandNode<ServerCommandSource> placeNode =
					literal("place")
							.then(argument("name", identifier())
									.suggests(suggestStructures())
										.executes(context -> placeStructure(context, getIdentifier(context, "name"), Player.of(context.getSource().getPlayer()))))
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
					literal("first")
							.executes(context -> setCorner(context, StructureManager.Corner.FIRST))
					.build();

			LiteralCommandNode<ServerCommandSource> twoNode =
					literal("second")
							.executes(context -> setCorner(context, StructureManager.Corner.SECOND))
					.build();

			LiteralCommandNode<ServerCommandSource> originNode =
					literal("anchor")
							.executes(StructureCommands::setAnchor)
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