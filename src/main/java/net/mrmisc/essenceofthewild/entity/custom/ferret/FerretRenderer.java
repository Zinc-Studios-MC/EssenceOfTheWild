package net.mrmisc.essenceofthewild.entity.custom.ferret;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FerretRenderer extends MobRenderer<FerretEntity, FerretModel<FerretEntity>> {

    public FerretRenderer(Context pContext) {
        super(pContext, new FerretModel<>(pContext.bakeLayer(FerretModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(FerretEntity pEntity) {
        return pEntity.getVariant().location();
    }

    @Override
    public void render(FerretEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
            MultiBufferSource pBuffer, int pPackedLight) {

        if(pEntity.isBaby()){
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
        }
        
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
    
}