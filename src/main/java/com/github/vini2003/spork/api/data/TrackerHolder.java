package com.github.vini2003.spork.api.data;

import java.util.Collection;

public interface TrackerHolder {
	/**
	 * Retrieves the trackers managed by this holder.
	 * @return the requested trackers.
	 */
	Collection<Tracker<?>> getTrackers();
}
