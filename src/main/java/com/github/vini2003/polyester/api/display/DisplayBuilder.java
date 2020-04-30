package com.github.vini2003.polyester.api.display;

import com.github.vini2003.polyester.api.entity.Player;
import com.google.common.collect.ImmutableMap;
import net.minecraft.container.Container;
import net.minecraft.container.NameableContainerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DisplayBuilder implements NameableContainerFactory {
	private final ImmutableMap<Integer, Consumer<Player>> actions;
	private final ImmutableMap<Integer, Supplier<ItemStack>> stacks;

	private final Text title;

	private final int rows;

	public DisplayBuilder(Map<Integer, Consumer<Player>> actions, Map<Integer, Supplier<ItemStack>> stacks, Text title, int rows) {
		this.actions = ImmutableMap.copyOf(actions);
		this.stacks = ImmutableMap.copyOf(stacks);

		this.title = title;

		this.rows = rows;
	}

	@Override
	public Text getDisplayName() {
		return title;
	}

	@Override
	public Container createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return new Display(syncId, rows, playerInventory, actions, stacks);
	}
}
