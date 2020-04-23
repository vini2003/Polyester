package com.github.vini2003.spork.api.formatting;

import net.minecraft.util.Formatting;

public interface FormattingWrapper {
	/**
	 * Retrieves the name of this formatting.
	 * @return the requested name.
	 */
	String getName();

	/**
	 * Retrieves the color code of this formatting.
	 * @return the requested code.
	 */
	char getCode();

	/**
	 * Retrieves the index of this formatting.
	 * @return the requested index.
	 */
	int getIndex();

	/**
	 * Retrieves the color of this formatting.
	 * @return the requested color.
	 */
	int getColor();

	/**
	 * Retrieves a formatting that matches the given name.
	 * @param name the specified name.
	 * @return the requested formatting.
	 */
	Formatting getByName(String name);

	/**
	 * Retrieves a formatting that matches the given color code.
	 * @param code the specified code.
	 * @return the requested formatting.
	 */
	Formatting getByCode(char code);

	/**
	 * Retrieves a formatting that matches the given index.
	 * @param index the specified index.
	 * @return the requested formatting.
	 */
	Formatting getByIndex(int index);

	/**
	 * Retrieves a formatting that matches the given color.
	 * @param color the specified color.
	 * @return the requested formatting.
	 */
	Formatting getByColor(int color);
}
