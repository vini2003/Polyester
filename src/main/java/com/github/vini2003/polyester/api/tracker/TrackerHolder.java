package com.github.vini2003.polyester.api.tracker;

import java.util.Map;

public interface TrackerHolder {
	/**
	 * Retrieves the trackers managed by this holder.
	 *
	 * @return the requested trackers.
	 */
	Map<Object, Tracker<?>> getTrackers();
}
