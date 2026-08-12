package net.mrmisc.essenceofthewild.entity.custom.spider;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SpiderRenderer extends GeoEntityRenderer<SpiderEntity> {
    public SpiderRenderer(EntityRendererProvider.Context context) {
        super(context, new SpiderGeoModel());
        this.shadowRadius = 0.7f;
    }

    @Override
    protected float getDeathMaxRotation(SpiderEntity animatable) {
        return 0.0F;
    }
}
