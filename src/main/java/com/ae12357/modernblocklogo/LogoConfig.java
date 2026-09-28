package com.ae12357.modernblocklogo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class LogoConfig {

    private static final Gson GSON =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

    private static final Path FILE =
            Path.of(
                    System.getProperty("user.home"),
                    "AppData",
                    "Roaming",
                    ".minecraft",
                    "config",
                    "modern-block-logo.json"
            );

    public boolean animationEnabled = true;

    public double animationSpeed = 1.0;

    public int chunkSize = 1;

    public ReplayMode replayMode = ReplayMode.ALL;

    public enum ReplayMode {
        NEVER,
        AFTER_WORLD,
        AFTER_SERVER,
        ON_JOIN,
        ALL
    }

    private static LogoConfig INSTANCE;

    public static LogoConfig get() {
        if (INSTANCE == null) {
            load();
        }

        return INSTANCE;
    }

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                INSTANCE =
                        GSON.fromJson(
                                Files.readString(FILE),
                                LogoConfig.class
                        );
            }
        } catch (Exception ignored) {
        }

        if (INSTANCE == null) {
            INSTANCE = new LogoConfig();
        }

        INSTANCE.clamp();
        save();
    }

    public static void save() {
        if (INSTANCE == null) {
            return;
        }

        INSTANCE.clamp();

        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(
                    FILE,
                    GSON.toJson(INSTANCE)
            );
        } catch (IOException ignored) {
        }
    }

    private void clamp() {
        if (animationSpeed < 0.25) {
            animationSpeed = 0.25;
        }

        if (animationSpeed > 4.0) {
            animationSpeed = 4.0;
        }

        if (chunkSize < 1) {
            chunkSize = 1;
        }

        if (chunkSize > 8) {
            chunkSize = 8;
        }

        if (replayMode == null) {
            replayMode = ReplayMode.ALL;
        }
    }
}