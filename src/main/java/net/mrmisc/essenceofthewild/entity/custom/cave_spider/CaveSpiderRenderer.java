package net.mrmisc.essenceofthewild.entity.custom.cave_spider;

import net.mrmisc.essenceofthewild.util.EOTWUtils;
import net.mrmisc.essenceofthewild.entity.util.EotwGeoModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CaveSpiderRenderer extends GeoEntityRenderer<CaveSpiderEntity> {
    public CaveSpiderRenderer(EntityRendererProvider.Context context) {
        super(context, new EotwGeoModel<>("cave_spider", EOTWUtils.getLoc("textures/entity/cave_spider/cave_spider.png")));
        this.shadowRadius = 0.4f;
    }

    @Override
    protected float getDeathMaxRotation(CaveSpiderEntity animatable) {
        return 0.0F;
    }
}
