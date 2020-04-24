package com.github.vini2003.example;

import com.github.vini2003.spork.api.preset.PresetRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Example implements ModInitializer {
	@Override
	public void onInitialize() {
		PresetRegistry.INSTANCE.register(new Identifier("example:preset"), new ExamplePreset());
	}
}
