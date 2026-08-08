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
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

// draws a second dyed shell over the sheep, either the wool or the shorn body under it
// each shell has its own uvs so it cant just be extra bones on the body model, and geckolib only
// animates the renderer's own model, so we copy the body's bone transforms over by name before
// drawing, same idea as copyPropertiesTo on the old vanilla models but without animating twice
@OnlyIn(Dist.CLIENT)
public abstract class SheepOverlayLayer extends GeoRenderLayer<SheepEntity> {

    private final GeoModel<SheepEntity> overlayModel;

    protected SheepOverlayLayer(GeoRenderer<SheepEntity> renderer, GeoModel<SheepEntity> overlayModel) {
        super(renderer);
        this.overlayModel = overlayModel;
    }

    // does this shell apply to the sheep right now
    protected abstract boolean appliesTo(SheepEntity sheep);

    @Override
    public void render(PoseStack poseStack, SheepEntity animatable, BakedGeoModel bakedModel, RenderType renderType,
                       MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick,
                       int packedLight, int packedOverlay) {
        // skip lambs, both shells are adult sized and there is no lamb version, plus the lamb model
        // already has its fleece cubes baked in so it looks fine without them (and you cant shear lambs anyway)
        if (animatable.isBaby() || !appliesTo(animatable)) {
            return;
        }

        BakedGeoModel shell = this.overlayModel.getBakedModel(this.overlayModel.getModelResource(animatable));
        copyPose(bakedModel, shell);

        ResourceLocation texture = this.overlayModel.getTextureResource(animatable);
        RenderType shellType = RenderType.entityCutoutNoCull(texture);
        float[] rgb = tint(animatable, partialTick);

        getRenderer().reRender(shell, poseStack, bufferSource, animatable, shellType,
                bufferSource.getBuffer(shellType), partialTick, packedLight, packedOverlay,
                rgb[0], rgb[1], rgb[2], 1.0F);
    }

    // give the shell the body's animated pose, bones get matched up by name
    private static void copyPose(BakedGeoModel from, BakedGeoModel to) {
        for (GeoBone bone : from.topLevelBones()) {
            copyBone(bone, to);
        }
    }

    private static void copyBone(GeoBone source, BakedGeoModel target) {
        target.getBone(source.getName()).ifPresent(dest -> {
            dest.setRotX(source.getRotX());
            dest.setRotY(source.getRotY());
            dest.setRotZ(source.getRotZ());
            dest.setPosX(source.getPosX());
            dest.setPosY(source.getPosY());
            dest.setPosZ(source.getPosZ());
            dest.setScaleX(source.getScaleX());
            dest.setScaleY(source.getScaleY());
            dest.setScaleZ(source.getScaleZ());
        });

        for (GeoBone child : source.getChildBones()) {
            copyBone(child, target);
        }
    }

    // wool colour including the jeb_ rainbow, tweaked the same way the old layers did it
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
