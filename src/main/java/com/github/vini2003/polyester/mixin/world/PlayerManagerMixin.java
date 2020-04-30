package com.github.vini2003.polyester.mixin.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	@Inject(at = @At("RETURN"), method = "Lnet/minecraft/server/PlayerManager;loadPlayerData(Lnet/minecraft/server/network/ServerPlayerEntity;)Lnet/minecraft/nbt/CompoundTag;")
	void onLoadPlayerData(ServerPlayerEntity player, CallbackInfoReturnable<CompoundTag> callbackInformationReturnable) {
		if (player.dimension == null) {
			player.dimension = DimensionType.OVERWORLD;
		}
	}
}
