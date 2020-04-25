package com.github.vini2003.example;

import com.github.vini2003.spork.api.data.Tracker;
import com.github.vini2003.spork.api.entity.Player;
import com.github.vini2003.spork.api.event.EventResult;
import com.github.vini2003.spork.api.event.type.block.BlockStepEvent;
import com.github.vini2003.spork.api.event.type.lobby.LobbyBindPlayerEvent;
import com.github.vini2003.spork.api.event.type.lobby.LobbyUnbindPlayerEvent;
import com.github.vini2003.spork.api.event.type.player.PlayerDamageEvent;
import com.github.vini2003.spork.api.lobby.Lobby;
import com.github.vini2003.spork.api.preset.Preset;
import com.github.vini2003.spork.api.text.TextWrapper;
import com.github.vini2003.spork.utility.DimensionUtilities;
import com.github.vini2003.spork.utility.WorldUtilities;
import net.minecraft.block.Blocks;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ExamplePreset extends Preset {
	public static final Identifier IDENTIFIER = new Identifier("spork", "debug");

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
		lobby.getPlayers().forEach(player -> player.sendMessage(TextWrapper.builder().with("The round has started!").build()));
	});

	private final BlockStepEvent.Listener stepListener = ((world, entity, data) -> {
		if (entity instanceof PlayerEntity) {
			Player player = Player.of((PlayerEntity) entity);

			if (player.hasPreset() && player.getPresetIdentifier().equals(IDENTIFIER)) {
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
			if (lobby.getPreset().getIdentifier().equals(ExamplePreset.IDENTIFIER)) {
				DimensionUtilities.teleport(player.target(), DimensionType.OVERWORLD, new BlockPos(0, 64, 0));
			}
		}

		return EventResult.CONTINUE;
	});

	private static final PlayerDamageEvent.Listener damagePlayerListener = (player, source, amount) -> {
		if (source == DamageSource.OUT_OF_WORLD) {
			if (player.hasLobby() && player.getPresetIdentifier().equals(IDENTIFIER) && !player.getWorld().isClient()) {
				player.setGameMode(GameMode.SPECTATOR);

				ArrayList<Player> players = (ArrayList<Player>) player.getLobby().getPlayers();

				if (players.size() > 1) {
					player.target().setCameraEntity(players.get(player.getWorld().random.nextInt(players.size())).target());
				}

				Lobby lobby = player.getLobby();

				if (lobby.getPlayers().stream().noneMatch(member -> member.getGameMode() == GameMode.ADVENTURE)) {
					lobby.getPreset().restart(lobby);
					lobby.getPlayers().forEach(member -> {
						member.sendMessage(TextWrapper.builder()
								.with(player.getName() + " wins this round!")
								.build());

						member.setGameMode(GameMode.ADVENTURE);

						DimensionUtilities.teleport(member.target(), lobby.getDimension(), new BlockPos(8, 26, 8));
					});

					lobby.getPreset().restart(lobby);
				}
				return EventResult.CANCEL;
			}
		} else if (player.getPresetIdentifier().equals(IDENTIFIER)) {
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
			Pair<Identifier, DimensionType> superFlat = DimensionUtilities.getSuperFlat();

			lobby.bindDimension(superFlat.getRight());
			lobby.bindWorld(WorldUtilities.getWorld(superFlat.getRight()));
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
