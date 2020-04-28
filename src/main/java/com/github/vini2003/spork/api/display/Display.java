package com.github.vini2003.spork.api.display;

import com.github.vini2003.spork.api.block.BlockData;
import com.github.vini2003.spork.api.entity.Player;
import com.github.vini2003.spork.api.event.EventResult;
import com.github.vini2003.spork.api.event.type.block.BlockAddEvent;
import com.google.common.collect.ImmutableMap;
import net.minecraft.container.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.world.World;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Display extends GenericContainer {
	private final Map<Integer, Consumer<Player>> actions;
	private final Map<Integer, Supplier<ItemStack>> stacks;

	private static final ImmutableMap<Integer, ContainerType<?>> TYPES = Util.make(ImmutableMap.<Integer, ContainerType<?>>builder(), (builder) ->  {
		builder.put(1, ContainerType.GENERIC_9X1);
		builder.put(2, ContainerType.GENERIC_9X2);
		builder.put(3, ContainerType.GENERIC_9X3);
		builder.put(4, ContainerType.GENERIC_9X4);
		builder.put(5, ContainerType.GENERIC_9X5);
		builder.put(6, ContainerType.GENERIC_9X6);
	}).build();

	public Display(int syncId, int rows, PlayerInventory playerInventory, ImmutableMap<Integer, Consumer<Player>> actions, ImmutableMap<Integer, Supplier<ItemStack>> stacks) {
		super(TYPES.getOrDefault(rows, ContainerType.GENERIC_9X1), syncId, playerInventory, new BasicInventory(9 * rows), rows);

		this.actions = ImmutableMap.copyOf(actions);
		this.stacks = ImmutableMap.copyOf(stacks);

		synchronize();
	}

	public void synchronize() {
		stacks.forEach((slot, supplier) -> {
			try {
				setStackInSlot(slot, supplier.get().copy());
			} catch (Exception ignored) {
				// Performance.
			}
		});

		getInventory().markDirty();

		sendContentUpdates();
	}

	@Override
	public ItemStack onSlotClick(int number, int clickData, SlotActionType actionType, PlayerEntity player) {
		try {
			actions.get(number).accept(Player.of(player));
		} catch (Exception ignored) {
			// Performance.
		}

		synchronize();

		return getSlot(number).getStack();
	}
}
