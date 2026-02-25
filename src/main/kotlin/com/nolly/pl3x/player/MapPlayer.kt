package com.nolly.pl3x.player

import net.pl3x.map.core.markers.Point
import net.pl3x.map.core.player.Player
import java.net.URL
import java.util.*

/**
 * Fluent wrapper around Pl3xMap [Player] with Kotlin properties.
 *
 * Provides live player data: position, health, status flags, skin.
 * Use `Pl3xAPI.player(uuid)` or iterate `Pl3xAPI.players()`.
 */
class MapPlayer(private val inner: Player) {
	/**
	 * Unique player identifier.
	 */
	val uuid: UUID get() = inner.uuid

	/**
	 * Player display name.
	 */
	val name: String get() = inner.name

	/**
	 * Current block position as [Point].
	 */
	val position: Point get() = inner.position

	/**
	 * Player facing direction (degrees).
	 */
	val yaw: Float get() = inner.yaw

	/**
	 * Current health points (0-20).
	 */
	val health: Int get() = inner.health

	/**
	 * Armor protection points (0-20).
	 */
	val armorPoints: Int get() = inner.armorPoints

	/**
	 * Player hidden from map (live flag).
	 */
	val isHidden: Boolean get() = inner.isHidden

	/**
	 * True if NPC (not real player).
	 */
	val isNpc: Boolean get() = inner.isNPC

	/**
	 * Player invisibility potion effect.
	 */
	val isInvisible: Boolean get() = inner.isInvisible

	/**
	 * Player in spectator mode (no collision, flying).
	 */
	val isSpectator: Boolean get() = inner.isSpectator

	/**
	 * Player skin texture URL (nullable).
	 */
	val skinUrl: URL? get() = inner.skin

	/**
	 * Current world name.
	 */
	val worldName: String get() = inner.world.name

	/**
	 * Hides/shows player on map.
	 *
	 * @param hidden Hide if true
	 * @param persistent Persist across sessions
	 */
	fun setHidden(hidden: Boolean, persistent: Boolean = true) = inner.setHidden(hidden, persistent)

	/**
	 * Raw Pl3xMap [Player] instance.
	 */
	fun raw(): Player = inner

	override fun toString() = "MapPlayer(uuid=$uuid, name=$name)"
	override fun equals(other: Any?) = other is MapPlayer && uuid == other.uuid
	override fun hashCode() = uuid.hashCode()
}
