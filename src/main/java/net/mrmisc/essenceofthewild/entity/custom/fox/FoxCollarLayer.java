package net.mrmisc.essenceofthewild.entity.custom.fox;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.animal.Fox;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class FoxCollarLayer extends GeoRenderLayer<FoxEntity> {
    public FoxCollarLayer(GeoRenderer<FoxEntity> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack pose, FoxEntity fox, BakedGeoModel model, RenderType renderType,
                       MultiBufferSource buffers, VertexConsumer buffer, float partialTick, int light, int overlay) {
        if (!fox.isTame() || fox.isInvisible()) {
            return;
        }
        boolean snow = fox.getVariant() == Fox.Type.SNOW;
        String name = fox.isBaby() ? (snow ? "collar_snow_fox_texture" : "collar_baby_fox_texture")
                : (snow ? "collar_snow_fox_adult_texture" : "collar_fox_adult_texture");
        RenderType type = RenderType.entityCutoutNoCull(EOTWUtils.getLoc("textures/entity/fox/" + name + ".png"));
        float[] color = fox.getCollarColor().getTextureDiffuseColors();
        getRenderer().reRender(model, pose, buffers, fox, type, buffers.getBuffer(type), partialTick,
                light, overlay, color[0], color[1], color[2], 1);
    }
}
