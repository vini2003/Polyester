package com.github.vini2003.spork.command;

import com.github.vini2003.spork.api.entity.Player;
import com.github.vini2003.spork.api.lobby.Lobby;
import com.github.vini2003.spork.api.manager.LobbyManager;
import com.github.vini2003.spork.api.text.TextWrapper;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.network.MessageType;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static net.minecraft.server.command.CommandManager.literal; // literal("foo")
import static net.minecraft.server.command.CommandManager.argument;

public class LobbyCommands {
	private static final SimpleCommandExceptionType NAME_NOT_FOUND_EXCEPTION = new SimpleCommandExceptionType(() -> "Argument 'name' not found!");

	public static SuggestionProvider<ServerCommandSource> suggestLobbies() {
		return (context, builder) -> getSuggestionsBuilder(builder, (List<String>) LobbyManager.getNames());
	}

	private static CompletableFuture<Suggestions> getSuggestionsBuilder(SuggestionsBuilder builder, List<String> list) {
		String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

		if(list.isEmpty()) {
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

	private static int joinLobby(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Lobby lobby = LobbyManager.getLobby(getString(context, "name"));
		lobby.bindPlayer(Player.of(context.getSource().getPlayer()));
		Player player = Player.of(context);
		player.sendChatMessage(
				TextWrapper.builder()
						.with(TextWrapper.literal("§7§o>> " + context.getInput() + "§r\n"))
						.with(TextWrapper.translatable("text.spork.lobby_join"))
						.with(TextWrapper.literal(getString(context, "name")))
						.build(), MessageType.CHAT
		);
		return 1;
	}

	private static int leaveLobby(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Lobby lobby = LobbyManager.getLobby(getString(context, "name"));
		lobby.unbindPlayer(Player.of(context.getSource().getPlayer()));
		Player player = Player.of(context);
		player.sendChatMessage(
				TextWrapper.builder()
						.with(TextWrapper.literal("§7§o>> " + context.getInput() + "§r\n"))
						.with(TextWrapper.translatable("text.spork.lobby_leave"))
						.with(TextWrapper.literal(getString(context, "name")))
						.build(), MessageType.CHAT
		);
		return 1;
	}

	private static int createLobby(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		try {
			LobbyManager.add(new Lobby(getString(context, "name")));
		} catch (IllegalArgumentException exception) {
			throw new CommandSyntaxException(NAME_NOT_FOUND_EXCEPTION, () -> "'lobby create' requires a name!");
		}
		Player player = Player.of(context);
		player.sendChatMessage(
				TextWrapper.builder()
						.with(TextWrapper.literal("§7§o>> " + context.getInput() + "§r\n"))
						.with(TextWrapper.translatable("text.spork.lobby_create"))
						.with(TextWrapper.literal(getString(context, "name")))
						.build(), MessageType.CHAT
		);
		return 1;
	}

	private static int removeLobby(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		LobbyManager.remove(getString(context, "name"));
		Player player = Player.of(context);
		player.sendChatMessage(
				TextWrapper.builder()
					.with(TextWrapper.literal("§7§o>> " + context.getInput() + "§r\n"))
					.with(TextWrapper.translatable("text.spork.lobby_remove"))
					.with(TextWrapper.literal(getString(context, "name")))
				.build(), MessageType.CHAT
		);
		return 1;
	}

	public static void initialize() {
		CommandRegistry.INSTANCE.register(false, dispatcher -> {
			LiteralCommandNode<ServerCommandSource> baseNode =
					literal("lobby").build();

			LiteralCommandNode<ServerCommandSource> joinNode =
					literal("join")
						.then(argument("name", string())
							.suggests(suggestLobbies())
								.executes(LobbyCommands::joinLobby))
					.build();

			LiteralCommandNode<ServerCommandSource> leaveNode =
					literal("leave")
						.then(argument("name", string())
							.suggests(suggestLobbies())
								.executes(LobbyCommands::leaveLobby))
					.build();

			LiteralCommandNode<ServerCommandSource> createNode =
					literal("create")
						.then(argument("name", string())
							.executes(LobbyCommands::createLobby))
					.build();

			LiteralCommandNode<ServerCommandSource> removeNode =
					literal("remove")
						.then(argument("name", string())
							.suggests(suggestLobbies())
								.executes(LobbyCommands::removeLobby))
					.build();

			dispatcher.getRoot().addChild(baseNode);
			baseNode.addChild(joinNode);
			baseNode.addChild(leaveNode);
			baseNode.addChild(createNode);
			baseNode.addChild(removeNode);
		});
	}
}
