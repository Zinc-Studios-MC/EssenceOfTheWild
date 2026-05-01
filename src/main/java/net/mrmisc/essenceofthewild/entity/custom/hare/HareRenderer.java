package net.mrmisc.essenceofthewild.entity.custom.hare;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HareRenderer extends MobRenderer<HareEntity, HareModel> {
    public HareRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new HareModel(pContext.bakeLayer(HareModel.LAYER_LOCATION)), 0.2f);
    }

    @Override
    public ResourceLocation getTextureLocation(HareEntity pEntity) {
        return pEntity.getRabbitVariant().location();
    }
}
