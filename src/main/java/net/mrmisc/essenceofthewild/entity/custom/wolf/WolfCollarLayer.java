package net.mrmisc.essenceofthewild.entity.custom.wolf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class WolfCollarLayer extends GeoRenderLayer<WolfEntity> {
    public WolfCollarLayer(GeoRenderer<WolfEntity> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack pose, WolfEntity wolf, BakedGeoModel model, RenderType renderType,
                       MultiBufferSource buffers, VertexConsumer buffer, float partialTick, int light, int overlay) {
        if (!wolf.isTame() || wolf.isInvisible()) {
            return;
        }
        String name = (wolf.isBaby() ? "baby_" : "") + "wolf_"
                + (WolfVariants.bigCollar(wolf.getVariant()) ? "big_" : "") + "collar_texture";
        RenderType type = RenderType.entityCutoutNoCull(EOTWUtils.getLoc("textures/entity/wolf/" + name + ".png"));
        float[] color = wolf.getCollarColor().getTextureDiffuseColors();
        getRenderer().reRender(model, pose, buffers, wolf, type, buffers.getBuffer(type), partialTick,
                light, overlay, color[0], color[1], color[2], 1);
    }
}
