package com.github.vini2003.spork.mixin.world;

import com.github.vini2003.spork.Spork;
import com.github.vini2003.spork.api.dimension.DimensionState;
import com.github.vini2003.spork.api.dimension.ImplementedDimension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.SecondaryServerWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ProgressListener;
import net.minecraft.util.profiler.DisableableProfiler;
import net.minecraft.world.SessionLockException;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Shadow @Final private Map<DimensionType, ServerWorld> worlds;

	@Shadow @Final private DisableableProfiler profiler;

	@Shadow public abstract ServerWorld getWorld(DimensionType dimensionType);

	@Mutable
	private WorldSaveHandler worldSaveHandler;

	@Mutable
	private WorldGenerationProgressListener worldGenerationProgressListener;

	@Inject(at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/world/dimension/DimensionType;getAll()Ljava/lang/Iterable;"), method = "createWorlds", cancellable = true)
	private void createWorld(WorldSaveHandler worldSaveHandler, LevelProperties properties, LevelInfo levelInfo, WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo callbackInformation){
		this.worldSaveHandler = worldSaveHandler;
		this.worldGenerationProgressListener = worldGenerationProgressListener;

		callbackInformation.cancel();
	}

	@Inject(method = "getWorld", at = @At("RETURN"), cancellable = true)
	void onGetWorld(DimensionType dimensionType, CallbackInfoReturnable<ServerWorld> callbackInformationReturnable) {
		if (dimensionType == null) throw new UnsupportedOperationException("Dimension type must not be " + null + "!");

		if (dimensionType != DimensionType.OVERWORLD && callbackInformationReturnable.getReturnValue() == null) {
			ServerWorld overworld = worlds.get(DimensionType.OVERWORLD);

			if (overworld == null) throw new UnsupportedOperationException("Overworld dimension not loaded!");

			ServerWorld world = new SecondaryServerWorld(overworld, (MinecraftServer) (Object) this, ((MinecraftServer) (Object) this).getWorkerExecutor(), worldSaveHandler, dimensionType, this.profiler, worldGenerationProgressListener);
			worlds.put(dimensionType, world);

			callbackInformationReturnable.setReturnValue(world);
		}
	}

	@Redirect(method = "save(ZZZ)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;save(Lnet/minecraft/util/ProgressListener;ZZ)V"))
	void onWorldSave(ServerWorld serverWorld, ProgressListener progressListener, boolean flush, boolean magic) throws SessionLockException {
		if (serverWorld.getDimension() instanceof ImplementedDimension) {
			DimensionState state = ((ImplementedDimension) serverWorld.getDimension()).getState();

			if (state.shouldUnload()) unloadWorld(serverWorld);
			if (state.shouldReset()) resetWorld(serverWorld);
			else serverWorld.save(progressListener, flush, magic);
		} else {
			serverWorld.save(progressListener, flush, magic);
		}
	}

	@Unique
	void unloadWorld(ServerWorld serverWorld) {
		if (!serverWorld.getPlayers().isEmpty()) {
			serverWorld.getPlayers().forEach(player -> {
				player.teleport(getWorld(DimensionType.OVERWORLD), 0, 64, 0, player.pitch, player.yaw);
			});
		}

		try {
			serverWorld.close();
		} catch (IOException exception) {
			Spork.LOGGER.log(Level.ERROR, "World unloading failed for world " + serverWorld.toString() + " of dimension " + serverWorld.getDimension().getType().getRawId() + " / " + serverWorld.getDimension().getType().getSuffix());
			exception.printStackTrace();
		}
	}

	@Unique
	void resetWorld(ServerWorld serverWorld) {
		serverWorld.savingDisabled = true;

		try {
			File worldSaveDirectory = serverWorld.dimension.getType().getSaveDirectory(serverWorld.getSaveHandler().getWorldDir());
			FileUtils.deleteDirectory(worldSaveDirectory);
		} catch (IOException exception) {
			Spork.LOGGER.log(Level.ERROR, "World reset failed for world " + serverWorld.toString() + " of dimension " + serverWorld.getDimension().getType().getRawId() + " / " + serverWorld.getDimension().getType().getSuffix());
			exception.printStackTrace();
		}
	}
}
