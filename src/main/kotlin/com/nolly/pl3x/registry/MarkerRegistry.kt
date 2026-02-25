package com.nolly.pl3x.registry

import com.nolly.pl3x.marker.MapMarker
import java.util.concurrent.ConcurrentHashMap
import com.nolly.pl3x.layer.MapLayer

/**
 * Thread-safe per-world/layer marker registry.
 *
 * Composite keys: "world:layer:marker". Tracks [MapLayer] additions/removals.
 */
class MarkerRegistry {
	private val markers = ConcurrentHashMap<String, MapMarker>()

	/**
	 * Builds registry key: "world:layer:markerKey".
	 */
	private fun compositeKey(worldName: String, layerKey: String, markerKey: String) = "$worldName:$layerKey:$markerKey"

	/**
	 * Stores [marker] by composite key.
	 */
	fun put(worldName: String, layerKey: String, marker: MapMarker) {
		markers[compositeKey(worldName, layerKey, marker.key)] = marker
	}

	/**
	 * Retrieves marker by composite key. Returns null if not found.
	 */
	fun get(worldName: String, layerKey: String, markerKey: String): MapMarker? =
		markers[compositeKey(worldName, layerKey, markerKey)]

	/**
	 * Checks if marker exists by composite key.
	 */
	fun has(worldName: String, layerKey: String, markerKey: String): Boolean =
		markers.containsKey(compositeKey(worldName, layerKey, markerKey))

	/**
	 * Removes marker by composite key. Returns removed marker or null if not found.
	 */
	fun remove(worldName: String, layerKey: String, markerKey: String): MapMarker? =
		markers.remove(compositeKey(worldName, layerKey, markerKey))

	/**
	 * Clears all markers in [worldName]/[layerKey].
	 */
	fun clearLayer(worldName: String, layerKey: String) {
		markers.keys
			.filter { it.startsWith("$worldName:$layerKey:") }
			.forEach { markers.remove(it) }
	}

	/**
	 * Clears all markers in [worldName].
	 */
	fun clearWorld(worldName: String) {
		markers.keys.filter { it.startsWith("$worldName:") }.forEach { markers.remove(it) }
	}

	/**
	 * @return Snapshot of all registered markers as worldName -> (layerKey -> markerKey -> MapMarker) map.
	 */
	fun all(): Map<String, MapMarker> = markers.toMap()

	/**
	 * Clears entire registry.
	 */
	fun clear() = markers.clear()
}
