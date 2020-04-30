package com.github.vini2003.polyester.mixin.event;

import com.github.vini2003.polyester.api.block.BlockInformation;
import com.github.vini2003.polyester.api.event.type.block.BlockPlaceEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(BlockItem.class)
public class BlockItemMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onPlaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V"), cancellable = true, method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;")
	void onPlaceBlock(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> callbackInformationReturnable) {
		World world = context.getWorld();
		PlayerEntity player = context.getPlayer();
		BlockInformation data = BlockInformation.of(world, context.getBlockPos());
		ItemStack stack = player != null && player.getActiveHand() != null ? player.getStackInHand(player.getActiveHand()) : ItemStack.EMPTY;

		if (BlockPlaceEvent.dispatch(world, Optional.ofNullable(player), data, stack).isCancelled()) {
			callbackInformationReturnable.setReturnValue(ActionResult.FAIL);
			callbackInformationReturnable.cancel();
		}
	}
}
