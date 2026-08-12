package net.mrmisc.essenceofthewild.entity.misc.silk_ball;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.entity.misc.SilkBall;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import software.bernie.geckolib.model.GeoModel;

public class SilkBallGeoModel extends GeoModel<SilkBall> {
    private static final ResourceLocation MODEL = EOTWUtils.getLoc("geo/entity/silk_ball.geo.json");
    private static final ResourceLocation TEXTURE = EOTWUtils.getLoc("textures/entity/misc/silk_ball.png");
    private static final ResourceLocation ANIMATIONS = EOTWUtils.getLoc("animations/entity/silk_ball.animation.json");

    @Override
    public ResourceLocation getModelResource(SilkBall animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(SilkBall animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(SilkBall animatable) {
        return ANIMATIONS;
    }
}
