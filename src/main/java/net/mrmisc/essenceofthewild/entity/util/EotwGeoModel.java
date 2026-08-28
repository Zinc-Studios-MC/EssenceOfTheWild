package net.mrmisc.essenceofthewild.entity.util;

import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

import java.util.function.Function;

public class EotwGeoModel<T extends GeoAnimatable> extends GeoModel<T> {
    private final ResourceLocation model;
    private final ResourceLocation animations;
    private final Function<T, ResourceLocation> texture;

    public EotwGeoModel(String name, ResourceLocation texture) {
        this(name, name, animatable -> texture);
    }

    public EotwGeoModel(String name, Function<T, ResourceLocation> texture) {
        this(name, name, texture);
    }

    public EotwGeoModel(String geoName, String animName, Function<T, ResourceLocation> texture) {
        this.model = geo(geoName);
        this.animations = animations(animName);
        this.texture = texture;
    }

    static ResourceLocation geo(String name) {
        return EOTWUtils.getLoc("geo/entity/" + name + ".geo.json");
    }

    static ResourceLocation animations(String name) {
        return EOTWUtils.getLoc("animations/entity/" + name + ".animation.json");
    }

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return texture.apply(animatable);
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return animations;
    }
}
