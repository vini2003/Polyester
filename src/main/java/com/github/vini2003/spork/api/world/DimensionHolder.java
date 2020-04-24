package com.github.vini2003.spork.api.world;

import com.github.vini2003.spork.api.entity.Player;
import net.minecraft.world.dimension.Dimension;

import java.util.Collection;

public interface DimensionHolder {
	/**
	 * Retrieves the dimension managed by this holder.
	 *
	 * @return the requested dimension.
	 */
	Dimension getDimension();

	/**
	 * Binds a dimension to this holder.
	 *
	 * @param dimension the dimension to be bound.
	 */
	void bindDimension(Dimension dimension);

	/**
	 * Unbinds a dimension from this holder.
	 *
	 * @param dimension the dimension to be unbound.
	 */
	void unbindPlayer(Dimension dimension);
}
