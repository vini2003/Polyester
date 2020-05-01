package com.github.vini2003.polyester.mixin.event;

import com.github.vini2003.polyester.api.entity.Player;
import com.github.vini2003.polyester.api.event.type.player.PlayerDamageEvent;
import com.github.vini2003.polyester.api.event.type.player.PlayerDeathEvent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
	@Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", cancellable = true)
	void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInformationReturnable) {
		if (PlayerDamageEvent.dispatch(Player.of((ServerPlayerEntity) (Object) this), source, amount).isCancelled()) {
			callbackInformationReturnable.setReturnValue(false);
			callbackInformationReturnable.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V", cancellable = true)
	void onDeath(DamageSource source, CallbackInfo callbackInformation) {
		if (PlayerDeathEvent.dispatch(Player.of((ServerPlayerEntity) (Object) this), source).isCancelled()) {
			callbackInformation.cancel();
		}
	}
}
