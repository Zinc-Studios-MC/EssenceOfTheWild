package net.mrmisc.essenceofthewild.entity.misc.silk_ball;

import net.mrmisc.essenceofthewild.util.EOTWUtils;
import net.mrmisc.essenceofthewild.entity.util.EotwGeoModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.mrmisc.essenceofthewild.entity.misc.SilkBall;

import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SilkBallRenderer extends GeoEntityRenderer<SilkBall> {

    private static final double MODEL_CENTRE_OFFSET = -11.0D / 16.0D;

    public SilkBallRenderer(EntityRendererProvider.Context context) {
        super(context, new EotwGeoModel<>("silk_ball", EOTWUtils.getLoc("textures/entity/misc/silk_ball.png")));
        this.shadowRadius = 0.15f;
    }

    @Override
    public void preRender(PoseStack poseStack, SilkBall animatable, BakedGeoModel model,
                          MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                          float partialTick, int packedLight, int packedOverlay,
                          float red, float green, float blue, float alpha) {
        if (!isReRender) {
            poseStack.translate(0.0D, MODEL_CENTRE_OFFSET, 0.0D);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick,
                packedLight, packedOverlay, red, green, blue, alpha);
    }
}
