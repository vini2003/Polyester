package com.github.vini2003.spork.api.block;

import com.github.vini2003.spork.api.data.Position;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A BlockData is a collection of
 * data for a given position,
 * which contains its attached
 * state, block and entity.
 */
public class BlockData {
	private Position position;
	private BlockState state;
	private Block block;
	private BlockEntity entity;

	/**
	 * Instantiates a new block data based on a
	 * position, state, block and entity.
	 *
	 * @param position the specified position.
	 * @param state    the specified state.
	 * @param block    the specified block.
	 * @param entity   the specified entity.
	 */
	public BlockData(BlockPos position, BlockState state, Block block, BlockEntity entity) {
		this.position = Position.of(position);
		this.state = state;
		this.block = block;
		this.entity = entity;
	}

	/**
	 * Instantiates a new block data based on a
	 * world and a hit result.
	 *
	 * @param world  the specified world.
	 * @param result the specified hit result.
	 * @return the requested data.
	 */
	public static BlockData of(World world, HitResult result) {
		return of(world, (BlockHitResult) result);
	}

	/**
	 * Instantiates a new block data based on a
	 * world and a block hit result.
	 *
	 * @param world  the specified world.
	 * @param result the specified hit result.
	 * @return the requested data.
	 */
	public static BlockData of(World world, BlockHitResult result) {
		BlockPos position = result.getBlockPos();
		BlockState state = world.getBlockState(position);
		Block block = state.getBlock();
		BlockEntity entity = world.getBlockEntity(position);

		return new BlockData(position, state, block, entity);
	}

	/**
	 * Instantiates a new block data based on a
	 * world and a position.
	 *
	 * @param world    the specified world.
	 * @param position the specified position.
	 * @return the requested data.
	 */
	public static BlockData of(World world, BlockPos position) {
		BlockState state = world.getBlockState(position);
		Block block = state.getBlock();
		BlockEntity entity = world.getBlockEntity(position);

		return new BlockData(position, state, block, entity);
	}

	/**
	 * Asserts whether this data has a position or not.
	 *
	 * @return true if yes; false if no.
	 */
	public boolean hasPosition() {
		return position != null;
	}

	/**
	 * Asserts whether this data has a state or not.
	 *
	 * @return true if yes; false if no.
	 */
	public boolean hasState() {
		return state != null;
	}

	/**
	 * Asserts whether this data has a block or not.
	 *
	 * @return true if yes; false if no.
	 */
	public boolean hasBlock() {
		return block != null;
	}

	/**
	 * Asserts whether this data has an entity or not.
	 *
	 * @return true if yes; false if no.
	 */
	public boolean hasEntity() {
		return entity != null;
	}

	/**
	 * Retrieves this data's position.
	 *
	 * @return the requested position.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Retrieves this data's state.
	 *
	 * @return the requested state.
	 */
	public BlockState getState() {
		return state;
	}

	/**
	 * Retrieves this data's block.
	 *
	 * @return the requested block.
	 */
	public Block getBlock() {
		return block;
	}

	/**
	 * Retrieves this data's entity.
	 *
	 * @return the requested entity.
	 */
	public BlockEntity getEntity() {
		return entity;
	}
}
