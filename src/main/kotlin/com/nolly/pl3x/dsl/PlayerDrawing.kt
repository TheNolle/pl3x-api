package com.nolly.pl3x.dsl

import com.nolly.pl3x.Pl3xAPI
import com.nolly.pl3x.marker.MarkerBuilder
import com.nolly.pl3x.player.MapPlayer

/**
 * Receiver scope used inside `player.drawOn {}` blocks.
 *
 * Automatically resolves the [player]'s current world and creates the specified
 * layer there. Includes `centerHere()` helper for positioning relative to the player.
 *
 * @param player The [MapPlayer] whose world this scope operates in
 * @param layerKey Layer key to create/use
 * @param layerLabel Layer label (defaults to [layerKey])
 */
class PlayerDrawingScope(private val player: MapPlayer, layerKey: String, layerLabel: String = layerKey) {
	private val world = Pl3xAPI.world(player.worldName)
	private val layer = world.layer(layerKey, layerLabel)

	fun circle(key: String, block: MarkerBuilder.() -> Unit = {}) =
		bound(key, MarkerBuilder.Type.CIRCLE, block)

	fun region(key: String, block: MarkerBuilder.() -> Unit = {}) =
		bound(key, MarkerBuilder.Type.REGION, block)

	fun rectangle(key: String, block: MarkerBuilder.() -> Unit = {}) =
		bound(key, MarkerBuilder.Type.RECTANGLE, block)

	fun icon(key: String, block: MarkerBuilder.() -> Unit = {}) =
		bound(key, MarkerBuilder.Type.ICON, block)

	/**
	 * Convenience extension on [MarkerBuilder] within [PlayerDrawingScope].
	 *
	 * Sets the marker's center to the current position of [PlayerDrawingScope.player].
	 */
	fun MarkerBuilder.centerHere(): MarkerBuilder {
		val pos = player.position
		return center(pos.x, pos.z)
	}

	private fun bound(
		key: String,
		type: MarkerBuilder.Type,
		block: MarkerBuilder.() -> Unit,
	) = MarkerBuilder(key, type)
		.world(world.name)
		.layer(layer.key, layer.label)
		.apply(block)
		.register()
}

/**
 * DSL entry point for drawing markers in the world of this player.
 *
 * Creates the specified layer in the player's current world, then runs the [block]
 * with a [PlayerDrawingScope] receiver.
 *
 * ```
 * player.drawOn("player_markers") {
 *   circle("position") { centerHere(); radius(8.0) }
 * }
 * ```
 *
 * @param layerKey Layer key to create/use
 * @param layerLabel Layer label (defaults to [layerKey])
 * @param block DSL block that creates markers
 */
fun MapPlayer.drawOn(layerKey: String, layerLabel: String = layerKey, block: PlayerDrawingScope.() -> Unit) {
	PlayerDrawingScope(this, layerKey, layerLabel).block()
}
