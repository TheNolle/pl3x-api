package com.nolly.pl3x.dsl

import com.nolly.pl3x.Pl3xAPI
import com.nolly.pl3x.world.MapWorld

/**
 * Applies the [block] to every available [MapWorld].
 *
 * Iterates over all worlds registered in Pl3xContext.
 *
 * @param block DSL block applied to each [MapWorld]
 */
fun forEachWorld(block: MapWorld.() -> Unit) {
	Pl3xAPI.worlds().forEach { it.block() }
}

/**
 * Applies the [block] to [MapWorld]s matching the [predicate].
 *
 * Filters worlds by name before applying the block.
 *
 * @param predicate Filter function: true to include the world
 * @param block DSL block applied to matching [MapWorld]s
 */
fun forWorlds(predicate: (String) -> Boolean, block: MapWorld.() -> Unit) {
	Pl3xAPI.worlds().filter { predicate(it.name) }.forEach { it.block() }
}

/**
 * Applies the [block] to [MapWorld]s with the given [names].
 *
 * Exact name matching (case-sensitive).
 *
 * @param names World names to match
 * @param block DSL block applied to matching [MapWorld]s
 */
fun forWorlds(vararg names: String, block: MapWorld.() -> Unit) {
	val set = names.toSet()
	Pl3xAPI.worlds().filter { it.name in set }.forEach { it.block() }
}
