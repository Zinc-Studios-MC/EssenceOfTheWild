package net.mrmisc.essenceofthewild.entity.custom.cave_spider;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import software.bernie.geckolib.model.GeoModel;

public class CaveSpiderGeoModel extends GeoModel<CaveSpiderEntity> {
    private static final ResourceLocation MODEL = EOTWUtils.getLoc("geo/entity/cave_spider.geo.json");
    private static final ResourceLocation TEXTURE = EOTWUtils.getLoc("textures/entity/cave_spider/cave_spider.png");
    private static final ResourceLocation ANIMATIONS = EOTWUtils.getLoc("animations/entity/cave_spider.animation.json");

    @Override
    public ResourceLocation getModelResource(CaveSpiderEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(CaveSpiderEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(CaveSpiderEntity animatable) {
        return ANIMATIONS;
    }
}
