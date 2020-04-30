package com.github.vini2003.polyester.mixin.chunk;

import com.github.vini2003.polyester.api.dimension.implementation.DimensionImplementation;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkManager.class)
public class ServerChunkManagerMixin {
	@Shadow
	@Final
	private ServerWorld world;

	@Inject(at = @At("HEAD"), method = "save(Z)V", cancellable = true)
	void onSave(boolean flush, CallbackInfo callbackInformation) {
		if (world.dimension instanceof DimensionImplementation) {
			if (((DimensionImplementation) world.dimension).getState().shouldReset()) callbackInformation.cancel();
		}
	}
}
