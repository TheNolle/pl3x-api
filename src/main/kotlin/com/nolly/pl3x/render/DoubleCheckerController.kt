package com.nolly.pl3x.render

import net.pl3x.map.core.Pl3xMap

/**
 * Controls Pl3xMap's region double-checker timer.
 *
 * Manages automatic re-rendering of regions to catch missed updates.
 */
class DoubleCheckerController {
	/**
	 * Stops the double-checker timer immediately.
	 */
	fun stop() {
		Pl3xMap.api().regionDoubleChecker.stop()
	}

	/**
	 * Stops current timer and starts new one with [delayMs].
	 *
	 * Default: 250 seconds (4+ minutes).
	 *
	 * @param delayMs Time between double-check cycles
	 */
	fun restart(delayMs: Long = 250_000L) {
		Pl3xMap.api().regionDoubleChecker.stop()
		Pl3xMap.api().regionDoubleChecker.start(delayMs)
	}
}
