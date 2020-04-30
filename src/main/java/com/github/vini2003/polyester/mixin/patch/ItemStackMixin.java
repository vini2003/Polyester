package com.github.vini2003.polyester.mixin.patch;

import com.github.vini2003.polyester.Polyester;
import com.github.vini2003.polyester.serializer.ItemStackSerializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract Item getItem();

	@Shadow
	public abstract int getCount();

	@Shadow
	public abstract boolean hasTag();

	@Shadow
	public abstract CompoundTag getTag();

	/**
	 * @author vini2003
	 */
	@Overwrite
	public CompoundTag toTag(CompoundTag tag) {
		return ItemStackSerializer.toTag((ItemStack) (Object) this, tag);
	}

	/**
	 * @author vini2003
	 */
	@Overwrite
	public static ItemStack fromTag(CompoundTag stackTag) {
		return ItemStackSerializer.fromTag(stackTag);
	}

	static {
		Polyester.LOGGER.log(Level.INFO, ItemStackMixin.class.getName() + " Mixin applied.");
	}
}
