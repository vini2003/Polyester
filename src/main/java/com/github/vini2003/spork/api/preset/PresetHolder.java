package com.github.vini2003.spork.api.preset;

import net.minecraft.world.dimension.Dimension;

public interface PresetHolder {
	/**
	 * Retrieves the preset that manages this holder.
	 *
	 * @return the requested preset.
	 */
	Preset getPreset();

	/**
	 * Binds a preset to this holder.
	 *
	 * @param preset the preset to be bound.
	 */
	void bindPreset(Preset preset);

	/**
	 * Unbinds a preset from this holder.
	 *
	 * @param preset the preset to be unbound.
	 */
	void unbindPreset(Preset preset);
}
