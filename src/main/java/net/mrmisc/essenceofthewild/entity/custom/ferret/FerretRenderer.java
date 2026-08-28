package net.mrmisc.essenceofthewild.entity.custom.ferret;

import net.mrmisc.essenceofthewild.entity.util.AgedGeoModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FerretRenderer extends GeoEntityRenderer<FerretEntity> {

    public FerretRenderer(Context context) {
        super(context, new AgedGeoModel<>("ferret", "baby_ferret", FerretEntity::getVariant));
        this.shadowRadius = 0.5f;
    }

    @Override
    public void preRender(PoseStack poseStack, FerretEntity animatable, BakedGeoModel model,
                          MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                          float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (animatable.isBaby()) {
            poseStack.scale(0.9F, 0.9F, 0.9F);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick,
                packedLight, packedOverlay, red, green, blue, alpha);
    }
}
