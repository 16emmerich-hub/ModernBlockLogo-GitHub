package com.ae12357.modernblocklogo.mixin;

import com.ae12357.modernblocklogo.LogoAnimation;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LoadingOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoadingOverlay.class)
public class LoadingOverlayMixin {

    @Unique
    private LogoAnimation modernBlockLogo$animation;

    @Inject(
            method = "extractRenderState",
            at = @At("TAIL")
    )
    private void modernBlockLogo$render(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float delta,
            CallbackInfo ci
    ) {

        if (modernBlockLogo$animation == null) {
            modernBlockLogo$animation =
                    new LogoAnimation();
        }

        modernBlockLogo$animation.render(
                graphics,
                graphics.guiWidth(),
                graphics.guiHeight()
        );
    }
}
