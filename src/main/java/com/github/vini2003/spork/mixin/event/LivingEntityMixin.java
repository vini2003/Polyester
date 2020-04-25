package com.github.vini2003.spork.mixin.event;

import com.github.vini2003.spork.api.event.type.stack.StackPickupEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(at = @At("HEAD"), method = "sendPickup(Lnet/minecraft/entity/Entity;I)V")
	void onPickupStack(Entity entity, int count, CallbackInfo callbackInformation) {
		if (StackPickupEvent.dispatch((ItemEntity) entity, count).isCancelled()) {
			callbackInformation.cancel();
		}
	}
}
