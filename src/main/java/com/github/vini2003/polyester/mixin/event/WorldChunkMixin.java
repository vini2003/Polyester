package com.github.vini2003.polyester.mixin.event;

import com.github.vini2003.polyester.api.block.BlockInformation;
import com.github.vini2003.polyester.api.event.type.block.BlockAddEvent;
import com.github.vini2003.polyester.api.event.type.block.BlockRemoveEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(WorldChunk.class)
public class WorldChunkMixin {
	@Shadow
	@Final
	private World world;

	@Shadow
	@Final
	private ChunkPos pos;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onBlockAdded(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)V"), cancellable = true, method = "Lnet/minecraft/world/chunk/WorldChunk;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;")
	void onAddBlock(BlockPos position, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> callbackInformationReturnable) {
		if (BlockAddEvent.dispatch(this.world, BlockInformation.of(this.world, position), moved).isCancelled()) {
			callbackInformationReturnable.setReturnValue(state);
			callbackInformationReturnable.cancel();
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onBlockRemoved(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)V"), cancellable = true, method = "Lnet/minecraft/world/chunk/WorldChunk;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;")
	void onRemoveBlock(BlockPos position, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> callbackInformationReturnable) {
		Optional<BlockEntity> blockEntity = Optional.ofNullable(this.world.getBlockEntity(position));

		if (BlockRemoveEvent.dispatch(this.world, blockEntity, BlockInformation.of(this.world, position), moved).isCancelled()) {
			callbackInformationReturnable.setReturnValue(state);
			callbackInformationReturnable.cancel();
		}
	}
}
