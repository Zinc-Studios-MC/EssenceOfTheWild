package net.mrmisc.essenceofthewild.entity.custom.sheep;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SheepRenderer extends MobRenderer<SheepEntity, SheepModel> {
    public SheepModel adult;
    public BabySheepModel baby;
    public SheepRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SheepModel(pContext.bakeLayer(SheepModel.LAYER_LOCATION)), 0.7F);
        this.addLayer(new ShearedSheepLayer(this, pContext.getModelSet()));
        this.addLayer(new WoolLayer(this, pContext.getModelSet()));
        this.adult = new SheepModel(pContext.bakeLayer(SheepModel.LAYER_LOCATION));
        this.baby = new BabySheepModel(pContext.bakeLayer(BabySheepModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(SheepEntity pEntity) {
        SheepVariant variant = pEntity.getVariant();
        return pEntity.isBaby() ? variant.babyLocation() : variant.adultLocation();
    }

    @Override
    public void render(SheepEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
            MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            this.model = this.baby;
            pPoseStack.scale(0.7F, 0.7F, 0.7F);
        }else{
            this.model = this.adult;
        }
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}