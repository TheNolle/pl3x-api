package com.nolly.pl3x.registry

import net.pl3x.map.core.Pl3xMap
import net.pl3x.map.core.image.IconImage
import java.awt.image.BufferedImage
import java.util.concurrent.ConcurrentHashMap
import com.nolly.pl3x.icon.IconBuilder

/**
 * Thread-safe registry for custom icons.
 *
 * Tracks [IconBuilder] registrations. Syncs with Pl3xMap.iconRegistry.
 */
class IconRegistry {
	private val registeredKeys = ConcurrentHashMap.newKeySet<String>()

	/**
	 * Registers [image] as icon with given [key]/[format].
	 *
	 * Validates no overwrite unless allowed.
	 */
	fun register(key: String, image: BufferedImage, format: String = "png", overwrite: Boolean = false) {
		val registry = Pl3xMap.api().iconRegistry
		if (!overwrite) {
			require(!registry.has(key)) { "Icon '$key' is already registered" }
		}
		val iconImage = IconImage(key, image, format)
		registry.register(iconImage)
		registeredKeys += key
	}

	/**
	 * Removes icon by [key] from Pl3xMap.
	 */
	fun unregister(key: String) {
		Pl3xMap.api().iconRegistry.unregister(key)
		registeredKeys -= key
	}

	/**
	 * Checks if icon [key] exists in Pl3xMap.
	 */
	fun has(key: String): Boolean = Pl3xMap.api().iconRegistry.has(key)

	/**
	 * Retrieves raw [IconImage] by [key].
	 */
	fun get(key: String): IconImage? = Pl3xMap.api().iconRegistry.get(key)

	/**
	 * @return Snapshot of tracked icon keys
	 */
	fun registeredKeys(): Set<String> = registeredKeys.toSet()

	/**
	 * Removes tracking without unregistering from Pl3xMap.
	 */
	fun untrack(key: String) = registeredKeys.remove(key)

	/**
	 * Clears tracking (icons remain in Pl3xMap).
	 */
	fun clear() = registeredKeys.clear()
}
