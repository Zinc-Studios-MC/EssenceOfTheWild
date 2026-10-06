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
import software.bernie.geckolib.cache.object.GeoBone;
import net.mrmisc.essenceofthewild.entity.util.EotwGeoModel;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(Dist.CLIENT)
public class SheepOverlayLayer extends GeoRenderLayer<SheepEntity> {
    private final EotwGeoModel<SheepEntity> wool = new EotwGeoModel<>("sheep_wool", "sheep",
            sheep -> EOTWUtils.getLoc("textures/entity/sheep/sheep_wool.png"));
    private final EotwGeoModel<SheepEntity> sheared = new EotwGeoModel<>("sheared_sheep", "sheep",
            sheep -> EOTWUtils.getLoc("textures/entity/sheep/sheared_sheep.png"));

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

        EotwGeoModel<SheepEntity> model = animatable.isSheared() ? sheared : wool;
        BakedGeoModel shell = model.getBakedModel(model.getModelResource(animatable));
        for (int i = 0; i < bakedModel.topLevelBones().size(); i++) {
            copyPose(bakedModel.topLevelBones().get(i), shell.topLevelBones().get(i));
        }
        ResourceLocation texture = model.getTextureResource(animatable);
        RenderType shellType = RenderType.entityCutoutNoCull(texture);
        float[] rgb = tint(animatable, partialTick);

        getRenderer().reRender(shell, poseStack, bufferSource, animatable, shellType,
                bufferSource.getBuffer(shellType), partialTick, packedLight, packedOverlay,
                rgb[0], rgb[1], rgb[2], 1.0F);
    }

    private static void copyPose(GeoBone from, GeoBone to) {
        to.updateRotation(from.getRotX(), from.getRotY(), from.getRotZ());
        to.updatePosition(from.getPosX(), from.getPosY(), from.getPosZ());
        to.updateScale(from.getScaleX(), from.getScaleY(), from.getScaleZ());
        for (int i = 0; i < from.getChildBones().size(); i++) {
            copyPose(from.getChildBones().get(i), to.getChildBones().get(i));
        }
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
