package com.github.vini2003.polyester.mixin.event;

import com.github.vini2003.polyester.api.event.type.entity.EntityAddEvent;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class WorldMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;addEntity(Lnet/minecraft/entity/Entity;)V"), cancellable = true, method = "Lnet/minecraft/server/world/ServerWorld;addEntity(Lnet/minecraft/entity/Entity;)Z")
	void onEntityAdded(Entity entity, CallbackInfoReturnable<Boolean> callbackInformationReturnable) {
		if (EntityAddEvent.dispatch(entity).isCancelled()) {
			callbackInformationReturnable.setReturnValue(false);
			callbackInformationReturnable.cancel();
		}
	}
}
