package com.github.vini2003.polyester.api.dimension;

import java.util.Arrays;

/**
 * This DimensionState is heavily based on
 * the Tesseract API by Vatuu Komalia, with
 * permission.
 * <p>
 * You can find Tesseract at: https://github.com/Vatuu/tesseract
 */
public enum DimensionState {
	SAVE(false, false, false),
	SAVE_UNLOAD(true, false, false),
	SAVE_UNREGISTER(true, false, true),
	RESET(false, true, false),
	RESET_UNREGISTER(false, true, true);

	private final boolean unload;
	private final boolean reset;
	private final boolean unregister;

	DimensionState(boolean unload, boolean reset, boolean unregister) {
		this.unload = unload;
		this.reset = reset;
		this.unregister = unregister;
	}

	public boolean shouldUnload() {
		return unload;
	}

	public boolean shouldReset() {
		return reset;
	}

	public boolean shouldUnregister() {
		return unregister;
	}

	public static DimensionState of(boolean unload, boolean reset, boolean unregister) {
		return Arrays.stream(DimensionState.values()).filter(result -> result.unload == unload && result.reset == reset && result.unregister == unregister).findFirst().orElse(DimensionState.SAVE);
	}
}
