package com.odtheking.mixin.mixins;

import com.odtheking.odin.OdinMod;
import com.odtheking.odin.utils.ui.rendering.NVGPIPRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void renderSkijaOverlay(DeltaTracker deltaTracker, boolean tick, CallbackInfo ci) {
        var window = OdinMod.getMc().getWindow();
        NVGPIPRenderer.renderQueuedOverlay(window.getScreenWidth(), window.getScreenHeight());
    }
}
