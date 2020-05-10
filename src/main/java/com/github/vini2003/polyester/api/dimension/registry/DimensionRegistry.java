package com.github.vini2003.polyester.api.dimension.registry;

import com.github.vini2003.polyester.api.data.Position;
import com.github.vini2003.polyester.api.dimension.DimensionFactory;
import com.github.vini2003.polyester.api.dimension.utilities.DimensionUtilities;
import com.github.vini2003.polyester.mixin.registry.SimpleRegistryMixin;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * This DimensionRegistry is heavily based on
 * the Tesseract API by Vatuu Komalia, with
 * permission.
 * <p>
 * You can find Tesseract at: https://github.com/Vatuu/tesseract
 */
public class DimensionRegistry {
	public static final DimensionRegistry INSTANCE = new DimensionRegistry();

	private final Set<Identifier> trackedValues = new HashSet<>();

	private DimensionRegistry() {
	}

	public Collection<Identifier> getKeys() {
		return (((SimpleRegistryMixin<DimensionType>) Registry.DIMENSION_TYPE).getEntries().inverse().values());
	}

	public Collection<DimensionType> getValues() {
		return (((SimpleRegistryMixin<DimensionType>) Registry.DIMENSION_TYPE).getEntries().values());
	}

	public DimensionType getByKey(Identifier key) {
		return ((SimpleRegistryMixin<DimensionType>) Registry.DIMENSION_TYPE).getEntries().get(key);
	}

	public Identifier getByValue(DimensionType value) {
		return ((SimpleRegistryMixin<DimensionType>) Registry.DIMENSION_TYPE).getEntries().inverse().get(value);
	}

	public void register(Identifier key, DimensionFactory value) {
		trackedValues.add(key);
		FabricDimensionType.builder()
				.biomeAccessStrategy(value.settings.biomeAccessType)
				.defaultPlacer(DimensionUtilities.PLACER.apply(Position.of(0, 64, 0).asBlockPosition()))
				.factory(value::build)
				.buildAndRegister(key);
	}

	public boolean unregister(Identifier key) {
		if (trackedValues.contains(key)) {
			trackedValues.remove(key);
			return ((SimpleRegistryMixin<DimensionType>) Registry.DIMENSION_TYPE).getEntries().values().remove(getByKey(key));
		} else {
			return false;
		}
	}

	public boolean unregister(DimensionType value) {
		if (trackedValues.contains(getByValue(value))) {
			trackedValues.remove(getByValue(value));
			return ((SimpleRegistryMixin<DimensionType>) Registry.DIMENSION_TYPE).getEntries().values().remove(value);
		} else {
			return false;
		}
	}

	public boolean isTracked(DimensionType value) {
		return trackedValues.contains(getByValue(value));
	}

	public boolean isTracked(Identifier key) {
		return trackedValues.contains(key);
	}
}
