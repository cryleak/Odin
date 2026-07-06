package com.odtheking.odin.utils.ui.rendering

import com.odtheking.odin.OdinMod.mc

object NVGRenderer {
    val defaultFont: Font
        get() = SkijaRenderer.defaultFont

    fun devicePixelRatio(): Float =
        if (mc.window.screenWidth == 0) 1f else (mc.window.width / mc.window.screenWidth).toFloat()

    fun beginFrame(width: Float, height: Float) = SkijaRenderer.beginFrame(width, height)
    fun endFrame() = SkijaRenderer.endFrame()
    fun cleanup() = SkijaRenderer.cleanup()

    fun push() = SkijaRenderer.push()
    fun pop() = SkijaRenderer.pop()
    fun scale(x: Float, y: Float) = SkijaRenderer.scale(x, y)
    fun translate(x: Float, y: Float) = SkijaRenderer.translate(x, y)
    fun rotate(amount: Float) = SkijaRenderer.rotate(amount)
    fun globalAlpha(amount: Float) = SkijaRenderer.globalAlpha(amount)
    fun pushScissor(x: Float, y: Float, w: Float, h: Float) = SkijaRenderer.pushScissor(x, y, w, h)
    fun popScissor() = SkijaRenderer.popScissor()

    fun line(x1: Float, y1: Float, x2: Float, y2: Float, thickness: Float, color: Int) =
        SkijaRenderer.line(x1, y1, x2, y2, thickness, color)

    fun drawHalfRoundedRect(x: Float, y: Float, w: Float, h: Float, color: Int, radius: Float, roundTop: Boolean) =
        SkijaRenderer.drawHalfRoundedRect(x, y, w, h, color, radius, roundTop)

    fun rect(x: Float, y: Float, w: Float, h: Float, color: Int, radius: Float) =
        SkijaRenderer.rect(x, y, w, h, color, radius)

    fun rect(x: Float, y: Float, w: Float, h: Float, color: Int) =
        SkijaRenderer.rect(x, y, w, h, color)

    fun hollowRect(x: Float, y: Float, w: Float, h: Float, thickness: Float, color: Int, radius: Float) =
        SkijaRenderer.hollowRect(x, y, w, h, thickness, color, radius)

    fun gradientRect(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        color1: Int,
        color2: Int,
        gradient: Gradient,
        radius: Float
    ) = SkijaRenderer.gradientRect(x, y, w, h, color1, color2, gradient, radius)

    fun dropShadow(x: Float, y: Float, width: Float, height: Float, blur: Float, spread: Float, radius: Float) =
        SkijaRenderer.dropShadow(x, y, width, height, blur, spread, radius)

    fun circle(x: Float, y: Float, radius: Float, color: Int) =
        SkijaRenderer.circle(x, y, radius, color)

    fun text(text: String, x: Float, y: Float, size: Float, color: Int, font: Font = defaultFont) =
        SkijaRenderer.text(text, x, y, size, color, font)

    fun textShadow(text: String, x: Float, y: Float, size: Float, color: Int, font: Font = defaultFont) =
        SkijaRenderer.textShadow(text, x, y, size, color, font)

    fun textWidth(text: String, size: Float, font: Font = defaultFont): Float =
        SkijaRenderer.textWidth(text, size, font)

    fun drawWrappedString(
        text: String,
        x: Float,
        y: Float,
        w: Float,
        size: Float,
        color: Int,
        font: Font = defaultFont,
        lineHeight: Float = 1f
    ) = SkijaRenderer.drawWrappedString(text, x, y, w, size, color, font, lineHeight)

    fun wrappedTextBounds(
        text: String,
        w: Float,
        size: Float,
        font: Font = defaultFont,
        lineHeight: Float = 1f
    ): FloatArray = SkijaRenderer.wrappedTextBounds(text, w, size, font, lineHeight) ?: FloatArray(4)

    fun image(image: Image, x: Float, y: Float, w: Float, h: Float, radius: Float) =
        SkijaRenderer.image(image, x, y, w, h, radius)

    fun image(image: Image, x: Float, y: Float, w: Float, h: Float) =
        SkijaRenderer.image(image, x, y, w, h)

    fun createImage(resourcePath: String): Image =
        SkijaRenderer.createImage(resourcePath) ?: throw IllegalStateException("Failed to load image: $resourcePath")

    fun deleteImage(image: Image) = SkijaRenderer.deleteImage(image)

    @Deprecated("Raw GL texture handles are not backend-neutral and are unsupported by the Skija renderer.")
    fun createNVGImage(textureId: Int, textureWidth: Int, textureHeight: Int): Int = -1

    @Deprecated("Raw GL texture handles are not backend-neutral and are unsupported by the Skija renderer.")
    fun image(image: Int, textureWidth: Int, textureHeight: Int, subX: Int, subY: Int, subW: Int, subH: Int, x: Float, y: Float, w: Float, h: Float, radius: Float) = Unit
}
