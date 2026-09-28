package com.ae12357.modernblocklogo.mixin;

import com.ae12357.modernblocklogo.LogoAnimationManager;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.TitleScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void modernBlockLogo$init(CallbackInfo ci) {
        LogoAnimationManager.reset();
    }

    @Inject(
            method = "extractRenderState",
            at = @At("HEAD")
    )
    private void modernBlockLogo$render(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float delta,
            CallbackInfo ci
    ) {
        LogoAnimationManager.getAnimation().render(
                graphics,
                graphics.guiWidth(),
                graphics.guiHeight()
        );
    }
}