package com.github.vini2003.polyester.api.text;

import com.github.vini2003.polyester.api.formatting.FormattingWrapper;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * A TextBuilder is a class which
 * manages Texts and appending them
 * sanely.
 */
public class TextBuilder {
	/**
	 * Retrieves a builder for this builder.
	 *
	 * @return the requested builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Retrieves a string as a literal text.
	 *
	 * @param text the specified string.
	 * @return the requested text.
	 */
	public static LiteralText literal(String text) {
		return new LiteralText(text);
	}

	/**
	 * Retrieves a string as a translatable text.
	 *
	 * @param text the specified string.
	 * @return the requested text.
	 */
	public static TranslatableText translatable(String text) {
		return new TranslatableText(text);
	}

	public static class Builder {
		ArrayList<Text> texts = new ArrayList<>();

		/**
		 * Instantiates this builder.
		 */
		private Builder() {
		}

		/**
		 * Appends a text to this builder's arguments.
		 *
		 * @param text the specified text.
		 */
		public Builder with(Text text) {
			texts.add(text);
			return this;
		}

		/**
		 * Appends a serializable value to this builder's arguments.
		 *
		 * @param serializable the specified value.
		 */
		public Builder with(Serializable serializable) {
			with(new LiteralText(serializable.toString()));
			return this;
		}

		/**
		 * Appends a formatting to this builder's arguments.
		 *
		 * @param formatting the specified formatting.
		 */
		public Builder with(FormattingWrapper formatting) {
			with("§" + formatting.getCode());
			return this;
		}

		/**
		 * Retrieves the final result of the build process
		 * as a literal text.
		 *
		 * @return the requested text.
		 */
		public LiteralText build() {
			return new LiteralText(texts.stream().map(Text::asFormattedString).collect(Collectors.joining(" ")));
		}
	}
}
