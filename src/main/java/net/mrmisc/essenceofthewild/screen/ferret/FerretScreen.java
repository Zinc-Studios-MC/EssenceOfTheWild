package net.mrmisc.essenceofthewild.screen.ferret;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.custom.ferret.FerretEntity;
import net.mrmisc.essenceofthewild.menu.ferret.FerretMenu;

public class FerretScreen extends AbstractContainerScreen<FerretMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/gui/ferret.png");

    private static final int ENTITY_BOX_X = 26;
    private static final int ENTITY_BOX_Y = 18;
    private static final int ENTITY_BOX_SIZE = 52;
    private static final int ENTITY_RENDER_X = 52;
    private static final int ENTITY_RENDER_Y = 63;
    private static final int ENTITY_RENDER_SCALE = 31;

    private float xMouse;
    private float yMouse;

    public FerretScreen(FerretMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        xMouse = mouseX;
        yMouse = mouseY;
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        FerretEntity ferret = menu.getFerret();
        if (ferret != null) {
            guiGraphics.enableScissor(
                    x + ENTITY_BOX_X,
                    y + ENTITY_BOX_Y,
                    x + ENTITY_BOX_X + ENTITY_BOX_SIZE,
                    y + ENTITY_BOX_Y + ENTITY_BOX_SIZE
            );
            InventoryScreen.renderEntityInInventoryFollowsMouse(
                    guiGraphics,
                    x + ENTITY_RENDER_X,
                    y + ENTITY_RENDER_Y,
                    ENTITY_RENDER_SCALE,
                    (float) (x + ENTITY_RENDER_X) - xMouse,
                    (float) (y + ENTITY_BOX_Y + ENTITY_BOX_SIZE / 2) - yMouse,
                    ferret
            );
            guiGraphics.disableScissor();
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
            // Empty on purpose!!
    }
}
