package com.github.vini2003.spork.api.dimension;

import com.github.vini2003.spork.mixin.registry.SimpleRegistryMixin;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;

/**
 * This DimensionRegistry is heavily based on
 * the Tesseract API by Vatuu Komalia, with
 * permission.
 * <p>
 * You can find Tesseract at: https://github.com/Vatuu/tesseract
 */
public class DimensionRegistry {
	public static final DimensionRegistry INSTANCE = new DimensionRegistry();

	private final HashMap<Integer, DimensionType> ENTRIES = new HashMap<>();
	private final HashMap<DimensionType, Identifier> DIMENSION_TO_NAME = new HashMap<>();
	private final HashMap<Identifier, DimensionType> NAME_TO_DIMENSION = new HashMap<>();

	private DimensionRegistry() {
		ENTRIES.put(DimensionType.OVERWORLD.getRawId(), DimensionType.OVERWORLD);
		ENTRIES.put(DimensionType.THE_NETHER.getRawId(), DimensionType.THE_NETHER);
		ENTRIES.put(DimensionType.THE_END.getRawId(), DimensionType.THE_END);

		NAME_TO_DIMENSION.put(new Identifier("overworld"), DimensionType.OVERWORLD);
		NAME_TO_DIMENSION.put(new Identifier("the_nether"), DimensionType.THE_NETHER);
		NAME_TO_DIMENSION.put(new Identifier("the_end"), DimensionType.THE_END);

		DIMENSION_TO_NAME.put(DimensionType.OVERWORLD, new Identifier("overworld"));
		DIMENSION_TO_NAME.put(DimensionType.THE_NETHER, new Identifier("the_nether"));
		DIMENSION_TO_NAME.put(DimensionType.THE_END, new Identifier("the_end"));
	}

	public HashMap<Integer, DimensionType> getEntries() {
		return ENTRIES;
	}

	public HashMap<DimensionType, Identifier> getNames() {
		return DIMENSION_TO_NAME;
	}

	public DimensionType getByIdentifier(Identifier identifier) {
		return NAME_TO_DIMENSION.get(identifier);
	}

	public Identifier getByType(DimensionType type) {
		return DIMENSION_TO_NAME.get(type);
	}

	public DimensionType register(Identifier name, DimensionFactory factory, BiomeAccessType biomeAccessType) {
		int identifier = ENTRIES.size() + 5;
		ImplementedDimensionType type = new ImplementedDimensionType(identifier, name.getPath(), name.getPath(), (factory::build), true, biomeAccessType);
		ENTRIES.put(identifier, type);
		DIMENSION_TO_NAME.put(type, name);
		NAME_TO_DIMENSION.put(name, type);
		return Registry.register(Registry.DIMENSION_TYPE, identifier, name.toString(), type);
	}

	public boolean unregister(Identifier name) {
		return unregister(NAME_TO_DIMENSION.get(name));
	}

	public boolean unregister(DimensionType type) {
		for (DimensionType entryType : ENTRIES.values()) {
			if (entryType.getRawId() == type.getRawId()) {
				ENTRIES.remove(type.getRawId() + 1);
				NAME_TO_DIMENSION.remove(DIMENSION_TO_NAME.get(type));
				DIMENSION_TO_NAME.remove(type);
			}
		}
		return ((SimpleRegistryMixin<DimensionType>) Registry.DIMENSION_TYPE).getEntries().values().remove(type);
	}
}
