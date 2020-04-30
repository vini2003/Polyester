package com.github.vini2003.polyester.mixin.event;

import com.github.vini2003.polyester.api.block.BlockInformation;
import com.github.vini2003.polyester.api.entity.Player;
import com.github.vini2003.polyester.api.event.type.block.BlockCollideEvent;
import com.github.vini2003.polyester.api.event.type.block.BlockLandEvent;
import com.github.vini2003.polyester.api.event.type.block.BlockStepEvent;
import com.github.vini2003.polyester.api.event.type.entity.EntityChangeDimensionEvent;
import com.github.vini2003.polyester.api.event.type.entity.EntityRemoveEvent;
import com.github.vini2003.polyester.api.event.type.player.PlayerMoveEvent;
import com.github.vini2003.polyester.api.event.type.stack.StackDropEvent;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashSet;
import java.util.Set;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public World world;

	@Shadow
	protected abstract BlockPos getLandingPos();

	@Shadow
	public abstract double getZ();

	@Shadow
	public DimensionType dimension;

	@Shadow
	public abstract MinecraftServer getServer();

	@Shadow
	public abstract EntityType<?> getType();

	@Inject(at = @At("HEAD"), cancellable = true, method = "fall(DZLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V")
	void onLandOnBlock(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition, CallbackInfo callbackInformation) {
		World world = this.world;

		if (BlockLandEvent.dispatch(world, (Entity) (Object) this, BlockInformation.of(world, landedPosition), (int) heightDifference).isCancelled()) {
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
		if (BlockCollideEvent.dispatch(this.world, (Entity) (Object) this, BlockInformation.of(this.world, new BlockPos(position))).isCancelled()) {
			callbackInformation.cancel();
		}
	}

	@Inject(at = @At(value = "RETURN"), cancellable = true, method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V")
	void onStepOnBlock(MovementType type, Vec3d movement, CallbackInfo callbackInformation) {
		if (!((Object) this instanceof LivingEntity)) return;

		Set<BlockPos> positions = new HashSet<>();

		positions.add(getLandingPos());
		positions.add(getLandingPos().offset(Direction.EAST));
		positions.add(getLandingPos().offset(Direction.EAST).offset(Direction.SOUTH));
		positions.add(getLandingPos().offset(Direction.EAST).offset(Direction.NORTH));
		positions.add(getLandingPos().offset(Direction.NORTH));
		positions.add(getLandingPos().offset(Direction.WEST));
		positions.add(getLandingPos().offset(Direction.WEST).offset(Direction.SOUTH));
		positions.add(getLandingPos().offset(Direction.WEST).offset(Direction.NORTH));
		positions.add(getLandingPos().offset(Direction.SOUTH));

		LivingEntity entity = (LivingEntity) (Object) this;

		for (BlockPos position : positions) {
			if (entity.getBoundingBox().intersects(new Box(position.up()))) {
				if (BlockStepEvent.dispatch(this.world, (Entity) (Object) this, BlockInformation.of(world, position)).isCancelled()) {
					callbackInformation.cancel();
				}
			}
		}
	}

	@Inject(at = @At("HEAD"), cancellable = true, method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;")
	void onDropStack(ItemStack stack, float yOffset, CallbackInfoReturnable<ItemEntity> callbackInformationReturnable) {
		if (StackDropEvent.dispatch((Entity) (Object) this, stack).isCancelled()) {
			callbackInformationReturnable.setReturnValue(null);
			callbackInformationReturnable.cancel();
		}
	}

	@Inject(at = @At("HEAD"), cancellable = true,  method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V")
	void onMove(MovementType type, Vec3d movement, CallbackInfo callbackInformation) {
		if (((Object) this) instanceof PlayerEntity) {
			if (PlayerMoveEvent.dispatch(Player.of((PlayerEntity) (Object) this), type, movement).isCancelled()) {
				callbackInformation.cancel();
			}
		}
	}

	@Inject(at = @At("HEAD"), cancellable = true,  method = "changeDimension(Lnet/minecraft/world/dimension/DimensionType;)Lnet/minecraft/entity/Entity;")
	void onChangeDimension(DimensionType dimension, CallbackInfoReturnable<Entity> callbackInformationReturnable) {
		if (EntityChangeDimensionEvent.dispatch((Entity) (Object) this, dimension).isCancelled()) {
			callbackInformationReturnable.setReturnValue((Entity) (Object) this);
			callbackInformationReturnable.cancel();
		}
	}
}
