package com.github.vini2003.polyester.api.preset.registry;

import com.github.vini2003.polyester.api.preset.Preset;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PresetRegistry {
	private final Map<Identifier, Preset> entries = new HashMap<>();

	public static final PresetRegistry INSTANCE = new PresetRegistry();

	private PresetRegistry() {
	}

	/**
	 * Retrieves all the entries in this registry.
	 *
	 * @return the requested entries.
	 */
	public Map<Identifier, Preset> getEntries() {
		return entries;
	}

	/**
	 * Retrieves the names of the entries in this registry.
	 *
	 * @return the requested names.
	 */
	public List<String> getNames() {
		return entries.keySet().stream().map(Identifier::toString).collect(Collectors.toList());
	}

	/**
	 * Retrieves a given entry by its identifier.
	 *
	 * @param identifier the specified identifier.
	 * @return the requested entry.
	 */
	public Preset getByIdentifier(Identifier identifier) {
		return entries.get(identifier);
	}

	/**
	 * Retrieves a given identifier by its preset.
	 *
	 * @param preset the specified preset.
	 * @return the requested identifier.
	 */
	public Identifier getByPreset(Preset preset) {
		return entries.entrySet().stream().filter(entry -> entry.getValue() == preset).findFirst().orElse(null).getKey();
	}

	/**
	 * Registers a given preset with a given identifier as its identifier.
	 *
	 * @param identifier the given identifier.
	 * @param preset the given preset.
	 * @return the given preset.
	 */
	public Preset register(Identifier identifier, Preset preset) {
		entries.put(identifier, preset);
		return preset;
	}
}
