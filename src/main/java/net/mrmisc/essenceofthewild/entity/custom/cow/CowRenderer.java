package net.mrmisc.essenceofthewild.entity.custom.cow;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CowRenderer extends MobRenderer<CowEntity, CowModel> {
    public CowRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new CowModel(pContext.bakeLayer(CowModel.LAYER_LOCATION)), 0.8f);
    }

    @Override
    public ResourceLocation getTextureLocation(CowEntity pEntity) {
        CowVariant variant = pEntity.getVariant();
        return pEntity.isBaby() ? variant.babyLocation() : variant.adultLocation();
    }

    @Override
    public void render(CowEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
            MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            pPoseStack.scale(0.5F, 0.5F, 0.5F);
        }
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
