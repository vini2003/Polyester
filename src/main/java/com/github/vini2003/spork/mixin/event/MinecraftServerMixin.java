package com.github.vini2003.spork.mixin.event;

import com.github.vini2003.spork.api.event.type.logic.ServerShutdownEvent;
import com.github.vini2003.spork.api.event.type.logic.ServerStartEvent;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;setFavicon(Lnet/minecraft/server/ServerMetadata;)V", ordinal = 0), method = "run")
	void onServerStart(CallbackInfo callbackInformation) {
		if (ServerStartEvent.dispatch((MinecraftServer) (Object) this).isCancelled()) {
			callbackInformation.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "shutdown")
	void onServerShutdown(CallbackInfo callbackInformation) {
		if (ServerShutdownEvent.dispatch((MinecraftServer) (Object) this).isCancelled()) {
			callbackInformation.cancel();
		}
	}
}
