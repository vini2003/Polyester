package com.github.vini2003.polyester.api.structure.registry;

import com.github.vini2003.polyester.Polyester;
import com.github.vini2003.polyester.api.block.BlockInformation;
import com.github.vini2003.polyester.api.data.Position;
import com.github.vini2003.polyester.api.dimension.DimensionFactory;
import com.github.vini2003.polyester.api.dimension.registry.DimensionRegistry;
import com.github.vini2003.polyester.api.dimension.utilities.DimensionUtilities;
import com.github.vini2003.polyester.api.structure.Structure;
import com.github.vini2003.polyester.mixin.registry.SimpleRegistryMixin;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.dimension.DimensionType;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

public class StructureRegistry {
	private static final Supplier<RuntimeException> ENTRY = () -> new RuntimeException("Entry not found in registry!");

	public static final StructureRegistry INSTANCE = new StructureRegistry();

	private final HashBiMap<Identifier, Structure> entries = HashBiMap.create();

	public Collection<Identifier> getKeys() {
		return entries.inverse().values();
	}

	public Collection<Structure> getValues() {
		return entries.values();
	}

	public Structure getByKey(Identifier key) {
		return entries.get(key);
	}

	public Identifier getByValue(Structure value) {
		return entries.inverse().get(value);
	}

	public void register(Identifier key, Structure value) {
		entries.put(key, value);
	}

	public boolean unregister(Identifier key) {
		return entries.remove(key) != null;
	}

	public boolean unregister(Structure value) {
		return entries.inverse().remove(value) != null;
	}

	public void deserialize(File path) {
		for (File file : path.listFiles()) {
			try {
				CompoundTag tag = NbtIo.read(file);
				Identifier key = new Identifier(tag.getString("name"));
				Structure value = Structure.deserialize(tag.getCompound("data"));

				register(key, value);
			} catch (IOException exception) {
				Polyester.LOGGER.log(Level.WARN, "Could not deserialize structure for " + path.getPath());
			} catch (NullPointerException exception) {
				Polyester.LOGGER.log(Level.WARN, "Could not deserialize structure name or information for " + path.getPath());
			}
		}
	}

	public void serialize(File path) {
		for (Map.Entry<Identifier, Structure> entry : entries.entrySet()) {
			File file = new File(path.toString() + "/" + entry.getKey().getNamespace() + "_" + entry.getKey().getPath() + ".nbt");
			try {
				FileUtils.writeStringToFile(file, entry.getValue().toString());
			} catch (IOException exception) {
				Polyester.LOGGER.log(Level.WARN, "Could not serialize structure to " + file.getPath());
			}
		}
	}
}
