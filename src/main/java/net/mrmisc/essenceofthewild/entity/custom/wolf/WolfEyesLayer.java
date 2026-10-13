package net.mrmisc.essenceofthewild.entity.custom.wolf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class WolfEyesLayer extends GeoRenderLayer<WolfEntity> {
    public WolfEyesLayer(GeoRenderer<WolfEntity> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack pose, WolfEntity wolf, BakedGeoModel model, RenderType renderType,
                       MultiBufferSource buffers, VertexConsumer buffer, float partialTick, int light, int overlay) {
        if (wolf.isBaby() || wolf.isInvisible()) {
            return;
        }
        String id = wolf.getVariantId();
        String name = wolf.isAngry() ? "angry_wolf_eyes_e"
                : id.equals("mountain") ? "mountain_wolf_e" : "eye_" + (id.equals("wolf") ? "wolf" : id + "_wolf") + "_e";
        RenderType type = RenderType.eyes(EOTWUtils.getLoc("textures/entity/wolf/" + name + ".png"));
        getRenderer().reRender(model, pose, buffers, wolf, type, buffers.getBuffer(type), partialTick,
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}
