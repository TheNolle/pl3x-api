package com.nolly.pl3x.dsl

import com.nolly.pl3x.world.MapWorld
import net.pl3x.map.core.markers.Point

/**
 * Re-renders all regions within [radiusBlocks] of this world's spawn point.
 *
 * @param radiusBlocks Block radius from spawn to render
 */
fun MapWorld.renderAroundSpawn(radiusBlocks: Int) {
	val spawnPoint = spawn
	render.radiusRender(spawnPoint.x, spawnPoint.z, radiusBlocks)
}

/**
 * Re-renders all regions within [radiusBlocks] of the given block coordinates.
 *
 * @param blockX Center X coordinate (block units)
 * @param blockZ Center Z coordinate (block units)
 * @param radiusBlocks Block radius to render
 */
fun MapWorld.renderAround(blockX: Int, blockZ: Int, radiusBlocks: Int) {
	render.radiusRender(blockX, blockZ, radiusBlocks)
}

/**
 * Re-renders only the regions containing any currently online players.
 *
 * Efficient for worlds with sparse player activity.
 */
fun MapWorld.renderPlayerRegions() {
	val regions = players().map { it.position }.map { pos -> Point.of(pos.x shr 9, pos.z shr 9) }.distinct()
	render.regions(regions)
}
