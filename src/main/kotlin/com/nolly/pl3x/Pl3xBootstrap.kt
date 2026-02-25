package com.nolly.pl3x

import com.nolly.pl3x.event.Pl3xEventBridge
import net.pl3x.map.core.Pl3xMap
import net.pl3x.map.core.event.EventHandler
import net.pl3x.map.core.event.EventListener
import net.pl3x.map.core.event.RegisteredHandler
import net.pl3x.map.core.event.server.Pl3xMapDisabledEvent
import net.pl3x.map.core.event.server.Pl3xMapEnabledEvent

/**
 * Plugin lifecycle manager for Pl3xMap integration.
 *
 * **Required Usage in Plugin:**
 *
 * ```
 * class MyPlugin : JavaPlugin() {
 *   override fun onEnable() {
 *     Pl3xBootstrap.attach()
 *     Pl3xBootstrap.onEnabled {
 *       logger.info("Pl3xMap ready!")
 *       // Safe to use Pl3xAPI here
 *     }
 *     Pl3xBootstrap.onDisabled {
 *       logger.info("Pl3xMap shutdown")
 *     }
 *   }
 *
 *   override fun onDisable() {
 *     Pl3xBootstrap.detach()
 *   }
 * }
 * ```
 *
 * Handles Pl3xMap lifecycle, context readiness, and event bridge.
 */
object Pl3xBootstrap {
	private var attached = false
	private val enabledListeners = mutableListOf<() -> Unit>()
	private val disabledListeners = mutableListOf<() -> Unit>()

	private val eventListener = object : EventListener {
		@EventHandler
		fun onEnabled(event: Pl3xMapEnabledEvent) {
			Pl3xContext.markReady()
			enabledListeners.forEach { it() }
		}

		@EventHandler
		fun onDisabled(event: Pl3xMapDisabledEvent) {
			Pl3xContext.markUnready()
			disabledListeners.forEach { it() }
		}
	}

	/**
	 * Attaches lifecycle listener + event bridge to Pl3xMap.
	 *
	 * **Idempotent:** Safe to call multiple times.
	 *
	 * Call in `JavaPlugin.onEnable()`.
	 */
	fun attach() {
		if (attached) return
		attached = true
		Pl3xMap.api().eventRegistry.register(eventListener)
		Pl3xEventBridge.register()
	}

	/**
	 * Detaches all listeners + resets context.
	 *
	 * Cleans up via reflection for graceful shutdown.
	 *
	 * Call in `JavaPlugin.onDisable()`.
	 */
	fun detach() {
		if (!attached) return
		attached = false
		Pl3xEventBridge.unregister()
		unregisterFromEvent(Pl3xMapEnabledEvent::class.java)
		unregisterFromEvent(Pl3xMapDisabledEvent::class.java)
		Pl3xContext.reset()
	}

	private fun unregisterFromEvent(eventClass: Class<*>) {
		try {
			val field = eventClass.getDeclaredField("handlers")
			field.isAccessible = true
			@Suppress("UNCHECKED_CAST")
			val handlers = field.get(null) as MutableList<RegisteredHandler>
			handlers.removeIf { it.listener === eventListener }
		} catch (_: Exception) {
		}
	}

	/**
	 * Adds callback for [Pl3xMapEnabledEvent].
	 *
	 * Called once after Pl3xMap fully initializes.
	 * First safe point to use `Pl3xAPI`.
	 */
	fun onEnabled(block: () -> Unit) {
		enabledListeners += block
	}

	/**
	 * Adds callback for [Pl3xMapDisabledEvent].
	 *
	 * Called once before Pl3xMap shutdown.
	 */
	fun onDisabled(block: () -> Unit) {
		disabledListeners += block
	}

	/**
	 * True when Pl3xMapEnabledEvent fired.
	 *
	 * `Pl3xAPI` safe to use when true.
	 */
	val isReady: Boolean get() = Pl3xContext.isReady
}
