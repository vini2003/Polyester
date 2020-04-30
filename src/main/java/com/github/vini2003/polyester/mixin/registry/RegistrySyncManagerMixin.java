package com.github.vini2003.polyester.mixin.registry;

import com.github.vini2003.polyester.api.dimension.registry.DimensionRegistry;
import net.fabricmc.fabric.impl.registry.sync.RegistrySyncManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RegistrySyncManager.class)
public class RegistrySyncManagerMixin {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/MutableRegistry;getId(Ljava/lang/Object;)Lnet/minecraft/util/Identifier;"), method = "toTag(Z)Lnet/minecraft/nbt/CompoundTag;")
	private static <T> Identifier onSynchronize(MutableRegistry registry, T entry) {
		if (registry == Registry.DIMENSION_TYPE && !DimensionRegistry.INSTANCE.shouldSynchronize((DimensionType) entry)) {
			return null;
		} else {
			return registry.getId(entry);
		}
	}
}
