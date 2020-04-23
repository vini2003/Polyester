package com.github.vini2003.spork.utility;

import com.github.vini2003.spork.api.data.Position;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;

public interface WorldUtilities {
	World getWorld();

	/**
	 * Instantiates a world utilities on a world.
	 *
	 * @param world the specified world.
	 * @return      the requested world utilities.
	 */
	static WorldUtilities of(World world) {
		return (WorldUtilities) world;
	}

	/**
	 * Sets the block in a given position to the specified block.
	 *
	 * @param position the specified position.
	 * @param block    the specified block.
	 */
	default void setBlock(Position position, Block block) {
		getWorld().setBlockState(position.asBlockPosition(), block.getDefaultState());
	}

	/**
	 * Sets the state in a given position to the specified state.
	 *
	 * @param position the specified position.
	 * @param state    the specified state.
	 */
	default void setState(Position position, BlockState state) {
		getWorld().setBlockState(position.asBlockPosition(), state);
	}

	/**
	 * Retrieves the block in a given position.
	 *
	 * @param position the specified position.
	 * @return the requested block.
	 */
	default Block getBlock(Position position) {
		return getWorld().getBlockState(position.asBlockPosition()).getBlock();
	}

	/**
	 * Retrieves the state in a given position.
	 *
	 * @param position the specified position.
	 * @return the requested block.
	 */
	default BlockState getState(Position position) {
		return getWorld().getBlockState(position.asBlockPosition());
	}

	/**
	 * Retrieves the block entity in a given position.
	 *
	 * @param position the specified position.
	 * @return the requested entity.
	 */
	default BlockEntity getBlockEntity(Position position) {
		return getWorld().getBlockEntity(position.asBlockPosition());
	}

	/**
	 * Asserts whether a given position is loaded or not.
	 *
	 * @param position the specified position.
	 * @return true if yes; false if no.
	 */
	default boolean isLoaded(Position position) {
		return getWorld().isChunkLoaded(((int) position.x()), ((int) position.z()));
	}

	/**
	 * Retrieves the highest surface position in a given position.
	 *
	 * @param position the specified position.
	 * @return the requested position.
	 */
	default Position getHighestPosition(Position position) {
		return Position.of(getWorld().getTopPosition(Heightmap.Type.WORLD_SURFACE, position.asBlockPosition()));
	}

	/**
	 * Retrieves a collection of positions scattered
	 * around a central point in circle form.
	 *
	 * @param center the specified center.
	 * @param count  the position count, and circle radius.
	 * @return the requested positions.
	 */
	default Collection<Position> getScatteredPositions(Position center, int count) {
		ArrayList<Position> positions = new ArrayList<>();
		double radiusSquared = Math.pow(count, 2);
		while (positions.size() < count) {
			for (int z = 0; z < Math.max(1, count); z++) {
				for (int x = 0; x < Math.max(1, count); x++) {
					Position position = Position.of(x, 0, z);
					double squaredDistance = center.getSquaredDistance(position);
					if (squaredDistance >= radiusSquared - 1 && squaredDistance <= radiusSquared + 1) {
						positions.add(getHighestPosition(position));
					}
				}
			}
		}
		return positions;
	}

	/**
	 * Adds an entity to the world.
	 *
	 * @param entity the specified entity.
	 */
	default void addEntity(Entity entity) {
		getWorld().spawnEntity(entity);
	}

	/**
	 * Removes an entity from the world.
	 *
	 * @param entity the specified entity.
	 */
	default void removeEntity(Entity entity) {
		entity.remove();
	}
}
