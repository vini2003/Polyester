package com.github.vini2003.polyester.mixin.patch;

import com.github.vini2003.polyester.Polyester;
import com.github.vini2003.polyester.api.formatting.FormattingWrapper;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;
import java.util.Optional;

@Mixin(Formatting.class)
public class FormattingMixin implements FormattingWrapper {
	@Shadow
	@Final
	private Integer colorValue;
	@Shadow
	@Final
	private String name;
	@Shadow
	@Final
	private char code;
	@Shadow
	@Final
	private int colorIndex;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public char getCode() {
		return code;
	}

	@Override
	public int getIndex() {
		return colorIndex;
	}

	@Override
	public int getColor() {
		return Optional.ofNullable(colorValue).orElse(0);
	}

	@Override
	public Formatting getByName(String name) {
		return Optional.of(Arrays.stream(Formatting.values()).filter(format -> ((FormattingWrapper) (Object) format).getName().equals(name)).findFirst()).get().orElse(Formatting.WHITE);
	}

	@Override
	public Formatting getByCode(char code) {
		return Optional.of(Arrays.stream(Formatting.values()).filter(format -> ((FormattingWrapper) (Object) format).getCode() == code).findFirst()).get().orElse(Formatting.WHITE);
	}

	@Override
	public Formatting getByIndex(int index) {
		return Optional.of(Arrays.stream(Formatting.values()).filter(format -> ((FormattingWrapper) (Object) format).getIndex() == index).findFirst()).get().orElse(Formatting.WHITE);
	}

	@Override
	public Formatting getByColor(int color) {
		return Optional.of(Arrays.stream(Formatting.values()).filter(format -> ((FormattingWrapper) (Object) format).getColor() == color).findFirst()).get().orElse(Formatting.WHITE);
	}

	static {
		Polyester.LOGGER.log(Level.INFO, FormattingMixin.class.getName() + " Mixin applied.");
	}
}
