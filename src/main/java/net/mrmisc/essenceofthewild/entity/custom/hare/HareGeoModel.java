package net.mrmisc.essenceofthewild.entity.custom.hare;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import software.bernie.geckolib.model.GeoModel;

public class HareGeoModel extends GeoModel<HareEntity> {
    private static final ResourceLocation MODEL = EOTWUtils.getLoc("geo/entity/hare.geo.json");
    private static final ResourceLocation ANIMATIONS = EOTWUtils.getLoc("animations/entity/hare.animation.json");

    @Override
    public ResourceLocation getModelResource(HareEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(HareEntity animatable) {
        return animatable.getRabbitVariant().location();
    }

    @Override
    public ResourceLocation getAnimationResource(HareEntity animatable) {
        return ANIMATIONS;
    }
}
