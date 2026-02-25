package com.nolly.pl3x.marker

import net.pl3x.map.core.markers.Point
import net.pl3x.map.core.markers.marker.Polygon
import net.pl3x.map.core.markers.marker.Polyline
import net.pl3x.map.core.markers.option.Options

/**
 * Complex polygon region with [points] boundary + optional [holes].
 *
 * [holes] define interior cutouts (lakes, etc.).
 * Created via `MarkerBuilder.region()` or `LayerScope.region()`.
 */
class MapRegion(
	key: String,
	val points: List<Point>,
	val holes: List<List<Point>> = emptyList(),
	var options: Options? = null,
	var pane: String? = null,
) : MapMarker(key) {
	override fun toPlx(): Polygon {
		val outer = Polyline(key + "_outer", points)
		val polygon = Polygon(key, outer)
		holes.forEachIndexed { i, holePoints ->
			polygon.addPolyline(Polyline(key + "_hole_$i", holePoints))
		}
		options?.let { polygon.setOptions(it) }
		pane?.let { polygon.setPane(it) }
		return polygon
	}
}
