package net.mrmisc.essenceofthewild.block.entity.custom.nest;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.ModelEvent;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;

public class NestBlockEntityRenderer implements BlockEntityRenderer<NestBlockEntity> {
    private static final ResourceLocation[] CHICKEN_EGG_MODELS = {
            model("block/eggs_1"),
            model("block/eggs_2"),
            model("block/eggs_3")
    };
    private static final ResourceLocation[] DUCK_EGG_MODELS = {
            model("block/duck_eggs_1"),
            model("block/duck_eggs_2"),
            model("block/duck_eggs_3")
    };

    private final BlockRenderDispatcher blockRenderer;

    public NestBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        blockRenderer = context.getBlockRenderDispatcher();
    }

    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        for (ResourceLocation model : CHICKEN_EGG_MODELS) {
            event.register(model);
        }
        for (ResourceLocation model : DUCK_EGG_MODELS) {
            event.register(model);
        }
    }

    @Override
    public void render(NestBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        int eggCount = blockEntity.getEggCount();
        if (eggCount <= 0) {
            return;
        }

        float progress = blockEntity.getHatchProgressRatio();
        float ticks = blockEntity.getAnimationTicks() + partialTick;
        float shake = getShakeAmount(progress, ticks);
        float bob = progress > 0.9F ? Mth.sin(ticks * 0.35F) * 0.018F : 0.0F;
        float scale = 1.0F + progress * 0.05F + (progress > 0.95F ? Mth.sin(ticks * 0.55F) * 0.035F : 0.0F);

        ResourceLocation[] models = EOTWEntities.DUCK.getId().equals(blockEntity.getHatchEntityId())
                ? DUCK_EGG_MODELS
                : CHICKEN_EGG_MODELS;
        BakedModel model = blockRenderer.getBlockModelShaper()
                .getModelManager()
                .getModel(models[Mth.clamp(eggCount, 1, models.length) - 1]);

        if (model == Minecraft.getInstance().getModelManager().getMissingModel()) {
            return;
        }
        assert blockEntity.getLevel() != null;
        int light = LevelRenderer.getLightColor(
                blockEntity.getLevel(),
                blockEntity.getBlockPos().above()
        );
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.29F + bob, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(ticks * 0.12F) * 5.0F * progress));
        poseStack.mulPose(Axis.ZP.rotationDegrees(shake));
        poseStack.mulPose(Axis.XP.rotationDegrees(-shake * 0.45F));
        poseStack.scale(scale, scale, scale);
        poseStack.translate(-0.5F, -0.31F, -0.5F);

        RenderType renderType = RenderType.cutout();
        blockRenderer.getModelRenderer().renderModel(
                poseStack.last(),
                buffer.getBuffer(renderType),
                blockEntity.getBlockState(),
                model,
                1.0F,
                1.0F,
                1F,
                light,
                packedOverlay
        );
        poseStack.popPose();
    }

    private static float getShakeAmount(float progress, float ticks) {
        int interval = Math.max(6, (int) Mth.lerp(progress, 84.0F, 8.0F));
        boolean constantShake = progress > 0.88F;
        boolean activeShake = constantShake || ((int) ticks % interval) < Mth.lerp(progress, 7.0F, 15.0F);

        if (!activeShake) {
            return 0.0F;
        }

        float strength = Mth.lerp(progress, 2.0F, 12.0F);
        return Mth.sin(ticks * Mth.lerp(progress, 0.55F, 1.7F)) * strength;
    }

    private static ResourceLocation model(String path) {
        return ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, path);
    }
}
