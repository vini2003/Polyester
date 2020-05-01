package com.github.vini2003.example;

import com.github.vini2003.polyester.Polyester;
import com.github.vini2003.polyester.api.text.TextBuilder;
import com.github.vini2003.polyester.api.tracker.Tracker;
import com.github.vini2003.polyester.api.dimension.registry.DimensionRegistry;
import com.github.vini2003.polyester.api.dimension.utilities.DimensionUtilities;
import com.github.vini2003.polyester.api.entity.Player;
import com.github.vini2003.polyester.api.event.EventResult;
import com.github.vini2003.polyester.api.event.type.block.BlockStepEvent;
import com.github.vini2003.polyester.api.event.type.lobby.LobbyBindPlayerEvent;
import com.github.vini2003.polyester.api.event.type.lobby.LobbyUnbindPlayerEvent;
import com.github.vini2003.polyester.api.event.type.player.PlayerDamageEvent;
import com.github.vini2003.polyester.api.lobby.Lobby;
import com.github.vini2003.polyester.api.preset.Preset;
import com.github.vini2003.polyester.utility.WorldUtilities;
import net.minecraft.block.Blocks;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ExamplePreset extends Preset {
	public static final Identifier IDENTIFIER = new Identifier(Polyester.IDENTIFIER, "debug");

	private static final Identifier SMALL_TNT_RUN = new Identifier("tnt_run_small");

	private final Tracker<Integer> startTracker = new Tracker<Integer>(0) {
		@Override
		public void tick() {
			setValue(getValue() + 1);
		}
	};

	private final Predicate<Lobby> feedPredicate = (lobby -> true);

	private final Consumer<Lobby> feedAction = (lobby -> {
		lobby.getPlayers().forEach(player -> player.target().getHungerManager().setFoodLevel(40));
	});

	private final Predicate<Lobby> startPredicate = (state -> {
		return state.getTrackers().get(startTracker).getValue().equals(200);
	});

	private final Consumer<Lobby> startAction = (lobby -> {
		lobby.getPlayers().forEach(player -> player.sendMessage(TextBuilder.builder().with("§2§lThe round has started!").build()));
	});

	private final BlockStepEvent.Listener stepListener = ((world, entity, data) -> {
		if (entity instanceof PlayerEntity) {
			Player player = Player.of((PlayerEntity) entity);

			if (player.hasPreset() && player.getPresetIdentifier().equals(IDENTIFIER) && player.getGameMode() == GameMode.ADVENTURE) {
				BlockPos sandPosition = data.getPosition().asBlockPosition();
				BlockPos tntPosition = sandPosition.offset(Direction.DOWN);

				if (world.getBlockState(sandPosition).getBlock() == Blocks.SAND && world.getBlockState(tntPosition).getBlock() == Blocks.TNT) {
					if (!player.getLobby().getTrackers().containsKey(sandPosition)) {
						player.getLobby().getTrackers().put(sandPosition, new Tracker<Integer>(0) {
							@Override
							public void tick() {
								setValue(getValue() + 1);
							}
						});

						Predicate<Lobby> predicate = (lobby -> {
							return lobby.getTrackers().get(sandPosition).getValue().equals(5);
						});

						Consumer<Lobby> action = (lobby -> {
							lobby.unqueueAction(predicate);

							lobby.getTrackers().remove(sandPosition);

							lobby.getWorld().setBlockState(sandPosition, Blocks.AIR.getDefaultState());
							lobby.getWorld().setBlockState(tntPosition, Blocks.AIR.getDefaultState());
						});

						player.getLobby().enqueueAction(predicate, action);
					}
				}
			}
		}
		return EventResult.CONTINUE;
	});

	private static final LobbyBindPlayerEvent.Listener bindPlayerListener = ((lobby, player) -> {
		if (!player.getWorld().isClient()) {
			if (lobby.getPreset().getIdentifier().equals(ExamplePreset.IDENTIFIER)) {
				DimensionUtilities.teleport(player.target(), lobby.getDimension(), new BlockPos(8, 26, 8));
			}
		}

		return EventResult.CONTINUE;
	});

	private static final LobbyUnbindPlayerEvent.Listener unbindPlayerListener = ((lobby, player) -> {
		if (!player.getWorld().isClient()) {
			if (player.hasPreset() && lobby.getPreset().getIdentifier().equals(ExamplePreset.IDENTIFIER)) {
				DimensionUtilities.teleport(player.target(), DimensionType.OVERWORLD, new BlockPos(0, 64, 0));
			}
		}

		return EventResult.CONTINUE;
	});

	private static final PlayerDamageEvent.Listener damagePlayerListener = (player, source, amount) -> {
		if (source == DamageSource.OUT_OF_WORLD) {
			if (player.hasLobby() && player.getPresetIdentifier().equals(IDENTIFIER) && !player.getWorld().isClient() && player.getGameMode() != GameMode.SPECTATOR) {
				player.setGameMode(GameMode.SPECTATOR);

				ArrayList<Player> players = (ArrayList<Player>) player.getLobby().getPlayers();

				players.forEach(member -> {
					if (member != player) {
						member.sendChatMessage(
								TextBuilder.builder()
										.with(new LiteralText("§6§o§l" + player.getName()))
										.with(new LiteralText("§r§7§ohas been consumed by the void!" + (players.size() > 1 ? " §r§8§o(" + players.stream().filter(target -> target.getGameMode() != GameMode.SPECTATOR).count() + "/" + players.size() + ") remain!" : "")))
										.build(), MessageType.CHAT
						);
					}
				});

				if (players.size() > 1) {
					player.target().setCameraEntity(players.get(player.getWorld().random.nextInt(players.size())).target());
				}

				Lobby lobby = player.getLobby();

				if (lobby.getPlayers().stream().noneMatch(member -> member.getGameMode() == GameMode.ADVENTURE)) {
					lobby.getPreset().restart(lobby);
					lobby.getPlayers().forEach(member -> {
						member.sendMessage(TextBuilder.builder()
								.with("§6§o§l" + player.getName())
								.with(new LiteralText("§r§7§owins this round!"))
								.build());

						member.setGameMode(GameMode.ADVENTURE);

						DimensionUtilities.teleport(member.target(), lobby.getDimension(), new BlockPos(8, 26, 8));
					});

					lobby.getPreset().restart(lobby);
				}
				return EventResult.CANCEL;
			}
		} else if (player.hasPreset() && player.getPresetIdentifier().equals(IDENTIFIER)) {
			return EventResult.CANCEL;
		}
		return EventResult.CONTINUE;
	};

	@Override
	public Identifier getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void apply(Lobby lobby) {
		startTracker.setValue(0);

		if (!lobby.hasDimension()) {
			DimensionType superFlat = DimensionRegistry.INSTANCE.getByKey(DimensionUtilities.getVoidDimension());

			lobby.bindDimension(superFlat);
			lobby.bindWorld(WorldUtilities.getWorld(superFlat));
		}

		lobby.getWorld().getStructureManager().getStructure(SMALL_TNT_RUN).place(lobby.getWorld(), new BlockPos(0, 0, 0), new StructurePlacementData());

		lobby.getTrackers().put(startTracker, startTracker);

		lobby.enqueueAction(startPredicate, startAction);

		lobby.enqueueAction(feedPredicate, feedAction);

		lobby.enqueueAction((state -> startTracker.getValue() == 200), (state -> {
			BlockStepEvent.register(stepListener);
		}));

		LobbyBindPlayerEvent.register(bindPlayerListener);

		LobbyUnbindPlayerEvent.register(unbindPlayerListener);

		PlayerDamageEvent.register(damagePlayerListener);
	}

	@Override
	public void retract(Lobby lobby) {
		lobby.unqueueAllActions();

		lobby.getTrackers().remove(startTracker);

		LobbyBindPlayerEvent.unregister(bindPlayerListener);

		LobbyUnbindPlayerEvent.unregister(unbindPlayerListener);

		BlockStepEvent.unregister(stepListener);

		PlayerDamageEvent.unregister(damagePlayerListener);
	}

	@Override
	public void restart(Lobby lobby) {
		retract(lobby);
		apply(lobby);
	}
}
