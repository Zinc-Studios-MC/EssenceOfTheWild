package net.mrmisc.essenceofthewild.entity.custom.rabbit;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import software.bernie.geckolib.model.GeoModel;

public class RabbitGeoModel extends GeoModel<RabbitEntity> {
    private static final ResourceLocation MODEL = EOTWUtils.getLoc("geo/entity/rabbit.geo.json");
    private static final ResourceLocation ANIMATIONS = EOTWUtils.getLoc("animations/entity/rabbit.animation.json");

    @Override
    public ResourceLocation getModelResource(RabbitEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(RabbitEntity animatable) {
        return animatable.getRabbitVariant().location();
    }

    @Override
    public ResourceLocation getAnimationResource(RabbitEntity animatable) {
        return ANIMATIONS;
    }
}
