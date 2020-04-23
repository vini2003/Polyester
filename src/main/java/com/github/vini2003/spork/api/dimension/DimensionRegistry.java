package com.github.vini2003.spork.api.dimension;

import com.github.vini2003.spork.mixin.registry.SimpleRegistryMixin;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.dimension.DimensionType;

/**
 * This DimensionRegistry is heavily based on
 * the Tesseract API by Vatuu Komalia, with
 * permission.
 *
 * You can find Tesseract at: https://github.com/Vatuu/tesseract
 */
public class DimensionRegistry {
	public static final DimensionRegistry INSTANCE = new DimensionRegistry();

	private final Int2ObjectArrayMap<DimensionType> ENTRIES = new Int2ObjectArrayMap<>();

	private DimensionRegistry() {
	}

	public DimensionType register(Identifier name, DimensionFactory factory, BiomeAccessType biomeAccessType) {
		int identifier = ENTRIES.size() + 5;
		ImplementedDimensionType type = new ImplementedDimensionType(identifier, name.getPath(), name.getPath(), (factory::build), true, biomeAccessType);
		ENTRIES.put(identifier, type);
		return Registry.register(Registry.DIMENSION_TYPE, identifier, name.toString(), type);
	}

	public boolean unregister(DimensionType type) {
		for (DimensionType entryType : ENTRIES.values()) {
			if (entryType.getRawId() == type.getRawId()) {
				ENTRIES.remove(type.getRawId());
			}
		}
		return ((SimpleRegistryMixin<DimensionType>) Registry.DIMENSION_TYPE).getEntries().values().remove(type);
	}
}
