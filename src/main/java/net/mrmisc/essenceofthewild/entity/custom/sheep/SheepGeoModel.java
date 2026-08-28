package net.mrmisc.essenceofthewild.entity.custom.sheep;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import software.bernie.geckolib.model.GeoModel;

public class SheepGeoModel extends GeoModel<SheepEntity> {
    static final ResourceLocation ANIMATIONS = path("animations/entity/sheep.animation.json");
    private static final ResourceLocation MODEL = path("geo/entity/sheep.geo.json");
    private static final ResourceLocation BABY_MODEL = path("geo/entity/baby_sheep.geo.json");

    @Override
    public ResourceLocation getModelResource(SheepEntity animatable) {
        return animatable.isBaby() ? BABY_MODEL : MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(SheepEntity animatable) {
        SheepVariant variant = animatable.getVariant();
        return animatable.isBaby() ? variant.babyLocation() : variant.adultLocation();
    }

    @Override
    public ResourceLocation getAnimationResource(SheepEntity animatable) {
        return ANIMATIONS;
    }

    static ResourceLocation path(String path) {
        return ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, path);
    }
}
