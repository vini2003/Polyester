package com.github.vini2003.spork.api.display;

import com.github.vini2003.spork.api.entity.Player;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DisplayBuilderFactory {
	private final Map<Integer, Consumer<Player>> actions = new HashMap<>();
	private final Map<Integer, Supplier<ItemStack>> stacks = new HashMap<>();

	private Text name;

	private int rows;

	private DisplayBuilderFactory() {
	}

	public static DisplayBuilderFactory builder() {
		return new DisplayBuilderFactory();
	}

	public DisplayBuilderFactory bindAction(int number, Consumer<Player> action) {
		actions.put(number, action);
		return this;
	}
	public DisplayBuilderFactory unbindAction(int number) {
		actions.remove(number);
		return this;
	}

	public DisplayBuilderFactory bindStack(int number, Supplier<ItemStack> supplier) {
		stacks.put(number, supplier);
		return this;
	}

	public DisplayBuilderFactory unbindStack(int number) {
		stacks.remove(number);
		return this;
	}

	public DisplayBuilderFactory bindTitle(Text name) {
		this.name = name;
		return this;
	}

	public DisplayBuilderFactory unbindTitle() {
		name = null;
		return this;
	}

	public DisplayBuilderFactory bindHeight(int rows) {
		this.rows = rows;
		return this;
	}

	public DisplayBuilder build() {
		return new DisplayBuilder(actions, stacks, name, rows);
	}
}
