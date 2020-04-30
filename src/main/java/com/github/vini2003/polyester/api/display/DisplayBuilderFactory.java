package com.github.vini2003.polyester.api.display;

import com.github.vini2003.polyester.api.entity.Player;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DisplayBuilderFactory {
	private final Map<Integer, Consumer<Player>> actions = new HashMap<>();
	private final Map<Integer, Supplier<ItemStack>> stacks = new HashMap<>();

	private Text title;

	private int rows;

	private DisplayBuilderFactory() {
	}

	/**
	 * Instantiates a display builder factory.
	 *
	 * @return the requested builder factory.
	 */
	public static DisplayBuilderFactory factory() {
		return new DisplayBuilderFactory();
	}

	/**
	 * Binds an action to a given slot number.
	 *
	 * @param number the given number.
	 * @param action the given action.
	 */
	public DisplayBuilderFactory bindAction(int number, Consumer<Player> action) {
		actions.put(number, action);
		return this;
	}

	/**
	 * Unbinds an action from a given slot number.
	 *
	 * @param number the given number.
	 */
	public DisplayBuilderFactory unbindAction(int number) {
		actions.remove(number);
		return this;
	}

	/**
	 * Binds a stack supplier to a given slot number.
	 *
	 * @param number the given number.
	 * @param supplier the given supplier.
	 */
	public DisplayBuilderFactory bindStack(int number, Supplier<ItemStack> supplier) {
		stacks.put(number, supplier);
		return this;
	}

	/**
	 * Unbinds a stack supplier form a given slot number.
	 *
	 * @param number the given slot number.
	 */
	public DisplayBuilderFactory unbindStack(int number) {
		stacks.remove(number);
		return this;
	}

	/**
	 * Binds a title to the result display.
	 *
	 * @param title the given title.
	 */
	public DisplayBuilderFactory bindTitle(Text title) {
		this.title = title;
		return this;
	}

	/**
	 * Unbinds the title from the result display.
	 */
	public DisplayBuilderFactory unbindTitle() {
		title = null;
		return this;
	}

	/**
	 * Binds a height in rows of slots to the result display.
	 *
	 * @param rows the given height in rows.
	 */
	public DisplayBuilderFactory bindHeight(int rows) {
		this.rows = rows;
		return this;
	}

	/**
	 * Instantiates a new display builder with the information
	 * provided to this factory.
	 *
	 * @return the resulting display builder.
	 */
	public DisplayBuilder build() {
		return new DisplayBuilder(actions, stacks, title, rows);
	}
}
