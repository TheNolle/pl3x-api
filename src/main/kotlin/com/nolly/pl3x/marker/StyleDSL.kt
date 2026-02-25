package com.nolly.pl3x.marker

import net.pl3x.map.core.markers.Point
import net.pl3x.map.core.markers.option.Fill
import net.pl3x.map.core.markers.option.Options
import net.pl3x.map.core.markers.option.Stroke
import net.pl3x.map.core.markers.option.Tooltip
import net.pl3x.map.core.util.Colors

/**
 * Fluent builder for Leaflet marker [Options].
 *
 * Used via `MarkerBuilder.style {}` or standalone `style()`.
 */
class StyleDSL {
	/**
	 * Converts existing [Options] to mutable StyleDSL.
	 */
	companion object {
		fun from(opts: Options): StyleDSL {
			val dsl = StyleDSL()
			dsl.builder = opts.asBuilder()
			return dsl
		}
	}

	internal var builder = Options.builder()

	/**
	 * Sets stroke color/weight shorthand.
	 */
	fun stroke(hex: String, weight: Int = 3): StyleDSL {
		builder.strokeColor(Colors.fromHex(hex)).strokeWeight(weight)
		return this
	}

	/**
	 * Toggles stroke visibility.
	 */
	fun strokeEnabled(enabled: Boolean): StyleDSL {
		builder.stroke(enabled)
		return this
	}

	/**
	 * Sets CSS dasharray pattern ("5,10").
	 */
	fun strokeDashPattern(pattern: String): StyleDSL {
		builder.strokeDashPattern(pattern)
		return this
	}

	/**
	 * Sets CSS dashoffset.
	 */
	fun strokeDashOffset(offset: String): StyleDSL {
		builder.strokeDashOffset(offset)
		return this
	}

	/**
	 * Sets line cap shape (butt, round, square).
	 */
	fun strokeLineCap(shape: Stroke.LineCapShape): StyleDSL {
		builder.strokeLineCapShape(shape)
		return this
	}

	/**
	 * Sets line join shape (round, bevel, miter).
	 */
	fun strokeLineJoin(shape: Stroke.LineJoinShape): StyleDSL {
		builder.strokeLineJoinShape(shape)
		return this
	}

	/**
	 * Sets fill color/type shorthand.
	 */
	fun fill(hex: String, type: Fill.Type = Fill.Type.EVENODD): StyleDSL {
		builder.fillColor(Colors.fromHex(hex)).fillType(type)
		return this
	}

	/**
	 * Toggles fill visibility.
	 */
	fun fillEnabled(enabled: Boolean): StyleDSL {
		builder.fill(enabled)
		return this
	}

	/**
	 * Configures popup content + options.
	 */
	fun popup(
		content: String,
		pane: String? = null,
		offset: Point? = null,
		maxWidth: Int? = null,
		minWidth: Int? = null,
	): StyleDSL {
		builder.popupContent(content)
		pane?.let { builder.popupPane(it) }
		offset?.let { builder.popupOffset(it) }
		maxWidth?.let { builder.popupMaxWidth(it) }
		minWidth?.let { builder.popupMinWidth(it) }
		return this
	}

	/**
	 * Sets popup max-height in pixels.
	 */
	fun popupMaxHeight(px: Int): StyleDSL {
		builder.popupMaxHeight(px)
		return this
	}

	/**
	 * Auto-pan map to fit popup.
	 */
	fun popupAutoPan(enabled: Boolean): StyleDSL {
		builder.popupShouldAutoPan(enabled)
		return this
	}

	/**
	 * Auto-close popup when another opens.
	 */
	fun popupAutoClose(enabled: Boolean): StyleDSL {
		builder.popupShouldAutoClose(enabled)
		return this
	}

	/**
	 * Close popup when user clicks on map (not marker).
	 */
	fun popupCloseOnClick(enabled: Boolean): StyleDSL {
		builder.popupShouldCloseOnClick(enabled)
		return this
	}

	/**
	 * Close popup when user presses Escape key.
	 */
	fun popupCloseOnEscape(enabled: Boolean): StyleDSL {
		builder.popupShouldCloseOnEscapeKey(enabled)
		return this
	}

	/**
	 * Show close button in popup corner.
	 */
	fun popupCloseButton(enabled: Boolean): StyleDSL {
		builder.popupCloseButton(enabled)
		return this
	}

	/**
	 * Keep popup in view when map pans.
	 */
	fun popupKeepInView(enabled: Boolean): StyleDSL {
		builder.popupShouldKeepInView(enabled)
		return this
	}

	/**
	 * Configures tooltip with position/sticky options.
	 */
	fun tooltip(
		content: String,
		sticky: Boolean = false,
		direction: Tooltip.Direction = Tooltip.Direction.TOP,
		permanent: Boolean = false,
		opacity: Double = 1.0,
		pane: String? = null,
		offset: Point? = null,
	): StyleDSL {
		builder.tooltipContent(content).tooltipSticky(sticky).tooltipDirection(direction).tooltipPermanent(permanent)
			.tooltipOpacity(opacity)
		pane?.let { builder.tooltipPane(it) }
		offset?.let { builder.tooltipOffset(it) }
		return this
	}

	/**
	 * Builds final immutable [Options].
	 */
	fun build(): Options = builder.build()
}

/**
 * Standalone convenience for quick Options.
 */
fun style(block: StyleDSL.() -> Unit): Options = StyleDSL().apply(block).build()
