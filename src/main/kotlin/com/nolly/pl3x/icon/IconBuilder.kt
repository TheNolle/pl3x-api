package com.nolly.pl3x.icon

import com.nolly.pl3x.Pl3xContext
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Fluent builder for registering custom icons with Pl3xMap.
 *
 * Supports loading from [BufferedImage], file path, or [File]. Registers to
 * Pl3xContext.icons for use in [MarkerBuilder.image()].
 *
 * ```
 * IconBuilder("my_icon")
 *   .image(myImage)
 *   .format("png")
 *   .overwrite()
 *   .register()
 * ```
 */
class IconBuilder(private val key: String) {
	private var image: BufferedImage? = null
	private var format: String = "png"
	private var overwrite: Boolean = false

	/**
	 * Sets the icon image from a [BufferedImage].
	 *
	 * @param img The image to register
	 * @return `this` for chaining
	 */
	fun image(img: BufferedImage): IconBuilder {
		image = img
		return this
	}

	/**
	 * Loads icon image from filesystem path.
	 *
	 * @param path Path to image file (PNG, JPG, GIF, etc.)
	 * @return `this` for chaining
	 */
	fun image(path: String): IconBuilder {
		image = ImageIO.read(File(path))
		return this
	}

	/**
	 * Loads icon image from [File].
	 *
	 * @param file Image file to load
	 * @return `this` for chaining
	 */
	fun image(file: File): IconBuilder {
		image = ImageIO.read(file)
		return this
	}

	/**
	 * Sets the image format for registration (png, jpg, gif, webp).
	 *
	 * Defaults to "png".
	 *
	 * @param fmt File format string
	 * @return `this` for chaining
	 */
	fun format(fmt: String): IconBuilder {
		format = fmt
		return this
	}

	/**
	 * Allows overwriting an existing icon with the same [key].
	 *
	 * @return `this` for chaining
	 */
	fun overwrite(): IconBuilder {
		overwrite = true
		return this
	}

	/**
	 * Registers this icon with Pl3xContext.icons.
	 *
	 * Validates image is set. Use in [MarkerBuilder.image("my_icon")].
	 */
	fun register() {
		val img = requireNotNull(image) { "IconBuilder: call .image(...) before .register()" }
		Pl3xContext.icons.register(key, img, format, overwrite)
	}
}
