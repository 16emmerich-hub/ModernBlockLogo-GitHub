package com.ae12357.modernblocklogo.mixin;

import com.ae12357.modernblocklogo.LogoAnimationManager;

import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(
            method = "disconnect",
            at = @At("HEAD")
    )
    private void modernBlockLogo$onDisconnect(
            CallbackInfo ci
    ) {
        LogoAnimationManager.reset();
    }
}