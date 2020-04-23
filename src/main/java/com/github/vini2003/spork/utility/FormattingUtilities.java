package com.github.vini2003.spork.utility;

import com.github.vini2003.spork.api.formatting.FormattingWrapper;
import net.minecraft.util.Formatting;

import java.util.Random;

/**
 * Utilities for dealing
 * with Formattings.
 */
public class FormattingUtilities {
	private static final Random RANDOM = new Random();

	/**
	 * Retrieves a random formatting.
	 *
	 * @return the requested formatting.
	 */
	public static Formatting getRandom() {
		return Formatting.values()[RANDOM.nextInt(Formatting.values().length)];
	}

	/**
	 * Retrieves a formatting by its name.
	 *
	 * @param name the specified name.
	 * @return the requested formatting.
	 */
	public static Formatting getByName(String name) {
		return ((FormattingWrapper) (Object) Formatting.WHITE).getByName(name);
	}

	/**
	 * Retrieves a formatting by its code.
	 *
	 * @param code the specified code.
	 * @return the requested formatting.
	 */
	public static Formatting getByCode(char code) {
		return ((FormattingWrapper) (Object) Formatting.WHITE).getByCode(code);
	}

	/**
	 * Retrieves a formatting by its index.
	 *
	 * @param index the specified index.
	 * @return the requested formatting.
	 */
	public static Formatting getByIndex(int index) {
		return ((FormattingWrapper) (Object) Formatting.WHITE).getByIndex(index);
	}

	/**
	 * Retrieves a formatting by its color.
	 *
	 * @param color the specified color.
	 * @return the requested formatting.
	 */
	public static Formatting getByColor(int color) {
		return ((FormattingWrapper) (Object) Formatting.WHITE).getByColor(color);
	}
}
