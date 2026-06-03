package net.mrmisc.essenceofthewild.entity.misc.arrow;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class UnderwaterArrowRenderer extends ArrowRenderer<UnderwaterArrow> {
    public UnderwaterArrowRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(UnderwaterArrow pEntity) {
        return ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/misc/underwater_arrow.png");
    }
}
