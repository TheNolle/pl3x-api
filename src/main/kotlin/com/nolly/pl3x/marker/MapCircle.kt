package com.nolly.pl3x.marker

import net.pl3x.map.core.markers.Point
import net.pl3x.map.core.markers.marker.Circle
import net.pl3x.map.core.markers.option.Options

/**
 * Circle marker centered at [center] with given [radius] (block units).
 *
 * Created via `MarkerBuilder.circle()` or `LayerScope.circle()`.
 */
class MapCircle(
	key: String,
	val center: Point,
	val radius: Double,
	var options: Options? = null,
	var pane: String? = null,
) : MapMarker(key) {
	override fun toPlx(): Circle {
		val circle = Circle(key, center, radius)
		options?.let { circle.setOptions(it) }
		pane?.let { circle.setPane(it) }
		return circle
	}
}
