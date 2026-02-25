package com.nolly.pl3x

import com.nolly.pl3x.registry.IconRegistry
import com.nolly.pl3x.registry.LayerRegistry
import com.nolly.pl3x.registry.MarkerRegistry
import com.nolly.pl3x.registry.WorldRegistry
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Internal singleton managing all registries + readiness state.
 *
 * **Not for direct use** - access via `Pl3xAPI` or bootstrapping.
 *
 * Thread-safe with `AtomicBoolean` readiness + `ConcurrentHashMap` registries.
 */
internal object Pl3xContext {
	private val _ready = AtomicBoolean(false)

	/**
	 * Pl3xMap platform readiness state.
	 */
	val isReady: Boolean get() = _ready.get()

	/**
	 * Thread-safe global registries.
	 */
	val worlds = WorldRegistry()
	val layers = LayerRegistry()
	val markers = MarkerRegistry()
	val icons = IconRegistry()

	/**
	 * Internal: set readiness from lifecycle events.
	 */
	fun markReady() = _ready.set(true)
	fun markUnready() = _ready.set(false)

	/**
	 * Clears all registries + readiness for shutdown/restart.
	 */
	fun reset() {
		_ready.set(false)
		worlds.clear()
		layers.clear()
		markers.clear()
		icons.clear()
	}

	/**
	 * Validates readiness, throws descriptive error if not ready.
	 *
	 * Used by all `Pl3xAPI` entry points.
	 */
	fun requireReady() {
		check(isReady) { "Pl3xMap is not ready yet - wait for Pl3xMapEnabledEvent" }
	}
}
