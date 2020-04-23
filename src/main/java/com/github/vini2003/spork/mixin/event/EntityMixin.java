package com.github.vini2003.spork.mixin.event;

import com.github.vini2003.spork.api.block.BlockData;
import com.github.vini2003.spork.api.event.type.block.BlockCollideEvent;
import com.github.vini2003.spork.api.event.type.block.BlockLandEvent;
import com.github.vini2003.spork.api.event.type.block.BlockStepEvent;
import com.github.vini2003.spork.api.event.type.entity.EntityRemoveEvent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public World world;

	@Shadow protected abstract BlockPos getLandingPos();

	@Inject(at = @At("HEAD"), cancellable = true, method = "fall(DZLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V")
	void onLandOnBlock(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition, CallbackInfo callbackInformation) {
		World world = this.world;

		if (BlockLandEvent.dispatch(world, (Entity) (Object) this, BlockData.of(world, landedPosition), (int) heightDifference).isCancelled()) {
			callbackInformation.cancel();
		}
	}

	@Inject(at = @At("HEAD"), cancellable = true, method = "remove()V")
	void onEntityRemoved(CallbackInfo callbackInformation) {
		if (EntityRemoveEvent.dispatch((Entity) (Object) this).isCancelled()) {
			callbackInformation.cancel();
		}
	}

	@Inject(locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V"), cancellable = true, method = "checkBlockCollision()V")
	void onCollideWithBlock(CallbackInfo callbackInformation, Box box, BlockPos.PooledMutable m1, BlockPos.PooledMutable m2, BlockPos.PooledMutable position) {
		if (BlockCollideEvent.dispatch(this.world, (Entity) (Object) this, BlockData.of(this.world, new BlockPos(position))).isCancelled()) {
			callbackInformation.cancel();
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onSteppedOn(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V"), cancellable = true, method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V")
	void onStepOnBlock(MovementType type, Vec3d movement, CallbackInfo callbackInformation) {
		if (BlockStepEvent.dispatch(this.world, (Entity) (Object) this, BlockData.of(world, this.getLandingPos())).isCancelled()) {
			callbackInformation.cancel();
		}
	}
}
