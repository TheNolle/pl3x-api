package com.nolly.pl3x.marker

import net.pl3x.map.core.markers.Point
import net.pl3x.map.core.markers.Vector
import net.pl3x.map.core.markers.marker.Icon
import net.pl3x.map.core.markers.option.Options

/**
 * Custom image icon marker at [point] using registered [imageKey].
 *
 * Supports [size], [anchor], [rotationAngle], and [rotationOrigin].
 * Created via `MarkerBuilder.iconMarker()` or `LayerScope.iconMarker()`.
 */
class MapIconMarker(
	key: String,
	val point: Point,
	val imageKey: String,
	val size: Vector? = null,
	val anchor: Vector? = null,
	val rotationAngle: Double? = null,
	val rotationOrigin: String? = null,
	var options: Options? = null,
	var pane: String? = null,
) : MapMarker(key) {
	override fun toPlx(): Icon {
		val icon = Icon(key, point, imageKey, size)
		anchor?.let { icon.setAnchor(it) }
		rotationAngle?.let { icon.setRotationAngle(it) }
		rotationOrigin?.let { icon.setRotationOrigin(it) }
		options?.let { icon.setOptions(it) }
		pane?.let { icon.setPane(it) }
		return icon
	}
}
