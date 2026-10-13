package net.mrmisc.essenceofthewild.entity.custom.wolf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.mrmisc.essenceofthewild.entity.util.EotwGeoModel;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

public class WolfRenderer extends GeoEntityRenderer<WolfEntity> {
    public WolfRenderer(EntityRendererProvider.Context context) {
        super(context, new EotwGeoModel<WolfEntity>("wolf", WolfRenderer::texture) {
            @Override
            public ResourceLocation getModelResource(WolfEntity wolf) {
                return EOTWUtils.getLoc("geo/entity/" + (wolf.isBaby() ? "baby_wolf" : "wolf") + ".geo.json");
            }

            @Override
            public ResourceLocation getAnimationResource(WolfEntity wolf) {
                return EOTWUtils.getLoc("animations/entity/" + (wolf.isBaby() ? "baby_wolf" : "wolf") + ".animation.json");
            }

            @Override
            public void setCustomAnimations(WolfEntity wolf, long id, AnimationState<WolfEntity> state) {
                var head = getAnimationProcessor().getBone("head");
                if (!wolf.isInSittingPose()) {
                    EntityModelData data = state.getData(DataTickets.ENTITY_MODEL_DATA);
                    head.setRotX(head.getRotX() + data.headPitch() * Mth.DEG_TO_RAD);
                    head.setRotY(head.getRotY() + data.netHeadYaw() * Mth.DEG_TO_RAD);
                }
                head.setRotZ(head.getRotZ() + wolf.getHeadRollAngle(state.getPartialTick())
                        + wolf.getBodyRollAngle(state.getPartialTick(), 0));
                var body = getAnimationProcessor().getBone("body");
                body.setRotZ(body.getRotZ() + wolf.getBodyRollAngle(state.getPartialTick(), -0.16F));
                var tail = getAnimationProcessor().getBone("tail");
                tail.setRotZ(tail.getRotZ() + wolf.getBodyRollAngle(state.getPartialTick(), -0.2F));
            }
        });
        shadowRadius = 0.5F;
        addRenderLayer(new WolfCollarLayer(this));
        addRenderLayer(new WolfEyesLayer(this));
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Override
            protected ItemStack getStackForBone(GeoBone bone, WolfEntity wolf) {
                return bone.getName().equals("mouth") ? wolf.getMainHandItem() : null;
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, WolfEntity wolf) {
                return ItemDisplayContext.NONE;
            }

            @Override
            protected void renderStackForBone(PoseStack pose, GeoBone bone, ItemStack stack, WolfEntity wolf,
                                              MultiBufferSource buffers, float partialTick, int light, int overlay) {
                pose.mulPose(Axis.XP.rotationDegrees(90));
                float scale = wolf.isBaby() ? 0.35F : 0.5F;
                pose.scale(scale, scale, scale);
                super.renderStackForBone(pose, bone, stack, wolf, buffers, partialTick, light, overlay);
            }
        });
    }

    private static ResourceLocation texture(WolfEntity wolf) {
        if (wolf.isBaby()) {
            return wolf.getVariant().babyLocation();
        }
        ResourceLocation texture = wolf.getVariant().adultLocation();
        if (wolf.isAngry()) {
            return EOTWUtils.getLoc(texture.getPath().replace("wolf/", "wolf/angry_"));
        }
        return texture;
    }
}
