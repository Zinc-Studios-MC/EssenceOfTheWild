package net.mrmisc.essenceofthewild.entity.custom.sheep;

import net.mrmisc.essenceofthewild.entity.util.AgedGeoModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SheepRenderer extends GeoEntityRenderer<SheepEntity> {

    public SheepRenderer(EntityRendererProvider.Context context) {
        super(context, new AgedGeoModel<>("sheep", "baby_sheep", "sheep", "sheep", SheepEntity::getVariant));
        this.shadowRadius = 0.7F;
        addRenderLayer(new SheepWoolLayer(this));
        addRenderLayer(new ShearedSheepLayer(this));
    }

    @Override
    public void preRender(PoseStack poseStack, SheepEntity animatable, software.bernie.geckolib.cache.object.BakedGeoModel model,
                          MultiBufferSource bufferSource, com.mojang.blaze3d.vertex.VertexConsumer buffer, boolean isReRender,
                          float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (animatable.isBaby()) {
            poseStack.scale(0.7F, 0.7F, 0.7F);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick,
                packedLight, packedOverlay, red, green, blue, alpha);
    }
}
