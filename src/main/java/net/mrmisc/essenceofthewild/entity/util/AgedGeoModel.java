package net.mrmisc.essenceofthewild.entity.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

import java.util.function.Function;

public class AgedGeoModel<T extends LivingEntity & GeoAnimatable> extends GeoModel<T> {
    private final ResourceLocation model;
    private final ResourceLocation babyModel;
    private final ResourceLocation animations;
    private final ResourceLocation babyAnimations;
    private final Function<T, Variant> variant;

    public AgedGeoModel(String name, String babyName, Function<T, Variant> variant) {
        this(name, babyName, name, babyName, variant);
    }

    public AgedGeoModel(String name, String babyName, String animName, String babyAnimName, Function<T, Variant> variant) {
        this.model = EotwGeoModel.geo(name);
        this.babyModel = EotwGeoModel.geo(babyName);
        this.animations = EotwGeoModel.animations(animName);
        this.babyAnimations = EotwGeoModel.animations(babyAnimName);
        this.variant = variant;
    }

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return animatable.isBaby() ? babyModel : model;
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        Variant v = variant.apply(animatable);
        return animatable.isBaby() ? v.babyLocation() : v.adultLocation();
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return animatable.isBaby() ? babyAnimations : animations;
    }
}
