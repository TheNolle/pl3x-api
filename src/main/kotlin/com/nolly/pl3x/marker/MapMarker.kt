package com.nolly.pl3x.marker

import net.pl3x.map.core.markers.marker.Marker
import com.nolly.pl3x.layer.MapLayer

/**
 * Sealed base for all Pl3xMap markers.
 *
 * Converts to raw Pl3xMap [Marker] via `toPlx()`. Registered via [MapLayer].
 */
sealed class MapMarker(val key: String) {
	abstract fun toPlx(): Marker<*>
	override fun toString() = "${this::class.simpleName}(key=$key)"
	override fun equals(other: Any?) = other is MapMarker && key == other.key
	override fun hashCode() = key.hashCode()
}
