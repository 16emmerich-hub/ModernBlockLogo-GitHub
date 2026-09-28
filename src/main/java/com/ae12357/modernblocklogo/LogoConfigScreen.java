package com.ae12357.modernblocklogo;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class LogoConfigScreen extends Screen {

    private final Screen parent;
    private final LogoConfig config;

    private Button animationButton;
    private Button replayButton;

    private SpeedSlider speedSlider;
    private ChunkSlider chunkSlider;

    public LogoConfigScreen(Screen parent) {
        super(Component.literal("Modern Block Logo"));
        this.parent = parent;

        LogoConfig original = LogoConfig.get();

        config = new LogoConfig();
        config.animationEnabled = original.animationEnabled;
        config.animationSpeed = original.animationSpeed;
        config.chunkSize = original.chunkSize;
        config.replayMode = original.replayMode;
    }

    @Override
    protected void init() {

        int center = this.width / 2;

        this.addRenderableWidget(
                new StringWidget(
                        center - 100,
                        25,
                        200,
                        20,
                        Component.literal("Modern Block Logo"),
                        this.font
                )
        );

        animationButton =
                Button.builder(
                        animationText(),
                        button -> {
                            config.animationEnabled =
                                    !config.animationEnabled;

                            updateDisabledState();
                            animationButton.setMessage(
                                    animationText()
                            );
                        }
                )
                .bounds(center - 100, 60, 200, 20)
                .build();

        this.addRenderableWidget(animationButton);

        speedSlider =
                new SpeedSlider(
                        center - 100,
                        100,
                        200,
                        20,
                        config.animationSpeed
                );

        this.addRenderableWidget(speedSlider);

        chunkSlider =
                new ChunkSlider(
                        center - 100,
                        145,
                        200,
                        20,
                        config.chunkSize
                );

        this.addRenderableWidget(chunkSlider);

        replayButton =
                Button.builder(
                        replayText(),
                        button -> cycleReplay()
                )
                .bounds(center - 100, 190, 200, 20)
                .build();

        this.addRenderableWidget(replayButton);

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Done"),
                        button -> saveAndClose()
                )
                .bounds(center - 100, 230, 95, 20)
                .build()
        );

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Cancel"),
                        button -> {
                            this.minecraft.setScreen(parent);
                        }
                )
                .bounds(center + 5, 230, 95, 20)
                .build()
        );

        updateDisabledState();
    }

    private Component animationText() {
        return Component.literal(
                "Animation: "
                        + (config.animationEnabled
                        ? "ON"
                        : "OFF")
        );
    }

    private Component replayText() {
        return Component.literal(
                "Replay: "
                        + replayName(config.replayMode)
        );
    }

    private String replayName(LogoConfig.ReplayMode mode) {
        return switch (mode) {
            case NEVER -> "Never";
            case AFTER_WORLD -> "After leaving world";
            case AFTER_SERVER -> "After leaving server";
            case ON_JOIN -> "On join";
            case ALL -> "All";
        };
    }

    private void cycleReplay() {

        LogoConfig.ReplayMode[] values =
                LogoConfig.ReplayMode.values();

        int next =
                (config.replayMode.ordinal() + 1)
                        % values.length;

        config.replayMode = values[next];

        replayButton.setMessage(
                replayText()
        );
    }

    private void updateDisabledState() {
        replayButton.active =
                config.animationEnabled;

        speedSlider.active =
                config.animationEnabled;

        chunkSlider.active =
                config.animationEnabled;
    }

    private void saveAndClose() {

        config.animationSpeed =
                speedSlider.getSpeed();

        config.chunkSize =
                chunkSlider.getChunkSize();

        LogoConfig saved = LogoConfig.get();

        saved.animationEnabled =
                config.animationEnabled;

        saved.animationSpeed =
                config.animationSpeed;

        saved.chunkSize =
                config.chunkSize;

        saved.replayMode =
                config.replayMode;

        LogoConfig.save();

        this.minecraft.setScreen(parent);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }

    private class SpeedSlider
            extends AbstractSliderButton {

        SpeedSlider(
                int x,
                int y,
                int width,
                int height,
                double value
        ) {
            super(
                    x,
                    y,
                    width,
                    height,
                    Component.empty(),
                    (value - 0.25) / 3.75
            );

            updateMessage();
        }

        double getSpeed() {
            return 0.25 + value * 3.75;
        }

        @Override
        protected void updateMessage() {
            setMessage(
                    Component.literal(
                            String.format(
                                    "Animation Speed: %.2fx",
                                    getSpeed()
                            )
                    )
            );
        }

        @Override
        protected void applyValue() {
            updateMessage();
        }
    }

    private class ChunkSlider
            extends AbstractSliderButton {

        ChunkSlider(
                int x,
                int y,
                int width,
                int height,
                int chunkSize
        ) {
            super(
                    x,
                    y,
                    width,
                    height,
                    Component.empty(),
                    (chunkSize - 1) / 7.0
            );

            updateMessage();
        }

        int getChunkSize() {
            return 1 + (int) Math.round(value * 7.0);
        }

        @Override
        protected void updateMessage() {
            setMessage(
                    Component.literal(
                            "Block Chunk Size: "
                                    + getChunkSize()
                    )
            );
        }

        @Override
        protected void applyValue() {
            updateMessage();
        }
    }
}