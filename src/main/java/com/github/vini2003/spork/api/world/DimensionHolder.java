package com.github.vini2003.spork.api.world;

import net.minecraft.world.dimension.DimensionType;

public interface DimensionHolder {
	/**
	 * Retrieves the dimension managed by this holder.
	 *
	 * @return the requested dimension.
	 */
	DimensionType getDimension();

	/**
	 * Binds a dimension to this holder.
	 *
	 * @param dimension the dimension to be bound.
	 */
	void bindDimension(DimensionType dimension);

	/**
	 * Unbinds the dimension of this holder.
	 */
	void unbindDimension();
}
