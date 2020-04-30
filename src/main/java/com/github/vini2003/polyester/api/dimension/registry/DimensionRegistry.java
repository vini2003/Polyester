package com.github.vini2003.polyester.api.dimension.registry;

import com.github.vini2003.polyester.api.data.Position;
import com.github.vini2003.polyester.api.dimension.DimensionFactory;
import com.github.vini2003.polyester.api.dimension.utilities.DimensionUtilities;
import com.github.vini2003.polyester.mixin.registry.SimpleRegistryMixin;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
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
	private static final Supplier<RuntimeException> DIMENSION_NOT_FOUND = () -> new RuntimeException("Dimension not found in registry!");

	public static final DimensionRegistry INSTANCE = new DimensionRegistry();

	private final Set<Identifier> trackedDimensions = new HashSet<>();

	private DimensionRegistry() {
	}

	public boolean shouldSynchronize(DimensionType type) {
		return !trackedDimensions.contains(getByType(type));
	}

	public boolean shouldSynchronize(Identifier name) {
		return !trackedDimensions.contains(name);
	}

	public Collection<Identifier> getNames() {
		return new ArrayList<>(((SimpleRegistryMixin<DimensionType>) Registry.DIMENSION_TYPE).getEntries().keySet());
	}

	public DimensionType getByIdentifier(Identifier identifier) {
		return ((SimpleRegistryMixin<DimensionType>) Registry.DIMENSION_TYPE).getEntries().entrySet().stream().filter((entry -> entry.getKey().equals(identifier))).findFirst().orElseThrow(DIMENSION_NOT_FOUND).getValue();
	}

	public Identifier getByType(DimensionType type) {
		return ((SimpleRegistryMixin<DimensionType>) Registry.DIMENSION_TYPE).getEntries().entrySet().stream().filter((entry -> entry.getValue() == type)).findFirst().orElseThrow(DIMENSION_NOT_FOUND).getKey();
	}

	public void register(Identifier name, DimensionFactory factory) {
		trackedDimensions.add(name);
		FabricDimensionType.builder()
				.biomeAccessStrategy(factory.settings.biomeAccessType)
				.defaultPlacer(DimensionUtilities.PLACER.apply(Position.of(0, 64, 0).asBlockPosition()))
				.factory(factory::build)
				.buildAndRegister(name);
	}

	public boolean unregister(Identifier name) {
		trackedDimensions.remove(name);
		return ((SimpleRegistryMixin<DimensionType>) Registry.DIMENSION_TYPE).getEntries().values().remove(getByIdentifier(name));
	}

	public boolean unregister(DimensionType type) {
		trackedDimensions.remove(getByType(type));
		return ((SimpleRegistryMixin<DimensionType>) Registry.DIMENSION_TYPE).getEntries().values().remove(type);
	}
}
