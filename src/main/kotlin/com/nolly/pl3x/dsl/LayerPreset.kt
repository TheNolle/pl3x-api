package com.nolly.pl3x.dsl

import com.nolly.pl3x.layer.MapLayer

/**
 * A reusable configuration block for [MapLayer] properties.
 *
 * Create once, apply to multiple layers:
 *
 * ```
 * val overlayPreset = LayerPreset {
 *   updateInterval = 30
 *   defaultHidden = true
 * }
 *
 * world.layer('layer1').applyPreset(overlayPreset)
 * world.layer('layer2').applyPreset(overlayPreset)
 * ```
 */
class LayerPreset(private val block: MapLayer.() -> Unit) {
	fun applyTo(layer: MapLayer) {
		layer.block()
	}
}

/**
 * Applies a [LayerPreset] configuration to this layer and returns `this`.
 *
 * @param preset The preset configuration to apply
 * @return `this` layer for fluent chaining
 */
fun MapLayer.applyPreset(preset: LayerPreset): MapLayer {
	preset.applyTo(this)
	return this
}
