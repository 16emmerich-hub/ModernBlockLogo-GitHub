package com.ae12357.modernblocklogo;

public final class LogoAnimationManager {

    private static LogoAnimation animation;

    private LogoAnimationManager() {
    }

    public static LogoAnimation getAnimation() {
        if (animation == null) {
            animation = new LogoAnimation();
        }

        return animation;
    }

    public static void reset() {
        animation = new LogoAnimation();
    }
}