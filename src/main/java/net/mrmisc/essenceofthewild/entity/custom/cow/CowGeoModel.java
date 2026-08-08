package net.mrmisc.essenceofthewild.entity.custom.cow;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import software.bernie.geckolib.model.GeoModel;

// adult and calf share this model, only the geo and animation files differ
// both use the same animation.cow.* names so the same RawAnimations drive either
public class CowGeoModel extends GeoModel<CowEntity> {
    private static final ResourceLocation ADULT_MODEL = path("geo/entity/cow.geo.json");
    private static final ResourceLocation BABY_MODEL = path("geo/entity/baby_cow.geo.json");
    private static final ResourceLocation ADULT_ANIMATIONS = path("animations/entity/cow.animation.json");
    private static final ResourceLocation BABY_ANIMATIONS = path("animations/entity/baby_cow.animation.json");

    @Override
    public ResourceLocation getModelResource(CowEntity animatable) {
        return animatable.isBaby() ? BABY_MODEL : ADULT_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(CowEntity animatable) {
        CowVariant variant = animatable.getVariant();
        return animatable.isBaby() ? variant.babyLocation() : variant.adultLocation();
    }

    @Override
    public ResourceLocation getAnimationResource(CowEntity animatable) {
        return animatable.isBaby() ? BABY_ANIMATIONS : ADULT_ANIMATIONS;
    }

    private static ResourceLocation path(String path) {
        return ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, path);
    }
}
