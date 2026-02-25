package com.nolly.pl3x.dsl

import com.nolly.pl3x.layer.MapLayer
import com.nolly.pl3x.marker.MapMarker
import com.nolly.pl3x.marker.MarkerBuilder

/**
 * Atomically removes the existing marker with [markerKey], then creates and
 * registers a new one of [type] using the [block].
 *
 * @param markerKey The key of the marker to replace
 * @param type The [MarkerBuilder.Type] of the new marker
 * @param block Configuration for the new [MarkerBuilder]
 * @return The newly created [MapMarker]
 */
fun MapLayer.update(markerKey: String, type: MarkerBuilder.Type, block: MarkerBuilder.() -> Unit): MapMarker {
	removeMarker(markerKey)
	return MarkerBuilder(markerKey, type).world(worldName()).layer(key).apply(block).register()
}
