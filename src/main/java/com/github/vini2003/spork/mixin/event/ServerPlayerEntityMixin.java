package com.github.vini2003.spork.mixin.event;

import com.github.vini2003.spork.api.entity.Player;
import com.github.vini2003.spork.api.event.type.player.PlayerConnectEvent;
import com.github.vini2003.spork.api.event.type.player.PlayerDamageEvent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
}
