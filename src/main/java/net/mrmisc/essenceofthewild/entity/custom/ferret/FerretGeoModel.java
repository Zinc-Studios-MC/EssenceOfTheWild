package net.mrmisc.essenceofthewild.entity.custom.ferret;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import software.bernie.geckolib.model.GeoModel;

public class FerretGeoModel extends GeoModel<FerretEntity> {
    private static final ResourceLocation ADULT_MODEL = EOTWUtils.getLoc("geo/entity/ferret.geo.json");
    private static final ResourceLocation BABY_MODEL = EOTWUtils.getLoc("geo/entity/baby_ferret.geo.json");
    private static final ResourceLocation ADULT_ANIMATIONS = EOTWUtils.getLoc("animations/entity/ferret.animation.json");
    private static final ResourceLocation BABY_ANIMATIONS = EOTWUtils.getLoc("animations/entity/baby_ferret.animation.json");

    @Override
    public ResourceLocation getModelResource(FerretEntity animatable) {
        return animatable.isBaby() ? BABY_MODEL : ADULT_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(FerretEntity animatable) {
        FerretVariant variant = animatable.getVariant();
        return animatable.isBaby() ? variant.babyLocation() : variant.adultLocation();
    }

    @Override
    public ResourceLocation getAnimationResource(FerretEntity animatable) {
        return animatable.isBaby() ? BABY_ANIMATIONS : ADULT_ANIMATIONS;
    }
}
