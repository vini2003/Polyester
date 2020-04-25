package com.github.vini2003.spork.command;

import com.github.vini2003.spork.api.entity.Player;
import com.github.vini2003.spork.api.lobby.Lobby;
import com.github.vini2003.spork.api.manager.LobbyManager;
import com.github.vini2003.spork.api.preset.Preset;
import com.github.vini2003.spork.api.preset.PresetRegistry;
import com.github.vini2003.spork.api.text.TextWrapper;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.network.MessageType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static net.minecraft.command.arguments.IdentifierArgumentType.getIdentifier;
import static net.minecraft.command.arguments.IdentifierArgumentType.identifier;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class LobbyCommands {
	private static final SimpleCommandExceptionType NAME_NOT_FOUND_EXCEPTION = new SimpleCommandExceptionType(() -> "Argument 'name' not found!");

	public static SuggestionProvider<ServerCommandSource> suggestLobbies() {
		return (context, builder) -> getSuggestionsBuilder(builder, (List<String>) LobbyManager.INSTANCE.getNames().stream().map(Identifier::toString).collect(Collectors.toList()));
	}

	public static SuggestionProvider<ServerCommandSource> suggestPresets() {
		return ((context, builder) -> getSuggestionsBuilder(builder, (List<String>) PresetRegistry.INSTANCE.getNames()));
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

	private static int joinLobby(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Identifier lobbyIdentifier = getIdentifier(context, "name");

		Lobby lobby = LobbyManager.INSTANCE.getLobby(lobbyIdentifier);

		Player player = Player.of(context);

		lobby.bindPlayer(player);

		player.sendChatMessage(
				TextWrapper.builder()
						.with(TextWrapper.translatable("text.spork.lobby_join"))
						.with(TextWrapper.literal(lobbyIdentifier.toString()))
						.build(), MessageType.CHAT
		);
		return 1;
	}

	private static int leaveLobby(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Identifier lobbyIdentifier;

		try {
			lobbyIdentifier = getIdentifier(context, "name");
		} catch (Exception exception) {
			lobbyIdentifier = Player.of(context).getLobby().getIdentifier();
		}

		Lobby lobby = LobbyManager.INSTANCE.getLobby(lobbyIdentifier);

		lobby.unbindPlayer(Player.of(context.getSource().getPlayer()));
		Player player = Player.of(context);
		player.sendChatMessage(
				TextWrapper.builder()
						.with(TextWrapper.translatable("text.spork.lobby_leave"))
						.with(TextWrapper.literal(lobbyIdentifier.toString()))
						.build(), MessageType.CHAT
		);
		return 1;
	}

	private static int createLobby(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Identifier lobbyIdentifier = getIdentifier(context, "name");

		try {
			LobbyManager.INSTANCE.add(new Lobby(lobbyIdentifier));
		} catch (IllegalArgumentException exception) {
			throw new CommandSyntaxException(NAME_NOT_FOUND_EXCEPTION, () -> "'lobby create' requires a name!");
		}

		Player player = Player.of(context);
		player.sendChatMessage(
				TextWrapper.builder()
						.with(TextWrapper.translatable("text.spork.lobby_create"))
						.with(TextWrapper.literal(lobbyIdentifier.toString()))
						.build(), MessageType.CHAT
		);
		return 1;
	}

	private static int removeLobby(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Identifier lobbyIdentifier = getIdentifier(context, "name");

		LobbyManager.INSTANCE.remove(lobbyIdentifier);

		Player player = Player.of(context);
		player.sendChatMessage(
				TextWrapper.builder()
						.with(TextWrapper.translatable("text.spork.lobby_remove"))
						.with(TextWrapper.literal(lobbyIdentifier.toString()))
						.build(), MessageType.CHAT
		);
		return 1;
	}

	private static int setLobbyPreset(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Identifier lobbyIdentifier;

		try {
			lobbyIdentifier = getIdentifier(context, "name");
		} catch (Exception exception) {
			lobbyIdentifier = Player.of(context).getLobby().getIdentifier();
		}

		Identifier presetIdentifier = getIdentifier(context, "preset");

		Lobby lobby = LobbyManager.INSTANCE.getLobby(lobbyIdentifier);

		Preset preset = PresetRegistry.INSTANCE.getByIdentifier(presetIdentifier);

		lobby.bindPreset(preset);

		Player player = Player.of(context);
		player.sendChatMessage(
				TextWrapper.builder()
						.with(TextWrapper.translatable("text.spork.lobby_preset_set_one"))
						.with(TextWrapper.literal(lobbyIdentifier.toString()))
						.with(TextWrapper.translatable("text.spork.lobby_preset_set_two"))
						.with(TextWrapper.literal(presetIdentifier.toString()))
						.build(), MessageType.CHAT
		);

		return 1;
	}

	private static int beginLobbyPreset(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Identifier lobbyIdentifier;

		try {
			lobbyIdentifier = getIdentifier(context, "name");
		} catch (Exception exception) {
			lobbyIdentifier = Player.of(context).getLobby().getIdentifier();
		}

		Lobby lobby = LobbyManager.INSTANCE.getLobby(lobbyIdentifier);

		lobby.getPreset().apply(lobby);

		Player player = Player.of(context);
		player.sendChatMessage(
				TextWrapper.builder()
						.with(TextWrapper.translatable("text.spork.lobby_preset_begin_one"))
						.with(TextWrapper.literal(lobbyIdentifier.toString()))
						.with(TextWrapper.translatable("text.spork.lobby_preset_begin_two"))
						.with(TextWrapper.literal(lobby.getPreset().getIdentifier().toString()))
						.build(), MessageType.CHAT
		);

		return 1;
	}

	private static int endLobbyPreset(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Identifier lobbyIdentifier;

		try {
			lobbyIdentifier = getIdentifier(context, "name");
		} catch (Exception exception) {
			lobbyIdentifier = Player.of(context).getLobby().getIdentifier();
		}

		Lobby lobby = LobbyManager.INSTANCE.getLobby(lobbyIdentifier);

		lobby.getPreset().retract(lobby);

		Player player = Player.of(context);
		player.sendChatMessage(
				TextWrapper.builder()
						.with(TextWrapper.translatable("text.spork.lobby_preset_end_one"))
						.with(TextWrapper.literal(lobbyIdentifier.toString()))
						.with(TextWrapper.translatable("text.spork.lobby_preset_end_two"))
						.with(TextWrapper.literal(lobby.getPreset().getIdentifier().toString()))
						.build(), MessageType.CHAT
		);

		return 1;
	}

	private static int restartLobbyPreset(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Identifier lobbyIdentifier;

		try {
			lobbyIdentifier = getIdentifier(context, "name");
		} catch (Exception exception) {
			lobbyIdentifier = Player.of(context).getLobby().getIdentifier();
		}

		Lobby lobby = LobbyManager.INSTANCE.getLobby(lobbyIdentifier);

		lobby.getPreset().restart(lobby);

		Player player = Player.of(context);
		player.sendChatMessage(
				TextWrapper.builder()
						.with(TextWrapper.translatable("text.spork.lobby_preset_restart_one"))
						.with(TextWrapper.literal(lobbyIdentifier.toString()))
						.with(TextWrapper.translatable("text.spork.lobby_preset_restart_two"))
						.with(TextWrapper.literal(lobby.getPreset().getIdentifier().toString()))
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
							.then(argument("name", identifier())
									.suggests(suggestLobbies())
									.executes(LobbyCommands::joinLobby))
							.build();

			LiteralCommandNode<ServerCommandSource> leaveNode =
					literal("leave")
							.then(argument("name", identifier())
									.suggests(suggestLobbies())
									.executes(LobbyCommands::leaveLobby))
							.executes(LobbyCommands::leaveLobby)
							.build();

			LiteralCommandNode<ServerCommandSource> createNode =
					literal("create")
							.then(argument("name", identifier())
									.executes(LobbyCommands::createLobby))
							.build();

			LiteralCommandNode<ServerCommandSource> removeNode =
					literal("remove")
							.then(argument("name", identifier())
									.suggests(suggestLobbies())
									.executes(LobbyCommands::removeLobby))
							.build();

			LiteralCommandNode<ServerCommandSource> presetNode =
					literal("preset").build();

			LiteralCommandNode<ServerCommandSource> setNode =
					literal("set")
							.then(argument("preset", identifier())
									.suggests(suggestPresets())
									.then(argument("name", identifier())
											.suggests(suggestLobbies())
											.executes(LobbyCommands::setLobbyPreset))
									.executes(LobbyCommands::setLobbyPreset))
							.build();

			LiteralCommandNode<ServerCommandSource> beginNode =
					literal("begin")
							.then(argument("name", identifier())
									.suggests(suggestLobbies())
									.executes(LobbyCommands::beginLobbyPreset))
							.executes(LobbyCommands::beginLobbyPreset)
							.build();

			LiteralCommandNode<ServerCommandSource> endNode =
					literal("end")
							.then(argument("name", identifier())
									.suggests(suggestLobbies())
									.executes(LobbyCommands::endLobbyPreset))
							.executes(LobbyCommands::endLobbyPreset)
							.build();

			LiteralCommandNode<ServerCommandSource> restartNode =
					literal("restart")
							.then(argument("name", identifier())
									.suggests(suggestLobbies())
									.executes(LobbyCommands::restartLobbyPreset))
							.executes(LobbyCommands::restartLobbyPreset)
							.build();

			dispatcher.getRoot().addChild(baseNode);

			baseNode.addChild(joinNode);
			baseNode.addChild(leaveNode);
			baseNode.addChild(createNode);
			baseNode.addChild(removeNode);
			baseNode.addChild(presetNode);

			presetNode.addChild(setNode);
			presetNode.addChild(beginNode);
			presetNode.addChild(endNode);
			presetNode.addChild(restartNode);
		});
	}
}
