package com.github.vini2003.spork.api.component;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * An InventoryComponentFromInventory
 * is a wrapper over an Inventory
 * that provides the functions and utilities
 * of an InventoryComponent.
 */
public class InventoryComponentFromInventory implements InventoryComponent {
	Inventory inventory;
	List<Consumer<InventoryComponent>> listeners = new ArrayList<>();

	private InventoryComponentFromInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public static InventoryComponentFromInventory of(Inventory inventory) {
		return new InventoryComponentFromInventory(inventory);
	}

	@Override
	public AbstractMap<Integer, ItemStack> getContents() {
		HashMap<Integer, ItemStack> contents = new HashMap<>();
		for (int i = 0; i < inventory.getInvSize(); ++i) {
			contents.put(i, inventory.getInvStack(i));
		}
		return contents;
	}

	@Override
	public ActionResult canInsert() {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canExtract() {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canInsert(ItemStack stack) {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canExtract(ItemStack stack) {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canInsert(int slot) {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canExtract(int slot) {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canInsert(ItemStack stack, int slot) {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canExtract(ItemStack stack, int slot) {
		return ActionResult.SUCCESS;
	}

	@Override
	public List<Consumer<InventoryComponent>> getListeners() {
		return listeners;
	}

	@Override
	public int getSize() {
		return inventory.getInvSize();
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		inventory.setInvStack(slot, stack);
	}

	@Override
	public ItemStack getStack(int slot) {
		return inventory.getInvStack(slot);
	}
}
