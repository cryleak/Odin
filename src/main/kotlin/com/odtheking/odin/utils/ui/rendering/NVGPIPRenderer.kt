package com.odtheking.odin.utils.ui.rendering

import com.mojang.blaze3d.vertex.PoseStack
import com.odtheking.odin.OdinMod.mc
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState
import org.joml.Matrix3x2f
import java.util.ArrayDeque

class NVGPIPRenderer : PictureInPictureRenderer<NVGPIPRenderer.NVGRenderState>() {

    override fun renderToTexture(state: NVGRenderState, poseStack: PoseStack, submitNodeCollector: SubmitNodeCollector) = Unit
    override fun getRenderStateClass(): Class<NVGRenderState> = NVGRenderState::class.java
    override fun getTextureLabel(): String = "odin_skija_renderer"

    enum class CoordinateSpace {
        WINDOW,
        GUI
    }

    data class NVGRenderState(
        private val x: Int,
        private val y: Int,
        private val width: Int,
        private val height: Int,
        private val poseMatrix: Matrix3x2f,
        private val scissor: ScreenRectangle?,
        private val bounds: ScreenRectangle?,
        val coordinateSpace: CoordinateSpace,
        val renderContent: () -> Unit
    ) : PictureInPictureRenderState {

        override fun scale(): Float = 1f
        override fun x0(): Int = x
        override fun y0(): Int = y
        override fun x1(): Int = x + width
        override fun y1(): Int = y + height
        override fun pose(): Matrix3x2f = poseMatrix
        override fun scissorArea(): ScreenRectangle? = scissor
        override fun bounds(): ScreenRectangle? = bounds
    }

    companion object {
        private val pending = ArrayDeque<NVGRenderState>()

        fun draw(
            context: GuiGraphicsExtractor,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            coordinateSpace: CoordinateSpace = CoordinateSpace.WINDOW,
            renderContent: () -> Unit
        ) {
            val scissor = context.scissorStack.peek()
            val pose = Matrix3x2f(context.pose())
            val bounds = createBounds(x, y, x + width, y + height, pose, scissor)
            pending.addLast(NVGRenderState(x, y, width, height, pose, scissor, bounds, coordinateSpace, renderContent))
            SkijaRenderer.invalidateOverlay()
        }

        @JvmStatic
        fun renderQueuedOverlay(screenWidth: Int, screenHeight: Int): Boolean {
            if (pending.isEmpty()) {
                SkijaRenderer.invalidateOverlay()
                return false
            }

            if (!SkijaRenderer.beginOverlayFrame(screenWidth.toFloat(), screenHeight.toFloat())) return false
            try {
                while (pending.isNotEmpty()) {
                    val state = pending.removeFirst()
                    NVGRenderer.push()
                    val scissor = state.scissorArea()
                    val scale = if (state.coordinateSpace == CoordinateSpace.GUI) mc.window.guiScale.toFloat() else 1f
                    try {
                        if (scissor != null) {
                            NVGRenderer.pushScissor(
                                scissor.left().toFloat() * scale,
                                scissor.top().toFloat() * scale,
                                scissor.width().toFloat() * scale,
                                scissor.height().toFloat() * scale
                            )
                        }
                        if (scale != 1f) {
                            NVGRenderer.scale(scale, scale)
                        }
                        SkijaRenderer.concat(state.pose())
                        state.renderContent()
                    } finally {
                        if (scissor != null) {
                            NVGRenderer.popScissor()
                        }
                        NVGRenderer.pop()
                    }
                }
            } finally {
                SkijaRenderer.endOverlayFrame()
            }
            return SkijaRenderer.compositeOverlay()
        }

        private fun createBounds(x0: Int, y0: Int, x1: Int, y1: Int, pose: Matrix3x2f, scissorArea: ScreenRectangle?): ScreenRectangle? {
            val screenRect = ScreenRectangle(x0, y0, x1 - x0, y1 - y0).transformMaxBounds(pose)
            return if (scissorArea != null) scissorArea.intersection(screenRect) else screenRect
        }
    }
}
