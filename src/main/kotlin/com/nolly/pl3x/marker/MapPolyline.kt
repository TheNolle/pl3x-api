package com.nolly.pl3x.marker

import net.pl3x.map.core.markers.Point
import net.pl3x.map.core.markers.marker.Polyline
import net.pl3x.map.core.markers.option.Options

/**
 * Connected line through [points] sequence.
 *
 * Created via `MarkerBuilder.polyline()` or `LayerScope.polyline()`.
 */
class MapPolyline(
	key: String,
	val points: List<Point>,
	var options: Options? = null,
	var pane: String? = null,
) : MapMarker(key) {
	override fun toPlx(): Polyline {
		val line = Polyline(key, points)
		options?.let { line.setOptions(it) }
		pane?.let { line.setPane(it) }
		return line
	}
}
