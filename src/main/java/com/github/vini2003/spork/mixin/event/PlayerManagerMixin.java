package com.github.vini2003.spork.mixin.event;

import com.github.vini2003.spork.api.entity.Player;
import com.github.vini2003.spork.api.event.type.player.PlayerConnectEvent;
import com.github.vini2003.spork.api.event.type.player.PlayerDisconnectEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	@Inject(at = @At("HEAD"), cancellable = true, method = "remove(Lnet/minecraft/server/network/ServerPlayerEntity;)V")
	void onPlayerDisconnect(ServerPlayerEntity player, CallbackInfo callbackInformation) {
		if (PlayerDisconnectEvent.dispatch(Player.of(player)).isCancelled()) {
			callbackInformation.cancel();
		}
	}

	@Inject(at = @At("HEAD"), cancellable = true, method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V")
	void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo callbackInformation) {
		if (PlayerConnectEvent.dispatch(Player.of(player)).isCancelled()) {
			callbackInformation.cancel();
		}
	}
}
