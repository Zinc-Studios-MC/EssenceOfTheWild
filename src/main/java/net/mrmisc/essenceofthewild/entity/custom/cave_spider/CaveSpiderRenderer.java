package net.mrmisc.essenceofthewild.entity.custom.cave_spider;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CaveSpiderRenderer extends GeoEntityRenderer<CaveSpiderEntity> {
    public CaveSpiderRenderer(EntityRendererProvider.Context context) {
        super(context, new CaveSpiderGeoModel());
        this.shadowRadius = 0.4f;
    }

    @Override
    protected float getDeathMaxRotation(CaveSpiderEntity animatable) {
        return 0.0F;
    }
}
