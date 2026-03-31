package net.mrmisc.essenceofthewild.entity.custom.pig;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PigRenderer extends MobRenderer<PigEntity, PigModel<PigEntity>> {
    public PigRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new PigModel<>(pContext.bakeLayer(PigModel.LAYER_LOCATION)), 0.7f);
        this.addLayer(new PigSaddleLayer(this, pContext.getModelSet()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(PigEntity pEntity) {
        return pEntity.getVariant().location();
    }
}
