package com.nolly.pl3x.dsl

import com.nolly.pl3x.layer.MapLayer
import com.nolly.pl3x.marker.MapMarker
import com.nolly.pl3x.marker.MarkerBuilder
import com.nolly.pl3x.world.MapWorld
import com.nolly.pl3x.registry.MarkerRegistry

/**
 * Receiver scope used inside `draw {}` DSL blocks.
 *
 * Provides factory functions for all marker types, automatically bound to the
 * specified [world] and [layer]. Supports layer configuration, marker removal,
 * and atomic replacement.
 *
 * @property world The [MapWorld] this scope operates on
 * @property layer The [MapLayer] all markers are added to
 */
class LayerScope(val world: MapWorld, val layer: MapLayer) {
	fun circle(key: String, block: MarkerBuilder.() -> Unit = {}): MapMarker =
		bound(MarkerBuilder(key, MarkerBuilder.Type.CIRCLE), block)

	fun ellipse(key: String, block: MarkerBuilder.() -> Unit = {}): MapMarker =
		bound(MarkerBuilder(key, MarkerBuilder.Type.ELLIPSE), block)

	fun region(key: String, block: MarkerBuilder.() -> Unit = {}): MapMarker =
		bound(MarkerBuilder(key, MarkerBuilder.Type.REGION), block)

	fun polyline(key: String, block: MarkerBuilder.() -> Unit = {}): MapMarker =
		bound(MarkerBuilder(key, MarkerBuilder.Type.POLYLINE), block)

	fun rectangle(key: String, block: MarkerBuilder.() -> Unit = {}): MapMarker =
		bound(MarkerBuilder(key, MarkerBuilder.Type.RECTANGLE), block)

	fun iconMarker(key: String, block: MarkerBuilder.() -> Unit = {}): MapMarker =
		bound(MarkerBuilder(key, MarkerBuilder.Type.ICON), block)

	fun multiPolygon(key: String, block: MarkerBuilder.() -> Unit = {}): MapMarker =
		bound(MarkerBuilder(key, MarkerBuilder.Type.MULTI_POLYGON), block)

	fun multiPolyline(key: String, block: MarkerBuilder.() -> Unit = {}): MapMarker =
		bound(MarkerBuilder(key, MarkerBuilder.Type.MULTI_POLYLINE), block)

	/**
	 * Configures this layer's properties using the given [block].
	 *
	 * @param block Configuration lambda that receives `this` layer
	 */
	fun configure(block: MapLayer.() -> Unit) = layer.block()

	/**
	 * Removes the marker with the given [markerKey] from this layer.
	 *
	 * Syncs with the internal [MarkerRegistry].
	 *
	 * @param markerKey The marker's key
	 */
	fun remove(markerKey: String) = layer.removeMarker(markerKey)

	/**
	 * Atomically removes the existing marker with [markerKey], then creates and
	 * adds a new one using the [block].
	 *
	 * @param markerKey The key of the marker to replace
	 * @param block DSL block that produces a new [MapMarker]
	 * @return The newly created [MapMarker]
	 */
	fun replace(markerKey: String, block: LayerScope.() -> MapMarker) {
		layer.removeMarker(markerKey)
		block()
	}

	/**
	 * Removes all markers from this layer.
	 *
	 * Syncs with the internal [MarkerRegistry].
	 */
	fun clear() = layer.clearMarkers()

	/**
	 * Binds the [builder] to the current [world] and [layer], applies the [block],
	 * registers it, and returns the resulting [MapMarker].
	 */
	private fun bound(builder: MarkerBuilder, block: MarkerBuilder.() -> Unit): MapMarker {
		builder.world(world.name).layer(layer.key, layer.label)
		builder.block()
		return builder.register()
	}
}

/**
 * DSL entry point for drawing markers on a layer of this world.
 *
 * Creates or gets the layer with [layerKey], then runs the [block] with a
 * [LayerScope] receiver.
 *
 * @param layerKey Stable identifier for the layer (Pl3xMap layer key)
 * @param layerLabel Display label shown in the Pl3xMap UI (defaults to [layerKey])
 * @param block DSL block that creates markers
 */
fun MapWorld.draw(layerKey: String, layerLabel: String = layerKey, block: LayerScope.() -> Unit) {
	val l = layer(layerKey, layerLabel)
	LayerScope(this, l).block()
}
