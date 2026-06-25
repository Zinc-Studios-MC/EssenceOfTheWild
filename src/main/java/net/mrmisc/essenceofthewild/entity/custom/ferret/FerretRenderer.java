package net.mrmisc.essenceofthewild.entity.custom.ferret;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FerretRenderer extends MobRenderer<FerretEntity, FerretModel> {

    public FerretModel adult;
    public BabyFerretModel baby;

    public FerretRenderer(Context pContext) {
        super(pContext, new FerretModel(pContext.bakeLayer(FerretModel.LAYER_LOCATION)), 0.5f);

        this.adult = new FerretModel(pContext.bakeLayer(FerretModel.LAYER_LOCATION));
        this.baby = new BabyFerretModel(pContext.bakeLayer(BabyFerretModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(FerretEntity pEntity) {
        FerretVariant variant = pEntity.getVariant();
        return pEntity.isBaby() ? variant.babyLocation() : variant.adultLocation();
    }

    @Override
    public void render(FerretEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
            MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            this.model = baby;
            pPoseStack.scale(0.9F, 0.9F, 0.9F);
        } else {
            this.model = adult;
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
    
}