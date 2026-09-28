package com.ae12357.modernblocklogo;

import net.fabricmc.api.ClientModInitializer;

public class ModernBlockLogoClient implements ClientModInitializer {

    public static final String MOD_ID = "modern-block-logo";

    @Override
    public void onInitializeClient() {
        System.out.println(
                "[ModernBlockLogo] Exact Minecraft 26.1.2 logo animation loaded!"
        );
    }
}
