package net.mrmisc.essenceofthewild.entity.custom.chicken;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ChickenRenderer extends MobRenderer<ChickenEntity, ChickenModel<ChickenEntity>> {
    public ChickenRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new ChickenModel<>(pContext.bakeLayer(ChickenModel.LAYER_LOCATION)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(ChickenEntity pEntity) {
        return pEntity.getVariant().location();
    }
}
