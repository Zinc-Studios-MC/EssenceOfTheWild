package net.mrmisc.essenceofthewild.entity.custom.rabbit;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RabbitRenderer extends MobRenderer<RabbitEntity, RabbitModel> {
    public RabbitRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new RabbitModel(pContext.bakeLayer(RabbitModel.LAYER_LOCATION)), 0.2f);
    }

    @Override
    public ResourceLocation getTextureLocation(RabbitEntity pEntity) {
        return pEntity.getRabbitVariant().location();
    }
}
