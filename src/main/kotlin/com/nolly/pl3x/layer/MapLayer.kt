package com.nolly.pl3x.layer

import com.nolly.pl3x.Pl3xContext
import com.nolly.pl3x.marker.MapMarker
import net.pl3x.map.core.markers.layer.SimpleLayer
import net.pl3x.map.core.markers.marker.Marker
import net.pl3x.map.core.world.World

/**
 * Fluent wrapper around Pl3xMap [SimpleLayer] with Kotlin properties.
 *
 * Auto-syncs with [Pl3xContext.markers] registry. Created via [MapWorld.layer()].
 *
 * ```
 * val layer = world.layer("players", "Player Locations") {
 *   updateInterval = 5
 *   defaultHidden = true
 * }
 * ```
 */
class MapLayer(val key: String, label: String, private val world: World) {
	/**
	 * @return This layer's [World] name
	 */
	internal fun worldName(): String = world.name

	/**
	 * Raw Pl3xMap [SimpleLayer] instance.
	 *
	 * Use only for advanced customization.
	 */
	val inner: SimpleLayer = object : SimpleLayer(key, { label }) {}

	/**
	 * Display label shown in Pl3xMap UI layer list.
	 *
	 * Auto-syncs to underlying layer.
	 */
	var label: String = label
		set(value) {
			field = value
			inner.setLabel(value)
		}

	/**
	 * Layer refresh interval in seconds (default 15).
	 *
	 * Auto-syncs to underlying layer.
	 */
	var updateInterval: Int = 15
		set(value) {
			field = value
			inner.setUpdateInterval(value)
		}

	/**
	 * Toggles layer visibility controls in UI (default true).
	 *
	 * Auto-syncs to underlying layer.
	 */
	var showControls: Boolean = true
		set(value) {
			field = value
			inner.setShowControls(value)
		}

	/**
	 * Hides layer by default in UI (default false).
	 *
	 * Auto-syncs to underlying layer.
	 */
	var defaultHidden: Boolean = false
		set(value) {
			field = value
			inner.setDefaultHidden(value)
		}

	/**
	 * Layer draw order in stack (lower = behind, default 99).
	 *
	 * Auto-syncs to underlying layer.
	 */
	var priority: Int = 99
		set(value) {
			field = value
			inner.setPriority(value)
		}

	/**
	 * CSS z-index for layer stacking (default 99).
	 *
	 * Auto-syncs to underlying layer.
	 */
	var zIndex: Int = 99
		set(value) {
			field = value
			inner.setZIndex(value)
		}

	/**
	 * Custom Leaflet pane name for this layer.
	 *
	 * Auto-syncs to underlying layer.
	 */
	var pane: String? = null
		set(value) {
			field = value
			inner.setPane(value)
		}

	/**
	 * Custom CSS class(es) for layer styling.
	 *
	 * Auto-syncs to underlying layer.
	 */
	var css: String? = null
		set(value) {
			field = value
			inner.setCss(value)
		}

	/**
	 * Enables continuous live updates (default false).
	 *
	 * Auto-syncs to underlying layer.
	 */
	var liveUpdate: Boolean = false
		set(value) {
			field = value
			inner.setLiveUpdate(value)
		}

	/**
	 * Adds [marker] to this layer and [Pl3xContext.markers] registry.
	 *
	 * @param marker [MapMarker] to add
	 */
	fun addMarker(marker: MapMarker) {
		inner.addMarker(marker.toPlx())
		Pl3xContext.markers.put(world.name, key, marker)
	}

	/**
	 * Removes marker by [markerKey] from layer and registry.
	 *
	 * Safe if marker doesn't exist.
	 *
	 * @param markerKey Marker key to remove
	 */
	fun removeMarker(markerKey: String) {
		inner.removeMarker(markerKey)
		Pl3xContext.markers.remove(world.name, key, markerKey)
	}

	/**
	 * Checks if layer contains marker by [markerKey].
	 *
	 * @param markerKey Marker key to check
	 * @return true if present
	 */
	fun hasMarker(markerKey: String): Boolean = inner.hasMarker(markerKey)

	/**
	 * Removes all markers from layer and registry.
	 *
	 * Syncs with [Pl3xContext.markers].
	 */
	fun clearMarkers() {
		inner.clearMarkers()
		Pl3xContext.markers.clearLayer(world.name, key)
	}

	/**
	 * @return Raw Pl3xMap markers map from underlying layer
	 */
	fun markers(): Map<String, Marker<*>> = inner.registeredMarkers()

	override fun toString() = "MapLayer(key=$key, world=${world.name})"
	override fun equals(other: Any?) = other is MapLayer && key == other.key && world.name == other.world.name
	override fun hashCode() = 31 * key.hashCode() + world.name.hashCode()
}

/**
 * DSL configuration entry point for [MapLayer] properties.
 *
 * Fluent: returns `this` layer.
 *
 * @param block Configuration lambda
 * @return `this` layer
 */
fun MapLayer.configure(block: MapLayer.() -> Unit): MapLayer = apply(block)
