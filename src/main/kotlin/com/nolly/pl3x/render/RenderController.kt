package com.nolly.pl3x.render

import net.pl3x.map.core.Pl3xMap
import net.pl3x.map.core.markers.Point
import net.pl3x.map.core.world.World

/**
 * Controls map rendering for a specific [World].
 *
 * Queues regions/blocks for re-rendering via Pl3xMap regionProcessor.
 */
class RenderController(private val world: World) {
	/**
	 * Lazy [DoubleCheckerController] for this world.
	 */
	val doubleChecker: DoubleCheckerController by lazy { DoubleCheckerController() }

	/**
	 * Queues entire world for full re-render.
	 *
	 * Uses currently-loaded regions only.
	 */
	fun full() {
		val regions = world.listRegions(true)
		if (regions.isEmpty()) return
		Pl3xMap.api().regionProcessor.addRegions(world, regions)
	}

	/**
	 * Queues single region for re-render.
	 *
	 * @param regionX Region X coordinate (>> 9 from blocks)
	 * @param regionZ Region Z coordinate (>> 9 from blocks)
	 */
	fun region(regionX: Int, regionZ: Int) {
		Pl3xMap.api().regionProcessor.addRegions(world, listOf(Point.of(regionX, regionZ)))
	}

	/**
	 * Queues region containing [blockX],[blockZ].
	 *
	 * Converts block coords to region via `>> 9`.
	 */
	fun regionAtBlock(blockX: Int, blockZ: Int) = region(blockX shr 9, blockZ shr 9)

	/**
	 * Queues multiple regions for re-render.
	 *
	 * Skips if empty collection.
	 */
	fun regions(regions: Collection<Point>) {
		if (regions.isEmpty()) return
		Pl3xMap.api().regionProcessor.addRegions(world, regions)
	}

	/**
	 * Queues regions within [radius] blocks of center.
	 *
	 * Filters existing loaded regions only.
	 */
	fun radiusRender(centerX: Int, centerZ: Int, radius: Int) {
		val rX = centerX shr 9
		val rZ = centerZ shr 9
		val rR = radius shr 9
		val existing = world.listRegions(true)
		val filtered = existing.filter { it.x in (rX - rR)..(rX + rR) && it.z in (rZ - rR)..(rZ + rR) }
		if (filtered.isEmpty()) return
		Pl3xMap.api().regionProcessor.addRegions(world, filtered)
	}

	/**
	 * Pauses all rendering for this world.
	 */
	fun pause() {
		Pl3xMap.api().regionProcessor.isPaused = true
	}

	/**
	 * Resumes rendering for this world.
	 */
	fun resume() {
		Pl3xMap.api().regionProcessor.isPaused = false
	}

	/**
	 * Current render pause state.
	 */
	val isPaused: Boolean get() = Pl3xMap.api().regionProcessor.isPaused

	/**
	 * @return Current render progress or null if not this world
	 */
	fun progress(): RenderProgress? {
		val p = Pl3xMap.api().regionProcessor.progress
		if (p.world?.name != world.name) return null
		return RenderProgress(p)
	}

	/**
	 * @return All worlds with queued renders
	 */
	fun queuedWorlds(): Set<World> = Pl3xMap.api().regionProcessor.queuedWorlds
}
