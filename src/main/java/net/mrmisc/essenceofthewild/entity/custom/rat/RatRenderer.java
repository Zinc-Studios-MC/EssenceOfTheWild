package net.mrmisc.essenceofthewild.entity.custom.rat;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class RatRenderer extends MobRenderer<RatEntity, RatModel> {

    // every baby rat uses the same texture no matter the variant
    private static final ResourceLocation BABY_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            EssenceOfTheWildMod.MOD_ID, "textures/entity/rat/baby_rat.png");

    private final RatModel adult;
    private final BabyRatModel baby;

    public RatRenderer(Context context) {
        super(context, new RatModel(context.bakeLayer(RatModel.LAYER_LOCATION)), 0.3f);
        this.adult = new RatModel(context.bakeLayer(RatModel.LAYER_LOCATION));
        this.baby = new BabyRatModel(context.bakeLayer(BabyRatModel.LAYER_LOCATION));
        this.addLayer(new RatCollarLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(RatEntity entity) {
        if (entity.isBaby()) {
            return BABY_TEXTURE;
        }
        // easter egg, name a rat fishguy and it gets the fishguy texture whatever its variant is
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
    public void render(RatEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight) {
        this.model = entity.isBaby() ? this.baby : this.adult;
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
