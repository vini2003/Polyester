package com.github.vini2003.polyester.api.structure;

import com.github.vini2003.polyester.api.block.BlockInformation;
import com.github.vini2003.polyester.api.data.Position;
import com.github.vini2003.polyester.api.entity.Player;
import com.github.vini2003.polyester.api.structure.registry.StructureRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class StructureManager {
	public enum Corner {
		FIRST,
		SECOND;


		@Override
		public String toString() {
			return this == FIRST ? "first" : "second";
		}
	}

	public static final Map<Player, Position> firstCorners = new HashMap<>();
	public static final Map<Player, Position> secondCorners = new HashMap<>();

	public static final Map<Player, Position> anchors = new HashMap<>();

	public static final Map<Player, Structure> boundStructures = new HashMap<>();

	public static final Map<Player, List<List<BlockInformation>>> cachedChanges = new HashMap<>();

	public static void setCorner(Corner corner, Player player, Position position) {
		(corner == Corner.FIRST ? firstCorners : secondCorners).put(player, position);
	}

	public static Position getCorner(Corner corner, Player player) {
		return (corner == Corner.FIRST ? firstCorners : secondCorners).get(player);
	}

	public static void setAnchor(Player player, Position position) {
		anchors.put(player, position);
	}

	public static Position getAnchor(Player player) {
		return anchors.get(player);
	}

	public static void setStructure(Structure structure, Player player) {
		boundStructures.put(player, structure);
	}

	public static Structure getStructure(Player player) {
		return boundStructures.get(player);
	}

	public static void loadStructure(Player player, Identifier identifier) {
		setStructure(StructureRegistry.INSTANCE.getByKey(identifier), player);
	}

	public static void saveStructure(World world, BlockPos anchor, BlockPos positionA, BlockPos positionB, Identifier identifier, boolean includeAir) {
		Structure structure = new Structure();

		Iterable<BlockPos> iterable = BlockPos.Mutable.iterate(positionA, positionB);
		Iterator<BlockPos> iterator = iterable.iterator();

		iterator.forEachRemaining(position -> {
			BlockInformation blockInformation = BlockInformation.of(world, position);

			if (!includeAir && blockInformation.hasState() && !blockInformation.getState().isAir()) {
				structure.add(Position.of(new BlockPos(anchor.getX() - position.getX(), anchor.getY() - position.getY(), anchor.getZ() - position.getZ())), blockInformation);
			}
		});

		StructureRegistry.INSTANCE.register(identifier, structure);
	}

	public static void placeStructure(Player player, World world, BlockPos anchor, Identifier identifier) {
		Structure structure = StructureRegistry.INSTANCE.getByKey(identifier);

		ArrayList<BlockInformation> cache = new ArrayList<>();

		if (player != null) {
			cachedChanges.computeIfAbsent(player, key -> new ArrayList<>());
		}

		structure.blocks.forEach(((rawPosition, serializedInformation) -> {
			BlockPos position = rawPosition.asBlockPosition();

			BlockPos newPosition = new BlockPos(anchor.getX() + position.getX(), anchor.getY() - position.getY(), anchor.getZ() + position.getZ());

			BlockInformation information = BlockInformation.deserialize(serializedInformation);

			BlockInformation oldInformation = BlockInformation.of(world, newPosition);

			if (information.hasState()) {
				world.setBlockState(newPosition, information.getState());
			}

			if (information.hasEntity()) {
				information.getEntity().setLocation(world, newPosition);

				world.getBlockEntity(newPosition).fromTag(information.getEntity().toTag(new CompoundTag()));
			}

			if (player != null) {
				cachedChanges.computeIfAbsent(player, k -> new ArrayList<>());
			}

			cache.add(oldInformation);
		}));

		if (player != null) {
			cachedChanges.get(player).add(cache);
		}
	}

	public static void undoStructure(Player player) {
		ArrayList<BlockInformation> cache = (ArrayList<BlockInformation>) cachedChanges.get(player).get(0);

		cachedChanges.get(player).remove(0);

		cache.forEach((information -> {
			if (information.hasState()) {
				player.getWorld().setBlockState(information.getPosition().asBlockPosition(), information.getState());
			}
			if (information.hasEntity()) {
				player.getWorld().removeBlockEntity(information.getPosition().asBlockPosition());
				player.getWorld().addBlockEntity(information.getEntity());
			}
		}));
	}
}
