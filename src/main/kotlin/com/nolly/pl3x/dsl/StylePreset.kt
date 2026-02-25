package com.nolly.pl3x.dsl

import com.nolly.pl3x.marker.MarkerBuilder
import com.nolly.pl3x.marker.StyleDSL
import net.pl3x.map.core.markers.option.Options

/**
 * A reusable configuration block for marker [StyleDSL] properties.
 *
 * Create once, apply to multiple markers:
 *
 * ```
 * val highlightPreset = StylePreset {
 *   stroke("#ffff00", 4)
 *   fill("#ffaa00")
 * }
 *
 * marker1.applyPreset(highlightPreset)
 * marker2.applyPreset(highlightPreset)
 * ```
 */
class StylePreset(private val block: StyleDSL.() -> Unit) {
	fun build(): Options = StyleDSL().apply(block).build()
}

/**
 * Applies a [StylePreset] configuration to this marker's options and returns `this`.
 *
 * @param preset The style preset to apply
 * @return `this` marker for fluent chaining
 */
fun MarkerBuilder.applyPreset(preset: StylePreset): MarkerBuilder = options(preset.build())
