package net.mrmisc.essenceofthewild.integration.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.block.entity.custom.freezer.WoodenFreezerBlockEntity;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import net.mrmisc.essenceofthewild.recipe.freezer.WoodenFreezerRecipe;

public class WoodenFreezerRecipeCategory implements IRecipeCategory<WoodenFreezerRecipe> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/gui/wooden_freezer_gui.png");

    private static final int WIDTH = 176;
    private static final int HEIGHT = 84;

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
            {12, 82, 93},
            {13, 81, 94},
            {14, 81, 94},
            {15, 82, 93}
    };

    private static final int[][] RIGHT_TANK_MASK = {
            {29, 120, 131},
            {30, 122, 131},
            {31, 120, 131},
            {32, 122, 132},
            {33, 119, 132},
            {34, 122, 131},
            {35, 120, 131},
            {36, 120, 131}
    };

    private static final int[][] CHAMBER_MASK = {
            {33, 80, 95},
            {34, 80, 95},
            {35, 80, 95},
            {36, 80, 95},
            {37, 80, 95},
            {38, 80, 95},
            {39, 80, 95},
            {40, 80, 95},
            {41, 80, 95},
            {42, 80, 95},
            {43, 80, 95},
            {44, 80, 95},
            {45, 80, 95},
            {46, 80, 95},
            {47, 80, 95},
            {48, 80, 95}
    };

    private static final int[][] ICE_PIPE = {
            {34, 32},
            {49, 32},
            {49, 14},
            {81, 14}
    };

    private static final int[][] MILK_PIPE = {
            {142, 33},
            {133, 33},
            {133, 32},
            {121, 32},
            {121, 34},
            {110, 34},
            {110, 40},
            {97, 40}
    };

    private static final int[][] LEFT_CONE_PIPE = {
            {82, 50},
            {82, 56},
            {50, 56}
    };

    private static final int[][] RIGHT_CONE_PIPE = {
            {92, 50},
            {92, 56},
            {126, 56}
    };

    private static final int[][] LEFT_DRIP_PATH = {
            {84, 17},
            {84, 18},
            {84, 19},
            {84, 20},
            {84, 21},
            {84, 22},
            {84, 23}
    };

    private static final int[][] CENTER_DRIP_PATH = {
            {88, 17},
            {88, 18},
            {88, 19},
            {88, 20},
            {88, 25},
            {88, 26}
    };

    private static final int[][] RIGHT_DRIP_PATH = {
            {92, 17},
            {92, 18},
            {92, 19},
            {92, 20},
            {92, 21},
            {92, 22},
            {92, 23},
            {92, 24},
            {92, 25}
    };

    private final IDrawable background;
    private final IDrawable icon;

    public WoodenFreezerRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        icon = guiHelper.createDrawableItemStack(new ItemStack(EOTWBlocks.WOODEN_FREEZER.get()));
    }

    @Override
    public RecipeType<WoodenFreezerRecipe> getRecipeType() {
        return EOTWJeiPlugin.WOODEN_FREEZER;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.essenceofthewild.wooden_freezer");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WoodenFreezerRecipe recipe, IFocusGroup focuses) {
        RegistryAccess registryAccess = Minecraft.getInstance().level == null ? RegistryAccess.EMPTY : Minecraft.getInstance().level.registryAccess();

        builder.addSlot(RecipeIngredientRole.INPUT, 17, 20)
                .addItemStack(new ItemStack(Items.ICE));
        builder.addSlot(RecipeIngredientRole.INPUT, 143, 20)
                .addItemStack(new ItemStack(EOTWItems.SHEEP_MILK_BUCKET.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 33)
                .addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.INPUT, 34, 48)
                .addItemStack(new ItemStack(EOTWItems.CONE.get()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 126, 48)
                .addItemStack(recipe.getResultItem(registryAccess));
    }

    @Override
    public void draw(WoodenFreezerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        float time = getPreviewTime();
        float progress = ((time % WoodenFreezerBlockEntity.MAX_PROGRESS) + 1.0F) / WoodenFreezerBlockEntity.MAX_PROGRESS;
        float smoothProgress = smoothStep(progress);

        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, WIDTH, HEIGHT, 256, 256);

        drawMaskedFillVerticalSmooth(guiGraphics, TOP_TANK_MASK, 1.0F, ICE_DEEP, ICE_MID, ICE_FILL);
        drawMaskedFillVerticalSmooth(guiGraphics, RIGHT_TANK_MASK, 1.0F, MILK_DEEP, MILK_MID, MILK_FILL);
        drawMaskedFillVerticalSmooth(guiGraphics, CHAMBER_MASK, smoothProgress, FREEZE_DEEP, FREEZE_MID, FREEZE_FILL);

        drawPipeAnimated(guiGraphics, ICE_PIPE, 1.0F, time, 1, ICE_DEEP, ICE_FILL, 1.55F, 10);
        drawPipeAnimated(guiGraphics, MILK_PIPE, 1.0F, time + 8.0F, 1, MILK_DEEP, MILK_FILL, 1.45F, 10);
        drawPipeProgressSmooth(guiGraphics, LEFT_CONE_PIPE, smoothProgress, 2, FREEZE_DEEP, FREEZE_MID, FREEZE_FILL);
        drawPipeProgressSmooth(guiGraphics, RIGHT_CONE_PIPE, smoothProgress, 2, FREEZE_DEEP, FREEZE_MID, FREEZE_FILL);
        drawTextureLockedDrip(guiGraphics, time);

        Component timeText = Component.translatable("jei.essenceofthewild.time", EOTWJeiText.formatTicks(WoodenFreezerBlockEntity.MAX_PROGRESS));
        guiGraphics.drawString(font, timeText, 5, 72, 0xFF303030, false);
    }

    private static float getPreviewTime() {
        Minecraft minecraft = Minecraft.getInstance();
        float time = minecraft.getFrameTime();

        if (minecraft.level != null) {
            time += minecraft.level.getGameTime();
        }

        return time;
    }

    private static float smoothStep(float value) {
        value = Mth.clamp(value, 0.0F, 1.0F);
        return value * value * (3.0F - 2.0F * value);
    }

    private static void drawTextureLockedDrip(GuiGraphics guiGraphics, float time) {
        drawDripPath(guiGraphics, LEFT_DRIP_PATH, time, 0, FREEZE_MID, FREEZE_FILL);
        drawDripPath(guiGraphics, CENTER_DRIP_PATH, time, 7, MILK_MID, MILK_FILL);
        drawDripPath(guiGraphics, RIGHT_DRIP_PATH, time, 13, FREEZE_MID, FREEZE_FILL);
    }

    private static void drawDripPath(GuiGraphics guiGraphics, int[][] path, float time, int offset, int bodyColor, int headColor) {
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
            guiGraphics.fill(point[0], point[1], point[0] + 1, point[1] + 1, color);
        }
    }

    private static void drawMaskedFillVerticalSmooth(GuiGraphics guiGraphics, int[][] mask, float fill, int bodyColor, int midColor, int surfaceColor) {
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

            guiGraphics.fill(span[1], y, span[2] + 1, y + 1, color);
        }
    }

    private static void drawPipeProgressSmooth(GuiGraphics guiGraphics, int[][] path, float progress, int thickness, int bodyColor, int midColor, int headColor) {
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

            drawPipePixel(guiGraphics, point[0], point[1], thickness, color);
        }

        if (fraction > 0.001F && fullLength < totalLength) {
            int[] nextPoint = getPointAtDistance(path, fullLength + 1);
            int alpha = (int) Mth.clamp(85.0F + fraction * 170.0F, 85.0F, 255.0F);
            drawPipePixel(guiGraphics, nextPoint[0], nextPoint[1], thickness, withAlpha(headColor, alpha));
        }
    }

    private static void drawPipeAnimated(GuiGraphics guiGraphics, int[][] path, float progress, float time, int thickness, int baseColor, int movingColor, float speed, int bandLength) {
        progress = Mth.clamp(progress, 0.0F, 1.0F);

        int totalLength = getPathLength(path);
        if (totalLength <= 0) {
            return;
        }

        int activeLength = Math.round(totalLength * progress);
        if (activeLength <= 0) {
            return;
        }

        drawPipeProgressSmooth(guiGraphics, path, progress, thickness, baseColor, blend(baseColor, movingColor, 0.25F), blend(baseColor, movingColor, 0.45F));

        float head = (time * speed) % activeLength;

        for (int i = 0; i < bandLength; i++) {
            float distance = head - i;

            if (distance < 0.0F) {
                distance += activeLength;
            }

            int[] point = getPointAtDistance(path, distance);
            float t = 1.0F - (i / (float) Math.max(1, bandLength - 1));
            int color = blend(baseColor, movingColor, 0.25F + t * 0.75F);

            drawPipePixel(guiGraphics, point[0], point[1], thickness, color);
        }
    }

    private static void drawPipePixel(GuiGraphics guiGraphics, int x, int y, int thickness, int color) {
        guiGraphics.fill(x, y, x + thickness, y + thickness, color);
    }

    private static int[] getPointAtDistance(int[][] path, float distance) {
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
                return new int[] {x, y};
            }

            distance -= segmentLength;
        }

        return path[path.length - 1];
    }

    private static int getPathLength(int[][] path) {
        int length = 0;

        for (int i = 0; i < path.length - 1; i++) {
            length += Math.abs(path[i + 1][0] - path[i][0]);
            length += Math.abs(path[i + 1][1] - path[i][1]);
        }

        return length;
    }

    private static int blend(int colorA, int colorB, float t) {
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

    private static int withAlpha(int color, int alpha) {
        alpha = Mth.clamp(alpha, 0, 255);
        return (alpha << 24) | (color & 0x00FFFFFF);
    }
}
