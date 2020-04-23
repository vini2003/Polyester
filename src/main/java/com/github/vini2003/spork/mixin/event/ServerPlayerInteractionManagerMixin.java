package com.github.vini2003.spork.mixin.event;

import com.github.vini2003.spork.api.block.BlockData;
import com.github.vini2003.spork.api.entity.Player;
import com.github.vini2003.spork.api.event.type.block.BlockBreakEvent;
import com.github.vini2003.spork.api.event.type.block.BlockUseEvent;
import com.github.vini2003.spork.api.event.type.item.ItemUseEvent;
import com.github.vini2003.spork.utility.RayUtilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
	@Shadow public ServerWorld world;

	@Shadow public ServerPlayerEntity player;

	@Inject(at = @At("HEAD"), cancellable = true, method = "interactBlock(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;")
	void onUseBlock(PlayerEntity playerEntity, World world, ItemStack stack, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> callbackInformationReturnable) {
		BlockData data = BlockData.of(world, hitResult);
		Player player = Player.of(playerEntity);

		if (BlockUseEvent.dispatch(world, player, data, hand).isCancelled()) {
			callbackInformationReturnable.setReturnValue(ActionResult.FAIL);
			callbackInformationReturnable.cancel();
		}
	}

	@Inject(at = @At("HEAD"), cancellable = true, method = "interactItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;")
	void onUseItem(PlayerEntity player, World world, ItemStack stack, Hand hand, CallbackInfoReturnable<ActionResult> callbackInformationReturnable) {
		BlockData data = BlockData.of(world, RayUtilities.rayTrace(world, player, RayTraceContext.FluidHandling.ANY));

		if (ItemUseEvent.dispatch(world, player, data, stack, hand).isCancelled()) {
			callbackInformationReturnable.setReturnValue(ActionResult.FAIL);
			callbackInformationReturnable.cancel();
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/item/ItemStack;)V"), cancellable = true, method = "tryBreakBlock(Lnet/minecraft/util/math/BlockPos;)Z")
	void onBreakBlock(BlockPos position, CallbackInfoReturnable<Boolean> callbackInformationReturnable) {
		World world = this.world;
		Player player = Player.of(this.player);
		BlockData data = BlockData.of(world, position);
		ItemStack stack = player.getHeldStack();

		if (BlockBreakEvent.dispatch(world, player, data, stack).isCancelled()) {
			callbackInformationReturnable.setReturnValue(false);
			callbackInformationReturnable.cancel();
		}
	}
}
