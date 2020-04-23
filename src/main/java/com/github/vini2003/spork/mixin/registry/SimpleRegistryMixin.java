package com.github.vini2003.spork.mixin.registry;

import com.google.common.collect.BiMap;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleRegistry.class)
public interface SimpleRegistryMixin<T> {
	@Accessor("entries")
	BiMap<Identifier, T> getEntries();
}
