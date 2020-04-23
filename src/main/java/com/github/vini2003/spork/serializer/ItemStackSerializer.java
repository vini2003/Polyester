package com.github.vini2003.spork.serializer;

import com.github.vini2003.spork.Spork;
import com.github.vini2003.spork.utility.IdentifierUtilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

public class ItemStackSerializer {
	/**
	 * Converts the given stack to a tag.
	 *
	 * @param stack the specified stack.
	 * @return the requested tag.
	 */
	public static CompoundTag toTag(ItemStack stack) {
		return toTag(stack, new CompoundTag());
	}

	/**
	 * Saves the given stack to a tag.
	 *
	 * @param stack the given stack.
	 * @param tag   the given tag.
	 * @return the requested tag.
	 */
	public static CompoundTag toTag(ItemStack stack, CompoundTag tag) {
		Identifier identifier = Registry.ITEM.getId(stack.getItem());

		if (stack.getCount() < 0) {
			Spork.LOGGER.log(Level.ERROR, "ItemStack failed to be written: count was smaller than zero!");
			return new CompoundTag();
		} else {
			tag.putString("id", identifier.toString());
			tag.putInt("Count", stack.getCount());

			if (stack.hasTag()) {
				tag.put("tag", stack.getTag());
			}

			return tag;
		}
	}

	/**
	 * Loads a stack from a tag.
	 *
	 * @param tag the specified tag.
	 * @return the requested stack.
	 */
	public static ItemStack fromTag(CompoundTag tag) {
		ItemStack stack;

		if (!tag.contains("id")) {
			return ItemStack.EMPTY;
		}

		String identifierString = tag.getString("id");

		if (!IdentifierUtilities.isValid(identifierString)) {
			Spork.LOGGER.log(Level.ERROR, "ItemStack failed to be read: " + CompoundTag.class.getName() + "'s 'id' value is not a valid " + Identifier.class.getName() + "!");
			return ItemStack.EMPTY;
		}

		Identifier identifier = Identifier.tryParse(identifierString);

		if (!Registry.ITEM.getIds().contains(identifier)) {
			Spork.LOGGER.log(Level.ERROR, "ItemStack failed to be read: item registry did not contain a valid item identifier!");
			return ItemStack.EMPTY;
		}

		if (!tag.contains("Count")) {
			Spork.LOGGER.log(Level.ERROR, "ItemStack failed to be read: " + CompoundTag.class.getName() + " does not contain 'Count' value!");
			return ItemStack.EMPTY;
		}

		int count = tag.getInt("Count");

		if (count < 0) {
			Spork.LOGGER.log(Level.ERROR, "ItemStack failed to be read: count was smaller than zero!");
			return ItemStack.EMPTY;
		}

		Item item = Registry.ITEM.get(identifier);

		stack = new ItemStack(item, count);

		if (tag.contains("tag", 10)) {
			Tag rawTag = tag.get("tag");

			if (!(rawTag instanceof CompoundTag)) {
				Spork.LOGGER.log(Level.WARN, "ItemStack did not fail to be read, but had a non-standard tag which was discarded!");
			} else {
				CompoundTag tagTag = (CompoundTag) rawTag;

				stack.setTag(tagTag);
			}
		}

		if (item.isDamageable()) {
			stack.setDamage(stack.getDamage());
		}

		return stack;
	}
}
