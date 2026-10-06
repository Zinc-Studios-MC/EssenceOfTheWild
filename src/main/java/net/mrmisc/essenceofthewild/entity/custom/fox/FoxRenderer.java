package net.mrmisc.essenceofthewild.entity.custom.fox;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.mrmisc.essenceofthewild.entity.util.EotwGeoModel;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

public class FoxRenderer extends GeoEntityRenderer<FoxEntity> {
    public FoxRenderer(EntityRendererProvider.Context context) {
        super(context, new EotwGeoModel<FoxEntity>("fox", FoxRenderer::texture) {
            @Override
            public ResourceLocation getModelResource(FoxEntity fox) {
                return EOTWUtils.getLoc("geo/entity/" + (fox.isBaby() ? "baby_fox" : "fox") + ".geo.json");
            }

            @Override
            public ResourceLocation getAnimationResource(FoxEntity fox) {
                return EOTWUtils.getLoc("animations/entity/" + (fox.isBaby() ? "baby_fox" : "fox") + ".animation.json");
            }

            @Override
            public void setCustomAnimations(FoxEntity fox, long id, AnimationState<FoxEntity> state) {
                var root = getAnimationProcessor().getBone("bone");
                var head = getAnimationProcessor().getBone("head");
                if (fox.isSleeping()) {
                    float z = fox.isBaby() ? 2.5F : 3.5F;
                    root.setPosX(Mth.sin(root.getRotY()) * z);
                    root.setPosZ(-Mth.cos(root.getRotY()) * z);
                } else if (fox.isPouncing() || fox.isFaceplanted()) {
                    root.setRotX(-Mth.lerp(state.getPartialTick(), fox.xRotO, fox.getXRot()) * Mth.DEG_TO_RAD);
                } else if (fox.isCrouching()) {
                    root.setPosY(-fox.getCrouchAmount(state.getPartialTick()));
                } else if (!fox.isSleeping() && !fox.isSitting()) {
                    EntityModelData data = state.getData(DataTickets.ENTITY_MODEL_DATA);
                    head.setRotX(head.getRotX() + data.headPitch() * Mth.DEG_TO_RAD);
                    head.setRotY(head.getRotY() + data.netHeadYaw() * Mth.DEG_TO_RAD);
                    head.setRotZ(head.getRotZ() + fox.getHeadRollAngle(state.getPartialTick()));
                }
            }
        });
        shadowRadius = 0.4F;
        addRenderLayer(new FoxCollarLayer(this));
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Override
            protected ItemStack getStackForBone(GeoBone bone, FoxEntity fox) {
                return bone.getName().equals("mouth") ? fox.getMainHandItem() : null;
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, FoxEntity fox) {
                return ItemDisplayContext.NONE;
            }

            @Override
            protected void renderStackForBone(PoseStack pose, GeoBone bone, ItemStack stack, FoxEntity fox,
                                              MultiBufferSource buffers, float partialTick, int light, int overlay) {
                pose.mulPose(Axis.XP.rotationDegrees(90));
                float scale = fox.isBaby() ? 0.4F : 0.5F;
                pose.scale(scale, scale, scale);
                super.renderStackForBone(pose, bone, stack, fox, buffers, partialTick, light, overlay);
            }
        });
    }

    @Override
    public void render(FoxEntity fox, float yaw, float partialTick, PoseStack pose, MultiBufferSource buffers, int light) {
        shadowRadius = fox.isSleeping() ? (fox.isBaby() ? 0.8F : 0.5F) : 0.4F;
        super.render(fox, yaw, partialTick, pose, buffers, light);
    }

    private static ResourceLocation texture(FoxEntity fox) {
        String color = fox.getVariant() == Fox.Type.SNOW ? "snow_fox" : fox.isBlack() ? "black_fox" : "fox";
        String name = (fox.isBaby() ? "baby_" : "") + (fox.isSleeping() ? "sleeping_" : "") + color;
        return EOTWUtils.getLoc("textures/entity/fox/" + name + "_texture.png");
    }
}
