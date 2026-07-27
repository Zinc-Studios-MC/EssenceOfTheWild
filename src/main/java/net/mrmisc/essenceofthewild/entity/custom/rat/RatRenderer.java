package net.mrmisc.essenceofthewild.entity.custom.rat;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RatRenderer extends MobRenderer<RatEntity, RatModel> {

    public RatRenderer(Context context) {
        super(context, new RatModel(context.bakeLayer(RatModel.LAYER_LOCATION)), 0.3f);
        this.addLayer(new RatCollarLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(RatEntity entity) {
        // Easter egg: a rat named "fishguy" wears the fishguy texture regardless of its variant.
        if (entity.hasCustomName()) {
            String name = ChatFormatting.stripFormatting(entity.getName().getString());
            if (name != null && name.equalsIgnoreCase("fishguy")) {
                return entity.isRatAngry()
                        ? RatVariants.FISHGUY.angryLocation()
                        : RatVariants.FISHGUY.location();
            }
        }
        RatVariant variant = entity.getVariant();
        return entity.isRatAngry() ? variant.angryLocation() : variant.location();
    }

    @Override
    protected void scale(RatEntity entity, PoseStack poseStack, float partialTick) {
        if (entity.isBaby()) {
            poseStack.scale(0.55f, 0.55f, 0.55f);
        }
        super.scale(entity, poseStack, partialTick);
    }
}
