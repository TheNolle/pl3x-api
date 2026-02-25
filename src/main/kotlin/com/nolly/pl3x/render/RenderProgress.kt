package com.nolly.pl3x.render

import net.pl3x.map.core.renderer.progress.Progress
import net.pl3x.map.core.world.World
import java.util.concurrent.atomic.AtomicLong

/**
 * Live snapshot of world render progress.
 *
 * Wraps Pl3xMap [Progress] with Kotlin properties.
 */
class RenderProgress(private val inner: Progress) {
	/**
	 * World being rendered (nullable).
	 */
	val world: World? get() = inner.world

	/**
	 * Total regions to render.
	 */
	val totalRegions: Long get() = inner.totalRegions

	/**
	 * Live count of rendered regions.
	 */
	val processedRegions: AtomicLong get() = inner.processedRegions

	/**
	 * Total chunks to render.
	 */
	val totalChunks: Long get() = inner.totalChunks

	/**
	 * Live count of rendered chunks.
	 */
	val processedChunks: AtomicLong get() = inner.processedChunks

	/**
	 * Completion percentage (0.0-100.0).
	 */
	val percent: Float get() = inner.percent

	/**
	 * Chunks per second rate.
	 */
	val cps: Double get() = inner.cps

	/**
	 * Estimated time remaining string.
	 */
	val eta: String get() = inner.eta

	override fun toString() =
		"RenderProgress(world=${world?.name ?: "unknown"}, regions=$processedRegions/$totalRegions, ${percent}%, eta=${eta}s)"
}
