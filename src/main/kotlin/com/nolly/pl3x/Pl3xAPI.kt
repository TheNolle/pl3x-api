package com.nolly.pl3x

import com.nolly.pl3x.dsl.LayerScope
import com.nolly.pl3x.dsl.draw
import com.nolly.pl3x.event.Pl3xEventBridge
import com.nolly.pl3x.icon.IconBuilder
import com.nolly.pl3x.layer.MapLayer
import com.nolly.pl3x.marker.MarkerBuilder
import com.nolly.pl3x.player.MapPlayer
import com.nolly.pl3x.world.MapWorld
import net.pl3x.map.core.Pl3xMap
import java.util.*

/**
 * Main entry point for all Pl3xMap operations.
 *
 * **Usage:**
 *
 * 1. **Bootstrap first** (in plugin `onEnable()`):
 *    ```
 *    Pl3xBootstrap.attach()
 *    Pl3xBootstrap.onEnabled {
 *      // Pl3xMap ready - safe to use API
 *    }
 *    ```
 *
 * 2. **Worlds & Layers:**
 *    ```
 *    val world = Pl3xAPI.world("world")
 *    val layer = world.layer("players", "Player Locations")
 *    ```
 *
 * 3. **Quick Draw DSL:**
 *    ```
 *    Pl3xAPI.draw("world", "markers") {
 *      circle("spawn") { center(world.spawn.x, world.spawn.z); radius(50.0) }
 *      rectangle("zone", 100, 100, 200, 200)
 *    }
 * ```
 *
 * 4. **Markers via Factory:**
 *    ```
 *    Pl3xAPI.circle("circle1")
 *      .world("world")
 *      .layer("markers")
 *      .center(0, 0)
 *      .radius(25.0)
 *      .stroke("#ff0000", 3)
 *      .register()
 *    ```
 *
 * **Requires:** `Pl3xBootstrap.attach()` + `Pl3xMapEnabledEvent`
 *
 * All methods validate Pl3xContext readiness.
 */
object Pl3xAPI {
	/**
	 * Access to event bridge for world load/unload callbacks.
	 *
	 * ```
	 * Pl3xAPI.events.onWorldLoad { world ->
	 *   println("Loaded: ${world.name}")
	 * }
	 * ```
	 */
	val events: Pl3xEventBridge get() = Pl3xEventBridge

	/**
	 * Gets [MapWorld] wrapper by name (lazy creation).
	 *
	 * @throws IllegalStateException if Pl3xMap not ready
	 */
	fun world(name: String): MapWorld {
		Pl3xContext.requireReady()
		return Pl3xContext.worlds.getOrCreate(name)
	}

	/**
	 * @return List of all loaded [MapWorld] wrappers
	 */
	fun worlds(): List<MapWorld> {
		Pl3xContext.requireReady()
		return Pl3xMap.api().worldRegistry.values().map { Pl3xContext.worlds.getOrCreate(it.name) }
	}

	/**
	 * Gets [MapPlayer] by UUID or null.
	 */
	fun player(uuid: UUID): MapPlayer? {
		Pl3xContext.requireReady()
		return Pl3xMap.api().playerRegistry.get(uuid)?.let { MapPlayer(it) }
	}

	/**
	 * @return Live list of all online [MapPlayer]
	 */
	fun players(): List<MapPlayer> {
		Pl3xContext.requireReady()
		return Pl3xMap.api().playerRegistry.values().map { MapPlayer(it) }
	}

	/**
	 * Creates [IconBuilder] for custom image registration.
	 *
	 * ```
	 * Pl3xAPI.icon("my_marker")
	 *   .image(myImage)
	 *   .register()
	 * ```
	 */
	fun icon(key: String): IconBuilder {
		Pl3xContext.requireReady()
		return IconBuilder(key)
	}

	/**
	 * Creates [MarkerBuilder] for complex polygon regions.
	 */
	fun region(key: String): MarkerBuilder {
		Pl3xContext.requireReady()
		return MarkerBuilder(key, MarkerBuilder.Type.REGION)
	}

	/**
	 * Creates [MarkerBuilder] for circular markers.
	 */
	fun circle(key: String): MarkerBuilder {
		Pl3xContext.requireReady()
		return MarkerBuilder(key, MarkerBuilder.Type.CIRCLE)
	}

	/**
	 * Creates [MarkerBuilder] for elliptical markers.
	 */
	fun ellipse(key: String): MarkerBuilder {
		Pl3xContext.requireReady()
		return MarkerBuilder(key, MarkerBuilder.Type.ELLIPSE)
	}

	/**
	 * Creates [MarkerBuilder] for connected line paths.
	 */
	fun polyline(key: String): MarkerBuilder {
		Pl3xContext.requireReady()
		return MarkerBuilder(key, MarkerBuilder.Type.POLYLINE)
	}

	/**
	 * Creates [MarkerBuilder] for axis-aligned rectangles.
	 */
	fun rectangle(key: String): MarkerBuilder {
		Pl3xContext.requireReady()
		return MarkerBuilder(key, MarkerBuilder.Type.RECTANGLE)
	}

	/**
	 * Creates [MarkerBuilder] for custom image icons.
	 */
	fun iconMarker(key: String): MarkerBuilder {
		Pl3xContext.requireReady()
		return MarkerBuilder(key, MarkerBuilder.Type.ICON)
	}

	/**
	 * Creates [MarkerBuilder] for multiple polygons.
	 */
	fun multiPolygon(key: String): MarkerBuilder {
		Pl3xContext.requireReady()
		return MarkerBuilder(key, MarkerBuilder.Type.MULTI_POLYGON)
	}

	/**
	 * Creates [MarkerBuilder] for multiple line groups.
	 */
	fun multiPolyline(key: String): MarkerBuilder {
		Pl3xContext.requireReady()
		return MarkerBuilder(key, MarkerBuilder.Type.MULTI_POLYLINE)
	}

	/**
	 * Quick DSL entry: creates layer + draws markers.
	 *
	 * ```
	 * Pl3xAPI.draw("world", "test") {
	 *   circle("center") { center(0, 0); radius(10) }
	 * }
	 * ```
	 */
	fun draw(worldName: String, layerKey: String, layerLabel: String = layerKey, block: LayerScope.() -> Unit) {
		Pl3xContext.requireReady()
		world(worldName).draw(layerKey, layerLabel, block)
	}

	/**
	 * Gets first world's layer by key (convenience).
	 */
	fun layer(key: String): MapLayer = worlds().first().layer(key)

	/**
	 * Auto-discovers common layers across worlds.
	 *
	 * Priority: `{world}_default`, `{world}`, `default`, `main`
	 */
	fun layers(): List<MapLayer> = worlds().flatMap {
		listOfNotNull(
			it.layerOrNull(it.name + "_default") ?: it.layerOrNull(it.name) ?: it.layerOrNull("default")
			?: it.layerOrNull("main")
		)
	}

	/**
	 * Raw Pl3xMap API instance.
	 */
	fun raw(): Pl3xMap = Pl3xMap.api()
}
