package net.mrmisc.essenceofthewild.entity.custom.sheep;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SheepRenderer extends MobRenderer<SheepEntity, SheepModel> {
    public SheepRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SheepModel(pContext.bakeLayer(SheepModel.LAYER_LOCATION)), 0.7F);
        this.addLayer(new WoolLayer(this, pContext.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(SheepEntity pEntity) {
        return pEntity.getVariant().location();
    }
}
