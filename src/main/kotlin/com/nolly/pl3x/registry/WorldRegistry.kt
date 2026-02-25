package com.nolly.pl3x.registry

import com.nolly.pl3x.world.MapWorld
import net.pl3x.map.core.Pl3xMap
import java.util.concurrent.ConcurrentHashMap

/**
 * Lazy registry for [MapWorld] wrappers.
 *
 * Validates Pl3xMap world exists before wrapping.
 */
class WorldRegistry {
	private val worlds = ConcurrentHashMap<String, MapWorld>()

	/**
	 * Gets existing or creates [MapWorld] wrapper.
	 *
	 * Errors if world not loaded in Pl3xMap.
	 */
	fun getOrCreate(name: String): MapWorld {
		return worlds.getOrPut(name) {
			val pl3xWorld = Pl3xMap.api().worldRegistry.get(name) ?: error("World '$name' is not loaded in Pl3xMap")
			MapWorld(pl3xWorld)
		}
	}

	/**
	 * Retrieves [MapWorld] wrapper by name. Returns null if not found.
	 */
	fun get(name: String): MapWorld? = worlds[name]

	/**
	 * Checks if [MapWorld] wrapper exists by name.
	 */
	fun all(): Map<String, MapWorld> = worlds.toMap()

	/**
	 * Removes [MapWorld] wrapper by name. Returns removed wrapper or null if not found.
	 */
	fun remove(name: String) = worlds.remove(name)

	/**
	 * Clears all [MapWorld] wrappers. Does not affect Pl3xMap world registry.
	 */
	fun clear() = worlds.clear()
}
