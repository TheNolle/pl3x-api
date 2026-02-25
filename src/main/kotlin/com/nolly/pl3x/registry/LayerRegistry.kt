package com.nolly.pl3x.registry

import com.nolly.pl3x.layer.MapLayer
import java.util.concurrent.ConcurrentHashMap

/**
 * Thread-safe per-world layer registry.
 *
 * Composite keys: "world:layer". Lazy creation via [getOrCreate].
 */
class LayerRegistry {
	private val layers = ConcurrentHashMap<String, MapLayer>()

	/**
	 * Builds registry key: "worldName:layerKey".
	 */
	private fun compositeKey(worldName: String, layerKey: String) = "$worldName:$layerKey"

	/**
	 * Registers [layer] for [worldName].
	 *
	 * Fails if already exists.
	 */
	fun register(worldName: String, layer: MapLayer) {
		val key = compositeKey(worldName, layer.key)
		require(!layers.containsKey(key)) {
			"Layer '${layer.key}' already registered for world '$worldName'"
		}
		layers[key] = layer
	}

	/**
	 * Gets existing or creates via [factory].
	 */
	fun getOrCreate(worldName: String, layerKey: String, factory: () -> MapLayer): MapLayer {
		return layers.getOrPut(compositeKey(worldName, layerKey), factory)
	}

	/**
	 * Clears all layers in [worldName].
	 */
	fun clearWorld(worldName: String) {
		layers.keys.filter { it.startsWith("$worldName:") }.forEach { layers.remove(it) }
	}
	/**
	 * Standard map operations by world/layer key.
	 */
	fun get(worldName: String, layerKey: String): MapLayer? = layers[compositeKey(worldName, layerKey)]

	/**
	 * Removes layer by world/layer key. Returns removed layer or null if not found.
	 */
	fun remove(worldName: String, layerKey: String): MapLayer? = layers.remove(compositeKey(worldName, layerKey))

	/**
	 * @return Snapshot of all registered layers as worldName -> (layerKey -> MapLayer) map.
	 */
	fun all(): Map<String, MapLayer> = layers.toMap()

	/**
	 * Clears all layers from all worlds.
	 */
	fun clear() = layers.clear()
}
