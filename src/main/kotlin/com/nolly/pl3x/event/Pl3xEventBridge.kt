package com.nolly.pl3x.event

import net.pl3x.map.core.Pl3xMap
import net.pl3x.map.core.event.EventHandler
import net.pl3x.map.core.event.EventListener
import net.pl3x.map.core.event.RegisteredHandler
import net.pl3x.map.core.event.server.Pl3xMapDisabledEvent
import net.pl3x.map.core.event.server.Pl3xMapEnabledEvent
import net.pl3x.map.core.event.server.ServerLoadedEvent
import net.pl3x.map.core.event.world.WorldLoadedEvent
import net.pl3x.map.core.event.world.WorldUnloadedEvent
import net.pl3x.map.core.world.World
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Bridges Pl3xMap core events to simple Kotlin lambdas.
 *
 * Registers internal listeners and forwards events to your callbacks.
 * Automatically handles registration on Pl3xMap startup.
 *
 * ```
 * Pl3xEventBridge.onWorldLoad { world ->
 *   println("World ${world.name} loaded")
 * }
 * ```
 */
object Pl3xEventBridge {
	private val worldLoadListeners = CopyOnWriteArrayList<(World) -> Unit>()
	private val worldUnloadListeners = CopyOnWriteArrayList<(World) -> Unit>()
	private val mapEnabledListeners = CopyOnWriteArrayList<() -> Unit>()
	private val mapDisabledListeners = CopyOnWriteArrayList<() -> Unit>()
	private val serverLoadedListeners = CopyOnWriteArrayList<() -> Unit>()

	private var registered = false
	private val listener = object : EventListener {
		@EventHandler
		fun onWorldLoaded(event: WorldLoadedEvent) {
			worldLoadListeners.forEach { it(event.world) }
		}

		@EventHandler
		fun onWorldUnloaded(event: WorldUnloadedEvent) {
			worldUnloadListeners.forEach { it(event.world) }
		}

		@EventHandler
		fun onEnabled(event: Pl3xMapEnabledEvent) {
			mapEnabledListeners.forEach { it() }
		}

		@EventHandler
		fun onDisabled(event: Pl3xMapDisabledEvent) {
			mapDisabledListeners.forEach { it() }
		}

		@EventHandler
		fun onServerLoaded(event: ServerLoadedEvent) {
			serverLoadedListeners.forEach { it() }
		}
	}

	/**
	 * Registers this bridge with Pl3xMap's event registry.
	 *
	 * Idempotent: safe to call multiple times.
	 */
	fun register() {
		if (registered) return
		registered = true
		Pl3xMap.api().eventRegistry.register(listener)
	}

	/**
	 * Unregisters all listeners and clears callbacks.
	 *
	 * Cleans up reflection-based handler removal for graceful shutdown.
	 */
	fun unregister() {
		if (!registered) return
		registered = false
		unregisterFromEvent(WorldLoadedEvent::class.java)
		unregisterFromEvent(WorldUnloadedEvent::class.java)
		unregisterFromEvent(Pl3xMapEnabledEvent::class.java)
		unregisterFromEvent(Pl3xMapDisabledEvent::class.java)
		unregisterFromEvent(ServerLoadedEvent::class.java)
		clearAll()
	}

	private fun unregisterFromEvent(eventClass: Class<*>) {
		try {
			val handlersField = eventClass.getDeclaredField("handlers")
			handlersField.isAccessible = true
			@Suppress("UNCHECKED_CAST")
			val handlers = handlersField.get(null) as MutableList<RegisteredHandler>
			handlers.removeIf { it.listener === listener }
		} catch (_: Exception) {
		}
	}

	/**
	 * Adds callback for [WorldLoadedEvent].
	 *
	 * Called when any world loads in the server.
	 *
	 * @param block Lambda receiving the loaded [World]
	 */
	fun onWorldLoad(block: (World) -> Unit) {
		worldLoadListeners += block
	}

	/**
	 * Adds callback for [WorldUnloadedEvent].
	 *
	 * Called when any world unloads from the server.
	 *
	 * @param block Lambda receiving the unloaded [World]
	 */
	fun onWorldUnload(block: (World) -> Unit) {
		worldUnloadListeners += block
	}

	/**
	 * Adds callback for [Pl3xMapEnabledEvent].
	 *
	 * Called once when Pl3xMap plugin fully initializes.
	 *
	 * @param block No-arg lambda
	 */
	fun onMapEnabled(block: () -> Unit) {
		mapEnabledListeners += block
	}

	/**
	 * Adds callback for [Pl3xMapDisabledEvent].
	 *
	 * Called once when Pl3xMap plugin shuts down.
	 *
	 * @param block No-arg lambda
	 */
	fun onMapDisabled(block: () -> Unit) {
		mapDisabledListeners += block
	}

	/**
	 * Adds callback for [ServerLoadedEvent].
	 *
	 * Called once after server fully loads (post-worlds).
	 *
	 * @param block No-arg lambda
	 */
	fun onServerLoaded(block: () -> Unit) {
		serverLoadedListeners += block
	}

	/**
	 * Clears all registered callbacks without unregistering listeners.
	 *
	 * Use before re-registering new handlers.
	 */
	fun clearAll() {
		worldLoadListeners.clear()
		worldUnloadListeners.clear()
		mapEnabledListeners.clear()
		mapDisabledListeners.clear()
		serverLoadedListeners.clear()
	}
}
