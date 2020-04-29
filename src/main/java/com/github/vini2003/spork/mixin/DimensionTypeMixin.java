package com.github.vini2003.spork.mixin;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiFunction;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {
	@Shadow @Final private BiFunction<World, DimensionType, ? extends Dimension> factory;

	@Inject(at = @At("HEAD"), method = "create(Lnet/minecraft/world/World;)Lnet/minecraft/world/dimension/Dimension;")
	void onCreate(World world, CallbackInfoReturnable<Dimension> callbackInformationReturnable) {
		System.out.println(world);
		System.out.println(world.dimension);
		System.out.println(world.dimension.getType());
		System.out.println(factory);
	}
}
