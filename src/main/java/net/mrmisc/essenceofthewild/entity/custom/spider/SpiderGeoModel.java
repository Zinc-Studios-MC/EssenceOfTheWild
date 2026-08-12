package net.mrmisc.essenceofthewild.entity.custom.spider;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import software.bernie.geckolib.model.GeoModel;

public class SpiderGeoModel extends GeoModel<SpiderEntity> {
    private static final ResourceLocation MODEL = EOTWUtils.getLoc("geo/entity/spider.geo.json");
    private static final ResourceLocation TEXTURE = EOTWUtils.getLoc("textures/entity/spider/spider.png");
    private static final ResourceLocation ANIMATIONS = EOTWUtils.getLoc("animations/entity/spider.animation.json");

    @Override
    public ResourceLocation getModelResource(SpiderEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(SpiderEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(SpiderEntity animatable) {
        return ANIMATIONS;
    }
}
