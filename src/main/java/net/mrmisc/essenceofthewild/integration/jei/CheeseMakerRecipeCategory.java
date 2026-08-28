package net.mrmisc.essenceofthewild.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.recipe.cheesemaker.CheeseMakerRecipe;

public class CheeseMakerRecipeCategory implements IRecipeCategory<CheeseMakerRecipe> {
    private static final int WIDTH = 176;
    private static final int HEIGHT = 92;

    private static final int INPUT_X = 16;
    private static final int OUTPUT_X = 142;
    private static final int SLOT_Y = 38;

    private static final int OUTER = 0xFF151912;
    private static final int PANEL = 0xFF252D21;
    private static final int PANEL_LIGHT = 0xFF3D4A34;
    private static final int PANEL_DARK = 0xFF1B2118;

    private static final int WOOD_DARK = 0xFF4F3520;
    private static final int WOOD = 0xFF7A5934;
    private static final int WOOD_LIGHT = 0xFFB48A52;

    private static final int SLOT_DARK = 0xFF171B15;
    private static final int SLOT_INNER = 0xFF2B3128;
    private static final int SLOT_LIGHT = 0xFF879263;

    private static final int MILK = 0xFFF8F4E8;
    private static final int MILK_SHADOW = 0xFFD8D1BF;
    private static final int WHEY = 0xFFFFE3A3;
    private static final int CHEESE = 0xFFFFD65A;
    private static final int CHEESE_LIGHT = 0xFFFFEA92;
    private static final int CHEESE_DARK = 0xFFC58A2B;
    private static final int WARM_GLOW = 0xFFFFD879;

    private final IDrawable background;
    private final IDrawable icon;

    public CheeseMakerRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        icon = guiHelper.createDrawableItemStack(new ItemStack(EOTWBlocks.CHEESE_MAKER.get()));
    }

    @Override
    public RecipeType<CheeseMakerRecipe> getRecipeType() {
        return EOTWJeiPlugin.CHEESE_MAKER;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.essenceofthewild.cheese_maker");
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
    public void setRecipe(IRecipeLayoutBuilder builder, CheeseMakerRecipe recipe, IFocusGroup focuses) {
        RegistryAccess registryAccess = Minecraft.getInstance().level == null
                ? RegistryAccess.EMPTY
                : Minecraft.getInstance().level.registryAccess();

        builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X, SLOT_Y)
                .addIngredients(recipe.getIngredient());

        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, SLOT_Y)
                .addItemStack(recipe.getResultItem(registryAccess));
    }

    @Override
    public void draw(CheeseMakerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;

        float time = getPreviewTime();
        float progress = (time % 90.0F) / 90.0F;
        float smoothProgress = smoothStep(progress);

        drawFrame(guiGraphics);
        drawLabels(guiGraphics, font);
        drawWorkSurface(guiGraphics);
        drawPipes(guiGraphics, smoothProgress);
        drawCheeseMakerEffects(guiGraphics, time, smoothProgress);

        drawSlot(guiGraphics, INPUT_X, SLOT_Y);
        drawSlot(guiGraphics, OUTPUT_X, SLOT_Y);

        renderCheeseMaker(guiGraphics);
        drawBottomText(recipe, guiGraphics, font);
    }

    private static void drawFrame(GuiGraphics guiGraphics) {
        guiGraphics.fill(0, 0, WIDTH, HEIGHT, OUTER);
        guiGraphics.fill(1, 1, WIDTH - 1, HEIGHT - 1, PANEL_LIGHT);
        guiGraphics.fill(3, 3, WIDTH - 3, HEIGHT - 3, PANEL);
        guiGraphics.fill(6, 6, WIDTH - 6, HEIGHT - 6, PANEL_DARK);

        guiGraphics.fill(8, 8, WIDTH - 8, 9, 0xFF576447);
        guiGraphics.fill(8, HEIGHT - 10, WIDTH - 8, HEIGHT - 9, 0xFF11140F);

        guiGraphics.fill(8, 70, WIDTH - 8, 84, 0xFF20261C);
        guiGraphics.fill(9, 71, WIDTH - 9, 72, 0xFF445138);
        guiGraphics.fill(9, 83, WIDTH - 9, 84, 0xFF11150F);
    }

    private static void drawLabels(GuiGraphics guiGraphics, Font font) {
        Component input = Component.literal("Input");
        Component result = Component.literal("Result");
        Component center = Component.literal("Cheese Making");

        guiGraphics.drawString(font, center, (WIDTH - font.width(center)) / 2, 10, 0xFFFFE7A7, false);
        guiGraphics.drawString(font, input, INPUT_X + 9 - font.width(input) / 2, 28, 0xFFD8C99B, false);
        guiGraphics.drawString(font, result, OUTPUT_X + 9 - font.width(result) / 2, 28, 0xFFD8C99B, false);
    }

    private static void drawWorkSurface(GuiGraphics guiGraphics) {
        guiGraphics.fill(38, 34, 138, 61, WOOD_DARK);
        guiGraphics.fill(40, 35, 136, 59, WOOD);
        guiGraphics.fill(42, 37, 134, 40, WOOD_LIGHT);
        guiGraphics.fill(42, 54, 134, 58, 0xFF5E3E24);

        for (int i = 0; i < 6; i++) {
            int x = 44 + i * 15;
            guiGraphics.fill(x, 41, x + 9, 42, 0xFF644529);
            guiGraphics.fill(x + 3, 52, x + 14, 53, 0xFF967044);
        }
    }

    private static void drawPipes(GuiGraphics guiGraphics, float progress) {
        drawPipeBase(guiGraphics, 34, 45, 67, 50);
        drawPipeBase(guiGraphics, 105, 45, 142, 50);

        guiGraphics.fill(36, 46, 65, 49, MILK_SHADOW);
        guiGraphics.fill(36, 46, 65, 48, MILK);
        guiGraphics.fill(36, 48, 65, 49, 0xFFE9DDC8);

        int cheeseWidth = Mth.ceil(34.0F * progress);
        if (cheeseWidth > 0) {
            guiGraphics.fill(106, 46, 106 + cheeseWidth, 49, CHEESE_DARK);
            guiGraphics.fill(106, 46, 106 + Math.max(1, cheeseWidth - 3), 48, CHEESE);
            guiGraphics.fill(106, 46, 106 + Math.max(1, cheeseWidth - 8), 47, CHEESE_LIGHT);
        }

        guiGraphics.fill(66, 43, 70, 52, 0xFF2A3124);
        guiGraphics.fill(103, 43, 107, 52, 0xFF2A3124);
    }

    private static void drawPipeBase(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2) {
        guiGraphics.fill(x1, y1, x2, y2, 0xFF1A1F17);
        guiGraphics.fill(x1, y1 + 1, x2, y2 - 1, 0xFF8A7449);
        guiGraphics.fill(x1, y1 + 1, x2, y1 + 2, 0xFFC7A96B);
        guiGraphics.fill(x1, y2 - 2, x2, y2 - 1, 0xFF4A3822);
    }

    private static void drawCheeseMakerEffects(GuiGraphics guiGraphics, float time, float progress) {
        int pulse = 90 + (int) (Mth.sin(time * 0.16F) * 30.0F);

        guiGraphics.fill(70, 52, 106, 55, withAlpha(WARM_GLOW, pulse));
        guiGraphics.fill(74, 49, 102, 52, withAlpha(blend(MILK, CHEESE, progress), 155));
        guiGraphics.fill(78, 46, 98, 48, withAlpha(CHEESE_LIGHT, 90));

        for (int i = 0; i < 5; i++) {
            float phase = time * 0.075F + i * 1.4F;
            int x = 72 + i * 7;
            int y = 40 - Math.floorMod((int) (phase * 8.0F), 13);
            int alpha = 70 + (int) (Mth.sin(phase) * 25.0F + 25.0F);

            guiGraphics.fill(x, y, x + 1, y + 1, withAlpha(WHEY, alpha));
        }
    }

    private static void drawSlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x - 2, y - 2, x + 20, y + 20, 0xFF10140F);
        guiGraphics.fill(x - 1, y - 1, x + 19, y + 19, SLOT_LIGHT);
        guiGraphics.fill(x, y, x + 18, y + 18, SLOT_DARK);
        guiGraphics.fill(x + 1, y + 1, x + 17, y + 17, SLOT_INNER);

        guiGraphics.fill(x + 1, y + 1, x + 17, y + 2, 0xFF566245);
        guiGraphics.fill(x + 1, y + 1, x + 2, y + 17, 0xFF566245);
        guiGraphics.fill(x + 1, y + 16, x + 17, y + 17, 0xFF141812);
        guiGraphics.fill(x + 16, y + 1, x + 17, y + 17, 0xFF141812);

        guiGraphics.fill(x + 4, y + 4, x + 14, y + 5, 0xFF333B2E);
        guiGraphics.fill(x + 4, y + 13, x + 14, y + 14, 0xFF20261D);
    }

    private static void renderCheeseMaker(GuiGraphics guiGraphics) {
        ItemStack stack = new ItemStack(EOTWBlocks.CHEESE_MAKER.get());

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(73.0F, 25.0F, 100.0F);
        poseStack.scale(1.85F, 1.85F, 1.85F);
        guiGraphics.renderItem(stack, 0, 0);
        poseStack.popPose();
    }

    private static void drawBottomText(CheeseMakerRecipe recipe, GuiGraphics guiGraphics, Font font) {
        Component timeText = Component.translatable(
                "jei.essenceofthewild.time",
                EOTWJeiText.formatTicks(recipe.getProcessTicks())
        );

        int maxWidth = WIDTH - 16;
        int textWidth = font.width(timeText);
        int textX = Math.max(8, (WIDTH - textWidth) / 2);

        if (textWidth > maxWidth) {
            String shortened = font.plainSubstrByWidth(timeText.getString(), maxWidth - font.width("...")) + "...";
            guiGraphics.drawString(font, shortened, 8, 75, 0xFFFFE7A7, false);
            return;
        }

        guiGraphics.drawString(font, timeText, textX, 75, 0xFFFFE7A7, false);
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