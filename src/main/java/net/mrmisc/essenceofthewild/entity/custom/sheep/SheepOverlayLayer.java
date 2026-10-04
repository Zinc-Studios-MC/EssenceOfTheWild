package net.mrmisc.essenceofthewild.entity.custom.sheep;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(Dist.CLIENT)
public class SheepOverlayLayer extends GeoRenderLayer<SheepEntity> {

    public SheepOverlayLayer(GeoRenderer<SheepEntity> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, SheepEntity animatable, BakedGeoModel bakedModel, RenderType renderType,
                       MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick,
                       int packedLight, int packedOverlay) {
        if (animatable.isBaby()) {
            return;
        }

        ResourceLocation texture = EOTWUtils.getLoc("textures/entity/sheep/"
                + (animatable.isSheared() ? "sheared_sheep.png" : "sheep_wool.png"));
        RenderType shellType = RenderType.entityCutoutNoCull(texture);
        float[] rgb = tint(animatable, partialTick);

        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, shellType,
                bufferSource.getBuffer(shellType), partialTick, packedLight, packedOverlay,
                rgb[0], rgb[1], rgb[2], 1.0F);
    }

    private static float[] tint(SheepEntity sheep, float partialTick) {
        if (sheep.hasCustomName() && "jeb_".equals(sheep.getName().getString())) {
            int offset = sheep.tickCount / 25 + sheep.getId();
            int count = DyeColor.values().length;
            float blend = ((float) (sheep.tickCount % 25) + partialTick) / 25.0F;
            float[] from = Sheep.getColorArray(DyeColor.byId(offset % count));
            float[] to = Sheep.getColorArray(DyeColor.byId((offset + 1) % count));

            return new float[]{
                    from[0] * (1.0F - blend) + to[0] * blend,
                    from[1] * (1.0F - blend) + to[1] * blend,
                    from[2] * (1.0F - blend) + to[2] * blend
            };
        }

        float[] base = Sheep.getColorArray(sheep.getColor());
        float saturation = 0.9F;
        float brightness = 1.1F;
        float grey = (base[0] + base[1] + base[2]) / 3.0F;
        float[] out = new float[3];

        for (int i = 0; i < 3; i++) {
            out[i] = Math.min((grey + (base[i] - grey) * saturation) * brightness, 1.0F);
        }
        return out;
    }
}
