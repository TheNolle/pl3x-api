package com.nolly.pl3x.dsl

import com.nolly.pl3x.marker.MarkerBuilder
import com.nolly.pl3x.marker.points

/**
 * Adds a straight line between two points as a 2-point polygon.
 *
 * @param x1 First point X (block units)
 * @param z1 First point Z (block units)
 * @param x2 Second point X (block units)
 * @param z2 Second point Z (block units)
 * @return `this` for chaining
 */
fun MarkerBuilder.line(x1: Int, z1: Int, x2: Int, z2: Int): MarkerBuilder {
	return run {
		polygon {
			point(x1, z1)
			point(x2, z2)
		}
	}
}

/**
 * Adds a square box centered at [centerX], [centerZ] with each side extending
 * [halfSize] blocks from the center.
 *
 * @param centerX Center X coordinate (block units)
 * @param centerZ Center Z coordinate (block units)
 * @param halfSize Half the side length (total side = [halfSize] × 2)
 * @return `this` for chaining
 */
fun MarkerBuilder.box(centerX: Int, centerZ: Int, halfSize: Int): MarkerBuilder {
	val x1 = centerX - halfSize
	val z1 = centerZ - halfSize
	val x2 = centerX + halfSize
	val z2 = centerZ + halfSize
	return corners(x1, z1, x2, z2)
}

/**
 * Adds a ring (annulus) with an outer and inner radius, creating a hollow circle.
 *
 * The ring is approximated using [segments] points around each circle.
 *
 * @param centerX Center X coordinate (block units)
 * @param centerZ Center Z coordinate (block units)
 * @param outerRadius Outer circle radius (block units)
 * @param innerRadius Inner circle radius (block units)
 * @param segments Number of points to approximate each circle (min 3, default 32)
 * @return `this` for chaining
 */
fun MarkerBuilder.ring(
	centerX: Int,
	centerZ: Int,
	outerRadius: Int,
	innerRadius: Int,
	segments: Int = 32,
): MarkerBuilder {
	require(segments >= 3) { "Ring requires at least 3 segments" }

	val outer = points {
		repeat(segments) { i ->
			val angle = 2.0 * Math.PI * i / segments
			val x = centerX + outerRadius * kotlin.math.cos(angle)
			val z = centerZ + outerRadius * kotlin.math.sin(angle)
			point(x, z)
		}
	}
	val inner = points {
		repeat(segments) { i ->
			val angle = 2.0 * Math.PI * i / segments
			val x = centerX + innerRadius * kotlin.math.cos(angle)
			val z = centerZ + innerRadius * kotlin.math.sin(angle)
			point(x, z)
		}
	}
	polygon { outer.forEach { point(it) } }
	hole { inner.forEach { point(it) } }
	return this
}
