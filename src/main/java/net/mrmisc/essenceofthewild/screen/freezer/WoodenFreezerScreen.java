package net.mrmisc.essenceofthewild.screen.freezer;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.entity.custom.freezer.WoodenFreezerBlockEntity;
import net.mrmisc.essenceofthewild.menu.freezer.WoodenFreezerMenu;

public class WoodenFreezerScreen extends AbstractContainerScreen<WoodenFreezerMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/gui/wooden_freezer_gui.png");

    private static final int SLOT_INTERIOR = 0xFF8B8B8B;

    private static final int ICE_FILL = 0xFF42CFFF;
    private static final int ICE_MID = 0xFF72DBFF;
    private static final int ICE_DEEP = 0xFF1599CC;

    private static final int MILK_FILL = 0xFFF9F8F4;
    private static final int MILK_MID = 0xFFEEEAE2;
    private static final int MILK_DEEP = 0xFFDDD7CF;

    private static final int FREEZE_FILL = 0xFFDDF6FF;
    private static final int FREEZE_MID = 0xFFBEEBFA;
    private static final int FREEZE_DEEP = 0xFF7FD8F2;

    private static final int[][] TOP_TANK_MASK = {
            { 12, 82, 93 },
            { 13, 81, 94 },
            { 14, 81, 94 },
            { 15, 82, 93 }
    };

    private static final int[][] RIGHT_TANK_MASK = {
            { 29, 120, 131 },
            { 30, 122, 131 },
            { 31, 120, 131 },
            { 32, 122, 132 },
            { 33, 119, 132 },
            { 34, 122, 131 },
            { 35, 120, 131 },
            { 36, 120, 131 }
    };

    private static final int[][] CHAMBER_MASK = {
            { 33, 80, 95 },
            { 34, 80, 95 },
            { 35, 80, 95 },
            { 36, 80, 95 },
            { 37, 80, 95 },
            { 38, 80, 95 },
            { 39, 80, 95 },
            { 40, 80, 95 },
            { 41, 80, 95 },
            { 42, 80, 95 },
            { 43, 80, 95 },
            { 44, 80, 95 },
            { 45, 80, 95 },
            { 46, 80, 95 },
            { 47, 80, 95 },
            { 48, 80, 95 }
    };

    private static final int[][] ICE_PIPE = {
            { 34, 32 },
            { 49, 32 },
            { 49, 14 },
            { 81, 14 }
    };

    private static final int[][] MILK_PIPE = {
            { 142, 33 },
            { 133, 33 },
            { 133, 32 },
            { 121, 32 },
            { 121, 34 },
            { 110, 34 },
            { 110, 40 },
            { 97, 40 }
    };

    private static final int[][] LEFT_CONE_PIPE = {
            { 82, 50 },
            { 82, 56 },
            { 50, 56 }
    };

    private static final int[][] CENTER_CONE_PIPE = {
            { 87, 50 },
            { 87, 58 }
    };

    private static final int[][] RIGHT_CONE_PIPE = {
            { 92, 50 },
            { 92, 56 },
            { 126, 56 }
    };

    private static final int[][] LEFT_DRIP_PATH = {
            { 84, 17 },
            { 84, 18 },
            { 84, 19 },
            { 84, 20 },
            { 84, 21 },
            { 84, 22 },
            { 84, 23 }
    };

    private static final int[][] CENTER_DRIP_PATH = {
            { 88, 17 },
            { 88, 18 },
            { 88, 19 },
            { 88, 20 },
            { 88, 25 },
            { 88, 26 }
    };

    private static final int[][] RIGHT_DRIP_PATH = {
            { 92, 17 },
            { 92, 18 },
            { 92, 19 },
            { 92, 20 },
            { 92, 21 },
            { 92, 22 },
            { 92, 23 },
            { 92, 24 },
            { 92, 25 }
    };

    // Hides the cone outlines cuz for some reason the texture has them built-in
    private static final int[][] CONE_OUTLINE_MASK = {
            { 2, 9, 13 },
            { 3, 8, 8 },
            { 3, 14, 14 },
            { 4, 6, 7 },
            { 4, 15, 15 },
            { 5, 5, 5 },
            { 5, 8, 8 },
            { 5, 15, 15 },
            { 6, 5, 6 },
            { 6, 8, 8 },
            { 6, 15, 15 },
            { 7, 5, 5 },
            { 7, 7, 7 },
            { 7, 9, 9 },
            { 7, 15, 15 },
            { 8, 4, 4 },
            { 8, 7, 7 },
            { 8, 10, 10 },
            { 8, 15, 15 },
            { 9, 4, 4 },
            { 9, 8, 8 },
            { 9, 11, 12 },
            { 9, 14, 14 },
            { 10, 3, 3 },
            { 10, 9, 10 },
            { 10, 13, 13 },
            { 11, 3, 3 },
            { 11, 11, 11 },
            { 11, 13, 13 },
            { 12, 2, 2 },
            { 12, 10, 12 },
            { 13, 2, 2 },
            { 13, 8, 9 },
            { 14, 1, 1 },
            { 14, 6, 7 },
            { 15, 1, 1 },
            { 15, 4, 5 },
            { 16, 1, 3 }
    };

    private float displayedIceLevel = 0.0F;
    private float displayedMilkLevel = 0.0F;
    private float displayedFreezeFill = 0.0F;

    public WoodenFreezerScreen(WoodenFreezerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.imageWidth = 176;
        this.imageHeight = 166;

        this.titleLabelX = 0;
        this.titleLabelY = 0;
        this.inventoryLabelX = 0;
        this.inventoryLabelY = 0;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        hideFilledConeOutlines(guiGraphics, x, y);
        renderFreezerAnimations(guiGraphics, x, y, partialTick);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Intentionally empty Dont Touch !!
    }

    private void hideFilledConeOutlines(GuiGraphics guiGraphics, int guiX, int guiY) {
        if (menu.hasConeInMenuSlot(WoodenFreezerMenu.MENU_CONE_SLOT_0)) {
            coverConeOutlineOnly(guiGraphics, guiX, guiY, 33, 47);
        }

        if (menu.hasConeInMenuSlot(WoodenFreezerMenu.MENU_CONE_SLOT_1)) {
            coverConeOutlineOnly(guiGraphics, guiX, guiY, 79, 60);
        }

        if (menu.hasConeInMenuSlot(WoodenFreezerMenu.MENU_CONE_SLOT_2)) {
            coverConeOutlineOnly(guiGraphics, guiX, guiY, 125, 47);
        }
    }

    private void coverConeOutlineOnly(GuiGraphics guiGraphics, int guiX, int guiY, int slotX, int slotY) {
        for (int[] span : CONE_OUTLINE_MASK) {
            int y = slotY + span[0];
            int x1 = slotX + span[1];
            int x2 = slotX + span[2];

            guiGraphics.fill(
                    guiX + x1,
                    guiY + y,
                    guiX + x2 + 1,
                    guiY + y + 1,
                    SLOT_INTERIOR
            );
        }
    }

    private void renderFreezerAnimations(GuiGraphics guiGraphics, int guiX, int guiY, float partialTick) {
        float time = partialTick;

        if (minecraft != null && minecraft.level != null) {
            time += minecraft.level.getGameTime();
        }

        int progress = menu.getProgress();
        int maxProgress = menu.getMaxProgress();
        boolean recipeActive = maxProgress > 0 && progress > 0;

        float targetIceLevel = Mth.clamp(menu.getIceLevel() / (float) WoodenFreezerBlockEntity.MAX_ICE_LEVEL, 0.0F, 1.0F);
        float targetMilkLevel = Mth.clamp(menu.getMilkLevel() / (float) WoodenFreezerBlockEntity.MAX_MILK_LEVEL, 0.0F, 1.0F);

        float targetFreezeFill = recipeActive
                ? Mth.clamp((progress + partialTick) / (float) maxProgress, 0.0F, 1.0F)
                : 0.0F;

        if (recipeActive) {
            displayedIceLevel = approach(displayedIceLevel, targetIceLevel, 0.03F);
            displayedMilkLevel = approach(displayedMilkLevel, targetMilkLevel, 0.03F);
            displayedFreezeFill = targetFreezeFill;
        } else {
            displayedIceLevel = targetIceLevel;
            displayedMilkLevel = targetMilkLevel;
            displayedFreezeFill = targetFreezeFill;
        }

        float smoothFreezeFill = smoothStep(displayedFreezeFill);

        if (displayedIceLevel > 0.0F) {
            drawMaskedFillVerticalSmooth(guiGraphics, guiX, guiY, TOP_TANK_MASK,
                    displayedIceLevel, ICE_DEEP, ICE_MID, ICE_FILL);
        }

        if (displayedMilkLevel > 0.0F) {
            drawMaskedFillVerticalSmooth(guiGraphics, guiX, guiY, RIGHT_TANK_MASK,
                    displayedMilkLevel, MILK_DEEP, MILK_MID, MILK_FILL);
        }

        if (smoothFreezeFill > 0.0F) {
            drawMaskedFillVerticalSmooth(guiGraphics, guiX, guiY, CHAMBER_MASK,
                    smoothFreezeFill, FREEZE_DEEP, FREEZE_MID, FREEZE_FILL);
        }

        if (displayedIceLevel > 0.0F) {
            if (recipeActive) {
                drawPipeAnimated(guiGraphics, guiX, guiY, ICE_PIPE, 1.0F, time, 1,
                        ICE_DEEP, ICE_FILL, 1.55F, 10);
            } else {
                drawPipeProgressSmooth(guiGraphics, guiX, guiY, ICE_PIPE, 1.0F, 1,
                        ICE_DEEP, ICE_MID, ICE_FILL);
            }
        }

        if (displayedMilkLevel > 0.0F) {
            if (recipeActive) {
                drawPipeAnimated(guiGraphics, guiX, guiY, MILK_PIPE, 1.0F, time + 8.0F, 1,
                        MILK_DEEP, MILK_FILL, 1.45F, 10);
            } else {
                drawPipeProgressSmooth(guiGraphics, guiX, guiY, MILK_PIPE, 1.0F, 1,
                        MILK_DEEP, MILK_MID, MILK_FILL);
            }
        }

        int craftableConeCount = menu.getCraftableConeCount();

        if (craftableConeCount > 0
                && menu.hasConeInputInMenuSlot(WoodenFreezerMenu.MENU_CONE_SLOT_0)
                && smoothFreezeFill > 0.0F) {
            drawPipeProgressSmooth(guiGraphics, guiX, guiY, LEFT_CONE_PIPE, smoothFreezeFill, 2,
                    FREEZE_DEEP, FREEZE_MID, FREEZE_FILL);
            craftableConeCount--;
        }

        if (craftableConeCount > 0
                && menu.hasConeInputInMenuSlot(WoodenFreezerMenu.MENU_CONE_SLOT_1)
                && smoothFreezeFill > 0.0F) {
            drawPipeProgressSmooth(guiGraphics, guiX, guiY, CENTER_CONE_PIPE, smoothFreezeFill, 2,
                    FREEZE_DEEP, FREEZE_MID, FREEZE_FILL);
            craftableConeCount--;
        }

        if (craftableConeCount > 0
                && menu.hasConeInputInMenuSlot(WoodenFreezerMenu.MENU_CONE_SLOT_2)
                && smoothFreezeFill > 0.0F) {
            drawPipeProgressSmooth(guiGraphics, guiX, guiY, RIGHT_CONE_PIPE, smoothFreezeFill, 2,
                    FREEZE_DEEP, FREEZE_MID, FREEZE_FILL);
        }

        if (recipeActive) {
            drawTextureLockedDrip(guiGraphics, guiX, guiY, time);
        }
    }

    private float approach(float current, float target, float step) {
        if (current < target) {
            return Math.min(target, current + step);
        }

        if (current > target) {
            return Math.max(target, current - step);
        }

        return current;
    }

    private float smoothStep(float value) {
        value = Mth.clamp(value, 0.0F, 1.0F);
        return value * value * (3.0F - 2.0F * value);
    }

    private void drawTextureLockedDrip(GuiGraphics guiGraphics, int guiX, int guiY, float time) {
        drawDripPath(guiGraphics, guiX, guiY, LEFT_DRIP_PATH, time, 0, FREEZE_MID, FREEZE_FILL);
        drawDripPath(guiGraphics, guiX, guiY, CENTER_DRIP_PATH, time, 7, MILK_MID, MILK_FILL);
        drawDripPath(guiGraphics, guiX, guiY, RIGHT_DRIP_PATH, time, 13, FREEZE_MID, FREEZE_FILL);
    }

    private void drawDripPath(GuiGraphics guiGraphics, int guiX, int guiY,
                              int[][] path, float time, int offset,
                              int bodyColor, int headColor) {
        int cycle = Math.floorMod((int) (time * 1.35F) + offset, 22);

        int visible;

        if (cycle < 9) {
            visible = cycle + 1;
        } else if (cycle < 15) {
            visible = path.length;
        } else {
            visible = Math.max(0, path.length - (cycle - 14));
        }

        visible = Mth.clamp(visible, 0, path.length);

        for (int i = 0; i < visible; i++) {
            int[] point = path[i];
            int color = i == visible - 1 ? headColor : bodyColor;

            guiGraphics.fill(
                    guiX + point[0],
                    guiY + point[1],
                    guiX + point[0] + 1,
                    guiY + point[1] + 1,
                    color
            );
        }
    }

    private void drawMaskedFillVerticalSmooth(GuiGraphics guiGraphics, int guiX, int guiY,
                                              int[][] mask, float fill,
                                              int bodyColor, int midColor, int surfaceColor) {
        fill = Mth.clamp(fill, 0.0F, 1.0F);

        if (fill <= 0.0F || mask.length == 0) {
            return;
        }

        int minY = mask[0][0];
        int maxY = mask[0][0];

        for (int[] span : mask) {
            minY = Math.min(minY, span[0]);
            maxY = Math.max(maxY, span[0]);
        }

        int height = maxY - minY + 1;
        float exactRows = height * fill;
        int filledRows = Mth.ceil(exactRows);
        int firstFilledY = maxY - filledRows + 1;
        float fractional = exactRows - Mth.floor(exactRows);

        if (filledRows <= 0) {
            return;
        }

        for (int[] span : mask) {
            int y = span[0];

            if (y < firstFilledY) {
                continue;
            }

            int color;

            if (y == firstFilledY) {
                int alpha = fill >= 1.0F
                        ? 255
                        : (fractional <= 0.001F ? 255 : (int) Mth.clamp(120.0F + fractional * 135.0F, 120.0F, 255.0F));
                color = withAlpha(surfaceColor, alpha);
            } else if (y == firstFilledY + 1) {
                color = midColor;
            } else {
                color = bodyColor;
            }

            guiGraphics.fill(
                    guiX + span[1],
                    guiY + y,
                    guiX + span[2] + 1,
                    guiY + y + 1,
                    color
            );
        }
    }

    private void drawPipeProgressSmooth(GuiGraphics guiGraphics, int guiX, int guiY,
                                        int[][] path, float progress, int thickness,
                                        int bodyColor, int midColor, int headColor) {
        progress = Mth.clamp(progress, 0.0F, 1.0F);

        int totalLength = getPathLength(path);
        if (totalLength <= 0 || progress <= 0.0F) {
            return;
        }

        float exactLength = totalLength * progress;
        int fullLength = Mth.floor(exactLength);
        float fraction = exactLength - fullLength;

        for (int distance = 0; distance <= fullLength; distance++) {
            int[] point = getPointAtDistance(path, distance);

            int remaining = fullLength - distance;
            int color;

            if (remaining <= 1) {
                color = headColor;
            } else if (remaining <= 4) {
                color = blend(midColor, headColor, 1.0F - remaining / 4.0F);
            } else {
                color = bodyColor;
            }

            drawPipePixel(guiGraphics, guiX, guiY, point[0], point[1], thickness, color);
        }

        if (fraction > 0.001F && fullLength < totalLength) {
            int[] nextPoint = getPointAtDistance(path, fullLength + 1);
            int alpha = (int) Mth.clamp(85.0F + fraction * 170.0F, 85.0F, 255.0F);

            drawPipePixel(guiGraphics, guiX, guiY, nextPoint[0], nextPoint[1], thickness, withAlpha(headColor, alpha));
        }
    }

    private void drawPipeAnimated(GuiGraphics guiGraphics, int guiX, int guiY,
                                  int[][] path, float progress, float time,
                                  int thickness, int baseColor, int movingColor,
                                  float speed, int bandLength) {
        progress = Mth.clamp(progress, 0.0F, 1.0F);

        int totalLength = getPathLength(path);
        if (totalLength <= 0) {
            return;
        }

        int activeLength = Math.round(totalLength * progress);
        if (activeLength <= 0) {
            return;
        }

        drawPipeProgressSmooth(guiGraphics, guiX, guiY, path, progress, thickness,
                baseColor, blend(baseColor, movingColor, 0.25F), blend(baseColor, movingColor, 0.45F));

        float head = (time * speed) % activeLength;

        for (int i = 0; i < bandLength; i++) {
            float distance = head - i;

            if (distance < 0.0F) {
                distance += activeLength;
            }

            int[] point = getPointAtDistance(path, distance);

            float t = 1.0F - (i / (float) Math.max(1, bandLength - 1));
            int color = blend(baseColor, movingColor, 0.25F + t * 0.75F);

            drawPipePixel(guiGraphics, guiX, guiY, point[0], point[1], thickness, color);
        }
    }

    private void drawPipePixel(GuiGraphics guiGraphics, int guiX, int guiY,
                               int x, int y, int thickness, int color) {
        guiGraphics.fill(
                guiX + x,
                guiY + y,
                guiX + x + thickness,
                guiY + y + thickness,
                color
        );
    }

    private int[] getPointAtDistance(int[][] path, float distance) {
        for (int i = 0; i < path.length - 1; i++) {
            int x1 = path[i][0];
            int y1 = path[i][1];
            int x2 = path[i + 1][0];
            int y2 = path[i + 1][1];

            int segmentLength = Math.abs(x2 - x1) + Math.abs(y2 - y1);

            if (segmentLength <= 0) {
                continue;
            }

            if (distance <= segmentLength) {
                float progress = distance / segmentLength;
                int x = Math.round(Mth.lerp(progress, x1, x2));
                int y = Math.round(Mth.lerp(progress, y1, y2));
                return new int[] { x, y };
            }

            distance -= segmentLength;
        }

        return path[path.length - 1];
    }

    private int getPathLength(int[][] path) {
        int length = 0;

        for (int i = 0; i < path.length - 1; i++) {
            length += Math.abs(path[i + 1][0] - path[i][0]);
            length += Math.abs(path[i + 1][1] - path[i][1]);
        }

        return length;
    }

    private int blend(int colorA, int colorB, float t) {
        t = Mth.clamp(t, 0.0F, 1.0F);

        int aA = (colorA >> 24) & 0xFF;
        int rA = (colorA >> 16) & 0xFF;
        int gA = (colorA >> 8) & 0xFF;
        int bA = colorA & 0xFF;

        int aB = (colorB >> 24) & 0xFF;
        int rB = (colorB >> 16) & 0xFF;
        int gB = (colorB >> 8) & 0xFF;
        int bB = colorB & 0xFF;

        int a = Math.round(Mth.lerp(t, aA, aB));
        int r = Math.round(Mth.lerp(t, rA, rB));
        int g = Math.round(Mth.lerp(t, gA, gB));
        int b = Math.round(Mth.lerp(t, bA, bB));

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private int withAlpha(int color, int alpha) {
        alpha = Mth.clamp(alpha, 0, 255);
        return (alpha << 24) | (color & 0x00FFFFFF);
    }
}
