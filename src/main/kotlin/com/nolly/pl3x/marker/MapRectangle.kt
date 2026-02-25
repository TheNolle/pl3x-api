package com.nolly.pl3x.marker

import net.pl3x.map.core.markers.Point
import net.pl3x.map.core.markers.marker.Rectangle
import net.pl3x.map.core.markers.option.Options

/**
 * Axis-aligned rectangle between diagonal [point1] and [point2].
 *
 * Created via `MarkerBuilder.rectangle()` or `LayerScope.rectangle()`.
 */
class MapRectangle(
	key: String,
	val point1: Point,
	val point2: Point,
	var options: Options? = null,
	var pane: String? = null,
) : MapMarker(key) {
	override fun toPlx(): Rectangle {
		val rect = Rectangle(key, point1, point2)
		options?.let { rect.setOptions(it) }
		pane?.let { rect.setPane(it) }
		return rect
	}
}
