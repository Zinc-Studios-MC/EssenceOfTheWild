package net.mrmisc.essenceofthewild.entity.custom.mooshroom;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import software.bernie.geckolib.model.GeoModel;

public class MooshroomGeoModel extends GeoModel<MooshroomEntity> {
    private static final ResourceLocation ADULT_MODEL = path("geo/entity/mooshroom.geo.json");
    private static final ResourceLocation BABY_MODEL = path("geo/entity/baby_mooshroom.geo.json");
    private static final ResourceLocation ADULT_ANIMATIONS = path("animations/entity/mooshroom.animation.json");
    private static final ResourceLocation BABY_ANIMATIONS = path("animations/entity/baby_mooshroom.animation.json");

    @Override
    public ResourceLocation getModelResource(MooshroomEntity animatable) {
        return animatable.isBaby() ? BABY_MODEL : ADULT_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(MooshroomEntity animatable) {
        MooshroomVariant variant = animatable.getVariantMooshroom();
        return animatable.isBaby() ? variant.babyLocation() : variant.adultLocation();
    }

    @Override
    public ResourceLocation getAnimationResource(MooshroomEntity animatable) {
        return animatable.isBaby() ? BABY_ANIMATIONS : ADULT_ANIMATIONS;
    }

    private static ResourceLocation path(String path) {
        return ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, path);
    }
}
