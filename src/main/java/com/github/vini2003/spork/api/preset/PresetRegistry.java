package com.github.vini2003.spork.api.preset;

import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;

import java.util.*;
import java.util.stream.Collectors;

public class PresetRegistry {
	private final Map<Identifier, Preset> ENTRIES = new HashMap<>();

	public static final PresetRegistry INSTANCE = new PresetRegistry();

	private PresetRegistry() {
	}

	public Map<Identifier, Preset> getEntries() {
		return ENTRIES;
	}

	public List<String> getNames() {
		return ENTRIES.keySet().stream().map(Identifier::toString).collect(Collectors.toList());
	}

	public Preset getByIdentifier(Identifier identifier) {
		return ENTRIES.get(identifier);
	}

	public Preset register(Identifier identifier, Preset preset) {
		ENTRIES.put(identifier, preset);
		return preset;
	}
}
