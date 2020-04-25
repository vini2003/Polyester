package com.github.vini2003.spork.api.preset;

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
	 * Unbinds the preset of this holder.
	 */
	void unbindPreset();
}
