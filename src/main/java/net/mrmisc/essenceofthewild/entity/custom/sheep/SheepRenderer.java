package net.mrmisc.essenceofthewild.entity.custom.sheep;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SheepRenderer extends GeoEntityRenderer<SheepEntity> {

    public SheepRenderer(EntityRendererProvider.Context context) {
        super(context, new SheepGeoModel());
        this.shadowRadius = 0.7F;
        addRenderLayer(new SheepWoolLayer(this));
        addRenderLayer(new ShearedSheepLayer(this));
    }

    @Override
    public void preRender(PoseStack poseStack, SheepEntity animatable, software.bernie.geckolib.cache.object.BakedGeoModel model,
                          MultiBufferSource bufferSource, com.mojang.blaze3d.vertex.VertexConsumer buffer, boolean isReRender,
                          float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // lambs have their own geo but the old renderer also scaled it down, keeping that so they dont change size
        if (animatable.isBaby()) {
            poseStack.scale(0.7F, 0.7F, 0.7F);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick,
                packedLight, packedOverlay, red, green, blue, alpha);
    }
}
