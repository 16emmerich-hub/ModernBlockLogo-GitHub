package com.ae12357.modernblocklogo;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import java.util.Random;

public final class LogoAnimation {

    private static final Identifier MINECRAFT_LOGO =
            Identifier.withDefaultNamespace(
                    "textures/gui/title/minecraft.png"
            );

    private static final Identifier MINECRAFT_EDITION =
            Identifier.withDefaultNamespace(
                    "textures/gui/title/edition.png"
            );

    private static final int LOGO_WIDTH = 256;
    private static final int LOGO_HEIGHT = 44;

    private static final int LOGO_TEXTURE_WIDTH = 256;
    private static final int LOGO_TEXTURE_HEIGHT = 64;

    private static final int EDITION_WIDTH = 128;
    private static final int EDITION_HEIGHT = 14;

    private static final int EDITION_TEXTURE_WIDTH = 128;
    private static final int EDITION_TEXTURE_HEIGHT = 16;

    private static final int DEFAULT_HEIGHT_OFFSET = 30;

    private static final int BASE_LOGO_PIECE_WIDTH = 8;
    private static final int BASE_LOGO_PIECE_HEIGHT = 4;

    private static final int BASE_EDITION_PIECE_WIDTH = 8;
    private static final int BASE_EDITION_PIECE_HEIGHT = 2;

    private static final int LOGO_COLS = 32;
    private static final int LOGO_ROWS = 11;

    private static final int EDITION_COLS = 16;
    private static final int EDITION_ROWS = 7;

    private static final long BUILD_TIME = 2300L;
    private static final long MAX_DELAY = 900L;

    private final long startTime;

    private final LogoPiece[] logoPieces;
    private final EditionPiece[] editionPieces;

    public LogoAnimation() {

        startTime = System.currentTimeMillis();

        Random random = new Random(0xAE12357L);

        logoPieces =
                new LogoPiece[
                        LOGO_COLS * LOGO_ROWS
                ];

        editionPieces =
                new EditionPiece[
                        EDITION_COLS * EDITION_ROWS
                ];

        int index = 0;

        for (int row = 0; row < LOGO_ROWS; row++) {

            for (int column = 0;
                 column < LOGO_COLS;
                 column++) {

                float angle =
                        random.nextFloat()
                                * (float) (Math.PI * 2.0);

                float distance =
                        250.0f
                                + random.nextFloat() * 700.0f;

                float startX =
                        (float) Math.cos(angle) * distance;

                float startY =
                        (float) Math.sin(angle) * distance;

                long delay =
                        (long)
                                (
                                        random.nextFloat()
                                                * MAX_DELAY
                                );

                logoPieces[index++] =
                        new LogoPiece(
                                column,
                                row,
                                startX,
                                startY,
                                delay
                        );
            }
        }

        index = 0;

        for (int row = 0; row < EDITION_ROWS; row++) {

            for (int column = 0;
                 column < EDITION_COLS;
                 column++) {

                float angle =
                        random.nextFloat()
                                * (float) (Math.PI * 2.0);

                float distance =
                        180.0f
                                + random.nextFloat() * 500.0f;

                float startX =
                        (float) Math.cos(angle) * distance;

                float startY =
                        (float) Math.sin(angle) * distance;

                long delay =
                        450L
                                + (long)
                                        (
                                                random.nextFloat()
                                                        * 700.0f
                                        );

                editionPieces[index++] =
                        new EditionPiece(
                                column,
                                row,
                                startX,
                                startY,
                                delay
                        );
            }
        }
    }

    public void render(
            GuiGraphicsExtractor graphics,
            int screenWidth,
            int screenHeight
    ) {

        LogoConfig config = LogoConfig.get();

        if (!config.animationEnabled) {

            renderCompleteLogo(
                    graphics,
                    screenWidth,
                    screenHeight
            );

            return;
        }

        double speed = config.animationSpeed;

        long elapsed =
                (long)
                        (
                                (System.currentTimeMillis()
                                        - startTime)
                                        * speed
                        );

        int chunkSize = config.chunkSize;

        int logoX =
                screenWidth / 2 - 128;

        int logoY =
                DEFAULT_HEIGHT_OFFSET;

        /*
         * The original animation uses 8x4 pixel pieces.
         *
         * chunkSize determines how many neighboring
         * pieces move together.
         */

        for (LogoPiece piece : logoPieces) {

            /*
             * Only render the top-left piece of each
             * chunk. The chunk's texture area contains
             * all of the pixels belonging to that chunk.
             */

            if ((piece.column % chunkSize) != 0
                    || (piece.row % chunkSize) != 0) {
                continue;
            }

            int pieceWidth =
                    Math.min(
                            chunkSize,
                            LOGO_COLS - piece.column
                    );

            int pieceHeight =
                    Math.min(
                            chunkSize,
                            LOGO_ROWS - piece.row
                    );

            int sourceX =
                    piece.column
                            * BASE_LOGO_PIECE_WIDTH;

            int sourceY =
                    piece.row
                            * BASE_LOGO_PIECE_HEIGHT;

            int width =
                    pieceWidth
                            * BASE_LOGO_PIECE_WIDTH;

            int height =
                    pieceHeight
                            * BASE_LOGO_PIECE_HEIGHT;

            float progress =
                    (elapsed - piece.delay)
                            / (float) BUILD_TIME;

            progress =
                    clamp(progress);

            float eased =
                    easeOutCubic(progress);

            int targetX =
                    logoX + sourceX;

            int targetY =
                    logoY + sourceY;

            int x =
                    Math.round(
                            targetX
                                    + piece.startX
                                    * (1.0f - eased)
                    );

            int y =
                    Math.round(
                            targetY
                                    + piece.startY
                                    * (1.0f - eased)
                    );

            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    MINECRAFT_LOGO,
                    x,
                    y,
                    sourceX,
                    sourceY,
                    width,
                    height,
                    width,
                    height,
                    LOGO_TEXTURE_WIDTH,
                    LOGO_TEXTURE_HEIGHT
            );
        }

        int editionX =
                screenWidth / 2 - 64;

        int editionY =
                DEFAULT_HEIGHT_OFFSET + 37;

        for (EditionPiece piece : editionPieces) {

            if ((piece.column % chunkSize) != 0
                    || (piece.row % chunkSize) != 0) {
                continue;
            }

            int pieceWidth =
                    Math.min(
                            chunkSize,
                            EDITION_COLS - piece.column
                    );

            int pieceHeight =
                    Math.min(
                            chunkSize,
                            EDITION_ROWS - piece.row
                    );

            int sourceX =
                    piece.column
                            * BASE_EDITION_PIECE_WIDTH;

            int sourceY =
                    piece.row
                            * BASE_EDITION_PIECE_HEIGHT;

            int width =
                    pieceWidth
                            * BASE_EDITION_PIECE_WIDTH;

            int height =
                    pieceHeight
                            * BASE_EDITION_PIECE_HEIGHT;

            float progress =
                    (elapsed - piece.delay)
                            / (float) BUILD_TIME;

            progress =
                    clamp(progress);

            float eased =
                    easeOutCubic(progress);

            int targetX =
                    editionX + sourceX;

            int targetY =
                    editionY + sourceY;

            int x =
                    Math.round(
                            targetX
                                    + piece.startX
                                    * (1.0f - eased)
                    );

            int y =
                    Math.round(
                            targetY
                                    + piece.startY
                                    * (1.0f - eased)
                    );

            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    MINECRAFT_EDITION,
                    x,
                    y,
                    sourceX,
                    sourceY,
                    width,
                    height,
                    width,
                    height,
                    EDITION_TEXTURE_WIDTH,
                    EDITION_TEXTURE_HEIGHT
            );
        }
    }

    private void renderCompleteLogo(
            GuiGraphicsExtractor graphics,
            int screenWidth,
            int screenHeight
    ) {

        int logoX =
                screenWidth / 2 - 128;

        int logoY =
                DEFAULT_HEIGHT_OFFSET;

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                MINECRAFT_LOGO,
                logoX,
                logoY,
                0,
                0,
                LOGO_WIDTH,
                LOGO_HEIGHT,
                LOGO_WIDTH,
                LOGO_HEIGHT,
                LOGO_TEXTURE_WIDTH,
                LOGO_TEXTURE_HEIGHT
        );

        int editionX =
                screenWidth / 2 - 64;

        int editionY =
                DEFAULT_HEIGHT_OFFSET + 37;

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                MINECRAFT_EDITION,
                editionX,
                editionY,
                0,
                0,
                EDITION_WIDTH,
                EDITION_HEIGHT,
                EDITION_WIDTH,
                EDITION_HEIGHT,
                EDITION_TEXTURE_WIDTH,
                EDITION_TEXTURE_HEIGHT
        );
    }

    private static float clamp(float value) {

        if (value < 0.0f) {
            return 0.0f;
        }

        if (value > 1.0f) {
            return 1.0f;
        }

        return value;
    }

    private static float easeOutCubic(float value) {

        float inverse =
                1.0f - value;

        return 1.0f
                - inverse
                * inverse
                * inverse;
    }

    private static final class LogoPiece {

        final int column;
        final int row;

        final float startX;
        final float startY;

        final long delay;

        LogoPiece(
                int column,
                int row,
                float startX,
                float startY,
                long delay
        ) {

            this.column = column;
            this.row = row;

            this.startX = startX;
            this.startY = startY;

            this.delay = delay;
        }
    }

    private static final class EditionPiece {

        final int column;
        final int row;

        final float startX;
        final float startY;

        final long delay;

        EditionPiece(
                int column,
                int row,
                float startX,
                float startY,
                long delay
        ) {

            this.column = column;
            this.row = row;

            this.startX = startX;
            this.startY = startY;

            this.delay = delay;
        }
    }
}