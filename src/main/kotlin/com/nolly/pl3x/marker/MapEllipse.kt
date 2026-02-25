package com.nolly.pl3x.marker

import net.pl3x.map.core.markers.Point
import net.pl3x.map.core.markers.marker.Ellipse
import net.pl3x.map.core.markers.option.Options

/**
 * Ellipse marker with separate X/Z radii and optional [tilt].
 *
 * [tilt] rotates ellipse from horizontal (0° = circle-aligned).
 * Created via `MarkerBuilder.ellipse()` or `LayerScope.ellipse()`.
 */
class MapEllipse(
	key: String,
	val center: Point,
	val radiusX: Double,
	val radiusZ: Double,
	val tilt: Double? = null,
	var options: Options? = null,
	var pane: String? = null,
) : MapMarker(key) {
	override fun toPlx(): Ellipse {
		val ellipse = if (tilt != null) Ellipse(key, center, radiusX, radiusZ, tilt)
		else Ellipse(key, center, radiusX, radiusZ)
		options?.let { ellipse.setOptions(it) }
		pane?.let { ellipse.setPane(it) }
		return ellipse
	}
}
