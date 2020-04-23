package com.github.vini2003.spork.api.dimension;

import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

import java.io.File;
import java.util.function.BiFunction;

/**
 * This ImplementedDimensionType is heavily based on
 * the Tesseract API by Vatuu Komalia, with
 * permission.
 *
 * You can find Tesseract at: https://github.com/Vatuu/tesseract
 */
public class ImplementedDimensionType extends DimensionType {
	public ImplementedDimensionType(int id, String suffix, String saveDirectory, BiFunction<World, DimensionType, ? extends Dimension> factory, boolean hasSkylight, BiomeAccessType biomeAccessType) {
		super(id, suffix, saveDirectory, factory, hasSkylight, biomeAccessType);
	}
}
